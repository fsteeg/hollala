/** 
 Project "com.quui.chat.core" (C) 2004 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.quui.chat.io;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.IllegalDataException;
import com.quui.chat.mind.Topic;

/**
 * Mapping of the internal Objects to the XML-DOM
 * 
 * @author Fabian Steeg (fsteeg)
 */
public class MapToDOM {

    /**
     * Default Constructor
     */
    public MapToDOM() {
        super();
    }

    /**
     * @param map
     *            The mapping of words to topics
     * @param topics
     *            The actual topics
     * @return The DOM Document (JDOM)
     * @throws ParserConfigurationException
     */
    public Document createDOM(Map<String, Vector<Integer>> map,
            Vector<Topic> topics) throws ParserConfigurationException {
        Element mapNode = new Element("chat-map");
        Document doc = new Document(mapNode);
        for (int i = 0; i < topics.size(); i++) {
            Topic t = (Topic) topics.elementAt(i);
            Element topicNode = new Element("chat-topic");
            topicNode.setAttribute(new Attribute("name", t.getName()));
            mapNode.addContent(topicNode);
            // keys
            Iterator keys = t.getKeys().iterator();
            Element keysElem = new Element("chat-keys");
            topicNode.addContent(keysElem);
            while (keys.hasNext()) {
                String keyText = (String) keys.next();
                Element keyNode = new Element("chat-key");
                keysElem.addContent(keyNode);
                keyNode.setText(keyText);
            }
            // answers in a mapping with the answers as keys and their
            // frequencies as Integers as values
            Map answersMapping = t.getAnswers();
            Iterator answers = answersMapping.keySet().iterator();
            Element answersElem = new Element("chat-answers");
            topicNode.addContent(answersElem);
            while (answers.hasNext()) {
                String answerText = "";
                try {
                    answerText = (String) answers.next();
                    Element answerNode = new Element("chat-answer");
                    answerNode.setText(answerText);
                    answerNode.setAttribute("frequency",
                            ((Integer) answersMapping.get(answerText))
                                    .intValue()
                                    + "");
                    answersElem.addContent(answerNode);
                } catch (IllegalDataException e) {
                    e.printStackTrace();
                }
            }

        }
        return doc;
    }
}
