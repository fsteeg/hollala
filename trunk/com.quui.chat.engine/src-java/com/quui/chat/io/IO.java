/** 
 Project "com.quui.chat.core" (C) 2004 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package com.quui.chat.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Map;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;

import com.quui.chat.Log;
import com.quui.chat.mind.Topic;

/**
 * Handles IO for stopwords and topic files
 * 
 * @author Fabian Steeg (fsteeg)
 */
public class IO {
    private Document doc;

    private Vector<Topic> topics;

    private Map<String, Vector<Integer>> map;

    /**
     * @param file
     *            The XML file containing the topics
     */
    public IO(String file) {
        super();
        loadTopics(file);
    }

    /**
     * @param file
     *            The location of the stopwords file
     * @return The loaded stopwords
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Vector<String> getStopwords(String file)
            throws FileNotFoundException, IOException {
        Vector<String> result = new Vector<String>();
        FileReader in = new FileReader(file);
        BufferedReader linein = new LineNumberReader(in);
        String word;
        while ((word = linein.readLine()) != null) {
            result.add(word);
        }
        return result;

    }

    /**
     * Loads an XML file containing the topics using SAX
     * 
     * @param file
     *            The location of the file contaiing the topics
     */
    private void loadTopics(String file) {
        try {
            File f = new File(file);
            if (!f.exists()) {
                Log.logger.info("Creating new topics file: " + file);
                f.createNewFile();
                FileWriter w = new FileWriter(f);
                w
                        .write("<?xml version=\"1.0\" encoding=\"UTF-8\"?> <chat-map/>");
                w.close();
            }
            SAXBuilder sb = new SAXBuilder();
            this.doc = sb.build(new FileReader(file));
            try {
                DOMToMap dom2map = new DOMToMap(this.doc);
                this.map = dom2map.getMap();
                this.topics = dom2map.getTopics();
            } catch (Throwable x) {
                x.printStackTrace();
            }
        } catch (Throwable x) {
            x.printStackTrace();
        }
    }

    /**
     * @return The Topics loaded by this IO
     */
    public Vector<Topic> getTopics() {
        return this.topics;
    }

    /**
     * @return The Mapping of words to topics (as Vector of Integers)
     */
    public Map<String, Vector<Integer>> getMap() {
        return this.map;
    }
}