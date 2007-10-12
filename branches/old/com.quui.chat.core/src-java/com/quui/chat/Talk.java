/** 
 Project "com.quui.chat.core" (C) 2004 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.quui.chat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import com.quui.chat.io.IO;
import com.quui.chat.mind.Mind;
import com.quui.chat.mind.wn.WNLookup;

/**
 * Access point to the chatbot, the interface between ui and logic
 * 
 * @author Fabian Steeg (fsteeg)
 */
public class Talk {

    private Mind mind;

    private String topicFileLocation;

    public Talk(String topicFolder, String topicFile, String wnLocation,
            String logFolder) {
        this.topicFileLocation = topicFolder + File.separator + topicFile;
        mind = getMindInstance(topicFolder, topicFile, wnLocation, logFolder);
    }

    /**
     * @param input
     *            The user input
     * @param learn
     *            If true mind will learn
     * @return The answer on the input
     */
    public String process(String input, boolean learn) {
        String result = null;
        try {
            result = mind.processConversation(input, learn);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * for console-use
     */
    public void talk() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    System.in, "UTF-8"));
            String s;
            System.out.println();
            System.out.println();
            System.out.println(":| hi there");
            boolean keepOn = true;
            while (keepOn) {
                s = in.readLine();
                if (s.equalsIgnoreCase("bye"))
                    keepOn = false;
                else
                    System.out.println(process(s, true));
            }
            System.out.println(" cu");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * @param topicFolder
     *            The folder containing the topic file
     * @param topicFile
     *            The actual topic file
     * @param wnLocation
     *            The path to the file_properties.xml file in wordnet
     * @param logFolder
     *            The folder to log to
     * @return The Mind
     */
    private Mind getMindInstance(String topicFolder, String topicFile,
            String wnLocation, String logFolder) {
        if (mind != null) {
            return mind;
        } else {
            Log.init(logFolder);
            long s = System.currentTimeMillis();
            try {
                String loc = topicFolder;
                IO io = new IO(loc + File.separator + topicFile);
                Vector<String> stopwords = io.getStopwords(loc + File.separator
                        + "stopwords");
                Vector<String> answerDummies = io.getStopwords(loc
                        + File.separator + "answer-dummies");
                new WNLookup().init(wnLocation);
                mind = new Mind(stopwords, io.getTopics(), io.getMap(),
                        answerDummies);
                Log.logger.info("mind init complete, took "
                        + (System.currentTimeMillis() - s) + " ms.");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mind;
        }

    }

    /**
     * Saves the mind to XML
     */
    public void save() {
        try {
            mind.saveMap(topicFileLocation);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return The mind
     */
    public Mind getMind() {
        return mind;
    }

    public void deleteLastAnswer() {
        System.out.println("Going to delete: " + mind.lastAnswer);
        Set<String> answers = mind.lastTopic.getAnswers().keySet();
        for (String string : answers) {
            System.out.println("Before: " + string);
        }
        System.out.println("-------------------------------------");
        answers.remove(mind.lastAnswer);
        for (String string : answers) {
            System.out.println("Remaining: " + string);
        }
        save();
    }

}