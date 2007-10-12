/** 
 Project "com.quui.chat.core" (C) 2006 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.quui.chat.mind;

import java.util.Iterator;
import java.util.Vector;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import com.quui.chat.Log;
import com.quui.chat.mind.wn.SemanticRelations;
import com.quui.chat.mind.wn.WNLookup;

/**
 * Learning functionality
 * 
 * @author Fabian Steeg (fsteeg)
 */
public class Learning {
    private Mind mind;

    /**
     * @param mind
     *            The mind to alter
     */
    public Learning(Mind mind) {
        this.mind = mind;
    }

    /**
     * @param s
     *            The user input to add to a topic.
     * @param chosenTopic
     *            The topic to add the user input to.
     */
    public void learnAnswerForTopic(String s, Topic chosenTopic) {
        chosenTopic.addAnswer(s);
    }

    /**
     * Tries to create a new Topic for the user input
     * 
     * @param rawUserInput
     *            The raw user input.
     * @param tokens
     *            The preprocessed user input.
     * @param learn
     * @throws JWNLException
     *             On problem with WordNet lookup.
     * @return An answer for the last newly learned topic
     */
    protected String learnNewTopics(String rawUserInput, Vector<String> tokens,
            boolean learn) {
        // ...then learn from input...
        Topic topic = null;
        String result = null;
        // ...for every lexical word in the input (that can be a noun)...
        for (String name : tokens) {
            if (name.trim().equals(""))
                throw new NullPointerException("Empty token!");
//            if (
            // TODO
//            mind.isWordNetEnabled && !WNLookup.isContentWord(name)
//                    || Mind.map.keySet().contains(name))
//                continue;
            // use wordnet for learning
            if (mind.isWordNetEnabled) {
                result = learnFromWordNet(name, rawUserInput);
            }
            // use only user input for learning
            else {
                topic = new Topic(name);
                if (learn) {
                    topic.addAnswer(rawUserInput);
                }
                for (String tok : tokens) {
                    topic.addKey(tok);
                }
                // get a answer different from the last given
                while (mind.lastAnswer.equals(result))
                    result = topic.getAnswer();
                Mind.topics.add(topic);
                mind.lastAnswer = result;
                mind.lastTopic = topic;
            }
        }
        return result;
    }

    /**
     * @param word
     *            The word to learn
     * @param rawUserInput
     *            The entire user input
     * @return An answer for the newly learned topic
     */
    private String learnFromWordNet(String word, String rawUserInput) {
        Vector<Synset> synSets = new Vector<Synset>();
        synSets = SemanticRelations.getSynsets(word);
        // learn a topic for ever syn
        for (Synset synset : synSets) {
            Topic topic = createNewTopic(rawUserInput, synset);
            addToMap(topic);
            Mind.topics.add(topic);
            if (topic != null) {
                return getFreshAnswer(topic);
            }
        }
        // a word thats not in wn:
        if (synSets.size() == 0) {
            Topic topic = createNewTopic(rawUserInput);
            addToMap(topic);
            Mind.topics.add(topic);
            if (topic != null) {
                return getFreshAnswer(topic);
            }
        }
        return null;

    }

    /**
     * @param rawUserInput
     *            The raw user input
     * @param synset
     *            The synset
     * @return The newly created topic for the synset
     */
    private Topic createNewTopic(String rawUserInput, Synset synset) {
        // ...open a new topic...
        Topic topic = new Topic(synset.getGloss());
        Word[] words = synset.getWords();
        String key = "";
        for (int i = 0; i < words.length; i++) {
            key = words[i].getLemma();
            // add all synonymes as keys
            if (!topic.containsKey(key)) {
                topic.addKey(key);
            }
//            if (key.indexOf("(") == -1 && key.indexOf("_") != -1) {
//                /*
//                 * learn the clean composed key as answer too, to trigger
//                 * conversation:
//                 */
//                key = key.replace('_', ' ');
//                topic.addAnswer(key);
//            }
        }
        // learn related words as keys
        learnRelatedWordsAsKeys(topic, key);
        // for every syn also learn cleaned definitions and uses
//        learnCleanedDefsAsAnswers(topic, synset);
        // a new topic should have at least two answers to no get
        // too obviously stuck
        if (topic.getAnswers().size() < 2) {
            if (key.endsWith(")")) {
                key = key.substring(0, key.indexOf("("));
            }
            topic.addAnswer(key);
        }
        // ...and add the user input as an answer...
        learnAnswerForTopic(rawUserInput, topic);
        return topic;
    }

    private Topic createNewTopic(String rawUserInput) {
        // ...open a new topic...
        Topic topic = new Topic(rawUserInput);
        String[] words = rawUserInput.split(" ");
        String key = "";
        for (int i = 0; i < words.length; i++) {
            key = words[i];
            if (!topic.containsKey(key)) {
                topic.addKey(key);
            }

//            topic.addAnswer(key);
        }
        // learn related words as keys
        learnRelatedWordsAsKeys(topic, key);
        // a new topic should have at least two answers to no get
        // too obviously stuck
        if (topic.getAnswers().size() < 2) {
            topic.addAnswer(key);
        }
        // ...and add the user input as an answer...
        learnAnswerForTopic(rawUserInput, topic);
        return topic;
    }

    /**
     * @param topic
     *            The topic to get an answer from
     * @return A fresh answer, meaning different from the last given
     */
    private String getFreshAnswer(Topic topic) {
        mind.lastTopic = topic;
        mind.lastTopic.setScore(-1);
        String answer = topic.getAnswer();
        if (mind.lastAnswer == null)
            mind.lastAnswer = answer;
        while (mind.lastAnswer.equals(answer))
            answer = topic.getAnswer();
        mind.lastAnswer = answer;
        return answer;
    }

    /**
     * @param topic
     *            The new topic to add to the currently active map
     */
    private void addToMap(Topic topic) {
        for (Iterator iterator = topic.getKeys().iterator(); iterator.hasNext();) {
            String k = (String) iterator.next();
            if (!Mind.map.keySet().contains(k)) {
                Vector<Integer> v = new Vector<Integer>();
                v.add(new Integer(Mind.topics.size()));
                Mind.map.put(k, v);
            } else {
                Vector<Integer> topicIndicesForKey = Mind.map.get(k);
                topicIndicesForKey.add(new Integer(Mind.topics.size()));
                Mind.map.put(k, topicIndicesForKey);
            }
        }
    }

    /**
     * @param topic
     *            The topic to learn answers for
     * @param synset
     *            The synsets whose defs should be learnend
     */
//    private void learnCleanedDefsAsAnswers(Topic topic, Synset synset) {
//        String[] defs = WNLookup.getDefsAsAnswers(synset);
//        for (int i = 0; i < defs.length; i++) {
//            Log.logger.debug("Learning answer: " + defs[i]);
//            topic.addAnswer(defs[i]);
//        }
//    }

    /**
     * Adds max 10 synonymes, hyperonymes (parents) and hyponyms (children) of
     * key as additional keys
     * 
     * @param topic
     *            The topic to learn keys for
     * @param key
     *            The basic key
     */
    private void learnRelatedWordsAsKeys(Topic topic, String key) {
        if (mind.isWordNetEnabled) {
            // learnParentsAsKeys(topic, key);
            learnChildrenAsKeys(topic, key);
        }
    }

    /**
     * Adds max 10 hyponyms (children) of key as additional keys
     * 
     * @param topic
     *            The topic to learn keys for
     * @param key
     *            The basic key
     */
    private void learnChildrenAsKeys(Topic topic, String key) {
        Vector<String> children = SemanticRelations.getChildren(key);
        // if (children.size() > 0)
        // Log.logger.debug("\t [children] " + children.toString());
        if (children.size() < 10) {
            for (int i = 0; i < children.size() && i < 10; i++) {
                String c = children.get(i);
//                if (c.contains("_"))
//                    topic.addAnswer(c.replaceAll("_", " "));
//                if (c.contains("-"))
//                    topic.addAnswer(c.replaceAll("-", " "));
                topic.addKey(c);
            }
        }
    }

    /**
     * Adds max 10 hyperonyms (parents) of key as additional keys
     * 
     * @param topic
     *            The topic to learn keys for
     * @param key
     *            The basic key
     */
    // private void learnParentsAsKeys(Topic topic, String key) {
    // Vector<String> parents = SemanticRelations.getParents(key);
    // // if (parents.size() > 0)
    // // Log.logger.debug("\t [parents] " + parents.toString());
    // for (int i = 0; i < parents.size() && i < 10; i++) {
    // String p = parents.get(i);
    // if (p.contains("_"))
    // topic.addAnswer(p.replaceAll("_", " "));
    // if (p.contains("-"))
    // topic.addAnswer(p.replaceAll("-", " "));
    // topic.addKey(p);
    // }
    // }
}
