/** 
 Project "com.quui.chat.core" (C) 2004 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package com.quui.chat.io;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;

import com.quui.chat.mind.Topic;

/**
 * Handles the mapping of the DOM to the Map used internally
 * 
 * @author Fabian Steeg (fsteeg)
 */
public class DOMToMap {

    private Vector<Topic> theTopics;

    private HashMap<String, Vector<Integer>> result;

    /**
     * @param dom
     *            The source DOM
     * @throws Exception
     */
    public DOMToMap(Document dom) throws Exception {
        super();
        init(dom);
    }

    /**
     * @param dom
     *            The source DOM
     * @throws Exception
     */
    private void init(Document dom) throws Exception {
        if (dom == null) {
            throw new NullPointerException("Document was null.");
        }
        List topics = dom.getRootElement().getChildren("chat-topic");
        this.result = new HashMap<String, Vector<Integer>>();
        this.theTopics = new Vector<Topic>();
        for (int i = 0; i < topics.size(); i++) {
            Element topicElem = (Element) topics.get(i);
            String name = topicElem.getAttributeValue("name");
            List keys = topicElem.getChild("chat-keys").getChildren("chat-key");
            List answers = topicElem.getChild("chat-answers").getChildren(
                    "chat-answer");
            Map<String, Integer> a = new HashMap<String, Integer>();
            for (Iterator iter = answers.iterator(); iter.hasNext();) {
                Element element = (Element) iter.next();
                String answer = element.getText();
                String freq = element.getAttributeValue("frequency");
                if (freq != null && !freq.equals("")) {
                    a.put(answer, new Integer(Integer.parseInt(freq)));
                } else {
                    throw new Exception(
                            "topics-file contains faulty <chat-answer>-elements (missing frequency-attributes)");
                }
            }
            Vector<String> k = new Vector<String>();
            Topic topic = new Topic(name, k, a);
            for (Iterator iter = keys.iterator(); iter.hasNext();) {
                Element element = (Element) iter.next();
                String key = element.getText();
                k.add(key);

                if (!this.result.keySet().contains(key)) {
                    Vector<Integer> v = new Vector<Integer>();
                    v.add(new Integer(i));
                    this.result.put(key, v);
                } else {
                    Vector<Integer> topicIndicesForKey = this.result.get(key);
                    topicIndicesForKey.add(new Integer(i));
                    this.result.put(key, topicIndicesForKey);
                }
            }

            if (!this.theTopics.contains(topic)) {
                this.theTopics.add(topic);
            }

        }
    }

    /**
     * @return The mapping of words to topics
     */
    public HashMap<String, Vector<Integer>> getMap() {
        return this.result;
    }

    /**
     * @return The topics
     */
    public Vector<Topic> getTopics() {
        return this.theTopics;
    }
}