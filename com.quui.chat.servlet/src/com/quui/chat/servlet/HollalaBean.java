/**
 * @author Fabian Steeg
 * Created on 05.02.2006
 *
 * @version $Revision: 1.1 $
 * 
 * $Log: HollalaBean.java,v $
 * Revision 1.1  2006/03/26 20:32:12  fsteeg
 * cleaned source
 *
 * Revision 1.2  2006/02/05 21:45:59  fsteeg
 * jsp, topic file selectable basic working
 *
 * Revision 1.1  2006/02/05 16:01:50  fsteeg
 * jsp
 *
 */
package com.quui.chat.servlet;

import java.io.File;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import com.quui.chat.StaticResources;
import com.quui.chat.mind.Mind;

public class HollalaBean {

    private Mind mind;

    private String emotion;

    private String topic;

    private Vector<String> conversation;
    
//    private String topicFile;

    private String LOGS;

    private String WN;

    private String TOPICS;

    private String topicFileName = "default.xml";

//    public String getTopicFile() {
//        return topicFile;
//    }

    public void setTopicFileName(String topicFile) {
        System.out.println("Setting topic file to: " + topicFile);
        mind = StaticResources.getMindInstance(TOPICS,topicFile,WN,LOGS);
        this.topicFileName = mind.getTopicFileName();
    }

    public String getConversationString() {
        StringBuilder buf = new StringBuilder();
        for (String s : conversation) {
            buf.append(s + "<br>");
        }
        return buf.toString();
    }

    public Vector<String> getConversation() {
        return conversation;
    }

    public void setConversation(Vector<String> conversation) {
        this.conversation = conversation;
    }

    public String getEmotion() {
        return mind.getEmotion();
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getTopic() {
        
        String lastTopicName = mind.getLastTopicName();
        if (lastTopicName.contains(";")) {
            return lastTopicName.substring(0, lastTopicName.indexOf(';'));
        }
        else return lastTopicName;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public HollalaBean() {
        System.out.println("CONSOCONS!!!");
        conversation = new Vector<String>();
        conversation.add("last input and answer <br>");
        String HOME = "Users";
        TOPICS = "/" + HOME + "/fsteeg/bin/dyirbal/config";
        WN = "/" + HOME
                + "/fsteeg/bin/dyirbal/wn/file_properties_unix.xml";
        LOGS = "/" + HOME + "/fsteeg/logs";
        // conversation.add("Hollala: " + this.getHollalaAnswer());
    }

    /**
     * 
     */
    public void doit() {
        mind = com.quui.chat.StaticResources.getMindInstance(TOPICS,topicFileName, WN, LOGS);
        this.hollalaAnswer = mind.processConversation(this.userInput);
    }
    
    public String getTopicFileName(){
        return topicFileName;
    }

    private String userInput = "";

    private String hollalaAnswer = "";

    public String getHollalaAnswer() {
        return hollalaAnswer;
    }

    public void setHollalaAnswer(String hollalaAnswer) {
        this.hollalaAnswer = hollalaAnswer;
    }

    public String getUserInput() {
        System.out.println("HOOOOOOOOOLLLLLLLLLLLLALLAAALALA getter");
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
        conversation.add("<li>" + "User: " + userInput);
        System.out.println("HOOOOOOOOOLLLLLLLLLLLLALLAAALALA setter");
        doit();
        conversation.add("<li>" + "Hollala: " + hollalaAnswer);
        try {
            mind.saveMap(topicFileName);
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

//    public void setTopicFileName(String topicFileName) {
//        this.topicFileName = topicFileName;
//    }
}
