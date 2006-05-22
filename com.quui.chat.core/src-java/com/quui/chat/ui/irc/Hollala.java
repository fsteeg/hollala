/** 
 Project "com.quui.chat.core" (C) 2006 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.quui.chat.ui.irc;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import com.quui.chat.GlobalProperties;
import com.quui.chat.Log;
import com.quui.chat.Preprocessor;
import com.quui.chat.Talk;

/**
 * Based on Testbot extends BasicIRCBot by David Seager
 * http://www-128.ibm.com/developerworks/library/j-javabot/index.html
 * 
 * Hollala the Chatbot, IRC Interface
 * 
 * @author Fabian Steeg (fsteeg)
 */
public class Hollala extends SuperBot {

    /**
     * Lists for different categories of users, content comes from property file
     */
    private List<String> users, admins, ignore, listen;

    /**
     * Config data, content comes from property file
     */
    private String name, description, logLocation, wnLocation, topicFolder,
            topicFile;

    /**
     * The connection to the Chatbot Engine
     */
    private Talk talk;

    /**
     * The Random for generating variations of answers
     */
    private Random r = new Random();

    /**
     * Properties containing content of the above fields
     */
    private Properties properties;

    /**
     * Instantiate a new bot
     * 
     * @param configFile
     *            The location of the properties file to use
     */
    public Hollala(String configFile) {
        super();
        initProperties(configFile);
        super.setBotName(name);
        super.setBotDescription(description);
        initCoreBot();
        initIRCBot();
    }

    /**
     * Initializes the content of the fields
     * 
     * @param propertiesLocation
     *            The location of the properties file to use
     */
    private void initProperties(String propertiesLocation) {
        properties = new Properties();
        loadProperties(propertiesLocation);
        this.name = properties.getProperty("name");
        this.server = properties.getProperty("server");
        this.description = properties.getProperty("description");
        this.logLocation = GlobalProperties.getInstance().getLogLocation()
                + "-" + name.toLowerCase();
        this.wnLocation = GlobalProperties.getInstance().getWNLocation();
        this.topicFolder = GlobalProperties.getInstance().getConfigLocation();
        this.topicFile = properties.getProperty("topics") + "-"
                + name.toLowerCase() + ".xml";
        this.channels = getItems("channels");
        this.users = new LinkedList<String>(Arrays.asList(getItems("users")));
        this.admins = new LinkedList<String>(Arrays.asList(getItems("admins")));
        this.ignore = new LinkedList<String>(Arrays.asList(getItems("ignore")));
        this.listen = new LinkedList<String>(Arrays.asList(getItems("listen")));
    }

    /**
     * @param propertiesLocation
     *            The location of the properties file to load
     */
    private void loadProperties(String propertiesLocation) {
        try {
            FileInputStream in = new FileInputStream(propertiesLocation);
            properties.load(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load properties file: "
                    + propertiesLocation + ".");
        }
    }

    /**
     * @param key
     *            The key int the properties file
     * @return An array containing the comma-serapated elements in the value for
     *         key
     */
    private String[] getItems(String key) {
        return properties.getProperty(key).split(",");
    }

    /**
     * Loads the topics by initializing the Talk-Object
     */
    private void initCoreBot() {
        talk = new Talk(topicFolder, topicFile, wnLocation, logLocation);
    }

    /**
     * Process a private message
     */
    protected void srv_privmsg(String sender, String message, String channel) {
        Log.logger.debug("Message: " + message + " from: " + sender
                + " on channel: " + channel);
        if (message.equals("!quit") && admins.contains(sender.toLowerCase()))
            System.exit(0);
        // build up the knowledge of users... TODO actually the users shoudl be
        // requested from the server...
        users.add(sender);
        // !forget-command...
        handleForget(sender, message, channel);
        boolean toBot = message.toLowerCase().contains(name.toLowerCase());
        // processing everytihng cause performance probs!
        if (!toBot || ignore.contains(sender.toLowerCase())) {
            return;
        }
        // first filter: ascii - to filter arabic stuff and such.
        if (Preprocessor.containsNonAscii(message)) {
            Log.logger.debug("Skipping because of non-ascii: " + message);
            return;
        }
        boolean learn = toBot || listen.contains(sender.toLowerCase());
        String orig = message;
        for (String nick : users) {
            nick = nick.toLowerCase();
            // clean user names from message:
            if (!nick.equals(name.toLowerCase())) {
                message = Preprocessor.clean(message, nick);
            }
        }
        boolean equal = orig.equals(message.trim());
        message = Preprocessor.clean(message, name.toLowerCase());
        // save only if not other users were removed, results in nonsensical
        // sentences, allow only one missing, like "?"
        String answer = converse(channel, sender, message, learn && equal);
        if (talk.getMind().interested)
            learn = true;
        if (learn) {
            Log.logger.info("Will reply on: " + sender + " in channel "
                    + channel + " says: <<< " + orig + " | cleaned to: "
                    + message + ", will learn is: " + learn);
            Log.logger
                    .info("Believing topic is: \""
                            + talk.getMind().getLastTopicName()
                            + ", will answer in channel " + channel + ": >>> "
                            + answer);
            this.send_privmsg(channel, answer);
        }

    }

    /***/
    private void handleForget(String sender, String message, String channel) {
        if (message.equals("!forget")) {
            if (admins.contains(sender)) {
                String answer = "";
                if (talk.getMind().lastAnswer == null)
                    answer = "Hm... I ain't got no thing to forget...";
                else {
                    answer = "Uh... I'm forgetting about '"
                            + this.talk.getMind().lastAnswer + "'";
                    talk.deleteLastAnswer();
                }
                this.send_privmsg(channel, answer);
            } else {
                this.send_privmsg(channel, "Who are you to mess with my mind, "
                        + sender + " :P");
            }
        }
    }

    /**
     * Retrieves an answer for message and answers on channel
     * 
     * @param channel
     *            The channel
     * @param sender
     *            The sender
     * @param message
     *            The message to answer
     */
    private String converse(String channel, String sender, String message,
            boolean answerInAnyCase) {
        // get an answer for the message
        String answer = talk.process(message.trim().toLowerCase(),
                answerInAnyCase);
        try {
            System.out.println("Waiting...");
            Thread.sleep(message.split(" ").length * 100);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        if (answer == null) {
            throw new NullPointerException(
                    "Mind returned null as answer - this could mean there is an error in the topics.xml or other nasty stuff.");
        }
        answer = constructFinalAnswer(sender, answer);
        try {
            System.out.println("Waiting...");
            Thread.sleep(answer.split(" ").length * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (answerInAnyCase)
            talk.save();
        return answer;
    }

    /**
     * @param sender
     *            The sender
     * @param answer
     *            The answer
     * @return A string lik "sender, answer" or "answer, sender" or "answer"
     */
    private String constructFinalAnswer(String sender, String answer) {
        // normal reply, with randomly generated variations
        int variants = 5;
        int v = r.nextInt(variants + 3);
        if (v == 0) {
            answer = sender + ": " + answer;
        } else if (v == 1) {
            answer = sender + ", " + answer;
        } else if (v == 2) {
            answer = answer + ", " + sender;
        } else if (v == 3) {
            answer = sender + " " + answer;
        } else if (v == 4) {
            answer = answer + " " + sender;
        }
        return answer;
    }

}