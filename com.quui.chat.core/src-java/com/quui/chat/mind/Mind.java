/** 
 Project "com.quui.chat.core" (C) 2004 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package com.quui.chat.mind;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.quui.chat.Log;
import com.quui.chat.Preprocessor;
import com.quui.chat.io.MapToDOM;

/**
 * Central class of the Chatbot Engine, contains the logic to determine an
 * answer for an input
 * 
 * @author Fabian Steeg (fsteeg)
 */
public class Mind {
    private Vector<String> stopwords;

    protected static Vector<Topic> topics;

    public Topic lastTopic;

    private Vector<String> answerDummies;

    protected static Map<String, Vector<Integer>> topicsIndices;

    public String lastAnswer = null;

    /**
     * Flag to indicate an input cause a very high-scored topic
     */
    public boolean interested = false;

    protected boolean isWordNetEnabled = true;

    private Preprocessor preprocessor = null;

    private Learning learning;

    /**
     * @param stopwords
     *            The stopwords
     * @param topics
     *            The topics
     * @param map
     *            The mapping of keys to topic-indices
     * @param answerDummies
     *            The dummy answers
     */
    public Mind(Vector<String> stopwords, Vector<Topic> topics,
            Map<String, Vector<Integer>> map, Vector<String> answerDummies)
            throws IOException {
        this.learning = new Learning(this);
        this.stopwords = stopwords;
        this.preprocessor = new Preprocessor(this.isWordNetEnabled,
                this.stopwords);
        Mind.topics = topics;
        Mind.topicsIndices = map;
        this.answerDummies = answerDummies;
    }

    /**
     * The main processing function.
     * 
     * @param originalMessage
     *            The user input.
     * @param learn
     *            If true input is learned
     * @return Returns an answer on the input.
     */
    public synchronized String processConversation(String originalMessage,
            boolean learn) {
        long start = System.currentTimeMillis();
        Vector<String> tokens = this.preprocessor.preProcess(originalMessage);
        Log.logger.info("[preprocessing took "
                + (System.currentTimeMillis() - start) / 1000 + "s]");
        start = System.currentTimeMillis();
        // look for the best topic on the user input, within the existing
        // topics, will try to use wordnet to determine the best topic:
        Topic chosenTopic = searchTopic(originalMessage, tokens);
        Log.logger.info("[searching topic took "
                + (System.currentTimeMillis() - start) / 1000 + "s]");
        start = System.currentTimeMillis();
        String answer = null;
        if (chosenTopic != null) {
            this.lastTopic = chosenTopic;
            answer = getAnswer(chosenTopic);
            Log.logger.info("[getting an answer took "
                    + (System.currentTimeMillis() - start) / 1000 + "s]");
            start = System.currentTimeMillis();
            // learn the user's sentence as a valid answer
            // if it isnt one already
            if (learn) {
                Log.logger.info(" learning: " + originalMessage);
                this.learning.learnAnswerForTopic(originalMessage, chosenTopic);
                Log.logger.debug("[learning answer took "
                        + (System.currentTimeMillis() - start) / 1000 + "s]");
                start = System.currentTimeMillis();
                this.learning.learnNewTopics(originalMessage, tokens, learn);
                Log.logger.info("[learning topics took "
                        + (System.currentTimeMillis() - start) / 1000 + "s]");
                start = System.currentTimeMillis();
            } else {
                Log.logger.info("Not learning: " + originalMessage);
            }

        }
        // no topic found:
        else {
            // answer dummy-answer plus newly learned:
            String newAnswer = this.learning.learnNewTopics(originalMessage,
                    tokens, learn);
            if (newAnswer != null) {
                answer = getAnswerDummy() + ", " + newAnswer;
                this.lastAnswer = answer;
            } else {
                answer = getAnswerDummy();
                this.lastAnswer = "";
                this.lastTopic = null;
            }

        }
        // and reply to user
        return answer;
    }

    /**
     * @param chosenTopic
     *            The topic to get the answer from
     * @return Returns a new original answer, different from the last one
     */
    private String getAnswer(Topic chosenTopic) {
        String answer;
        answer = chosenTopic.getAnswer();
        if (this.lastAnswer == null) {
            this.lastAnswer = answer;
        }
        // get an answer that isnt the one just given
        while (this.lastAnswer.equals(answer)) {
            answer = chosenTopic.getAnswer();
        }
        this.lastAnswer = answer;
        return answer;
    }

    /**
     * Retreives the Topic with the highest score and checks if that makes him
     * interested
     * 
     * @param originalMessage
     *            The original message
     * @param tokens
     *            The preprocessed input words
     * @return The topic for the input
     */
    private Topic searchTopic(String originalMessage, Vector<String> tokens) {
        Topic chosenTopic = getBestTopic(tokens,
                originalMessage.split(" ").length);
        if (chosenTopic != null) {
            Log.logger.debug("Topic Score: " + chosenTopic.getScore());
            // if more than half of the user input can be mapped to one topic,
            // but not all, then answer in any case:
            if (chosenTopic.getScore() >= 0.5 && chosenTopic.getScore() < 1.0) {
                this.interested = true;
            } else {
                this.interested = false;
            }
        } else {
            this.interested = false;
        }
        return chosenTopic;
    }

    /**
     * Searches the best Topic for the user input. First tries to find a
     * suitable topic in the internnal map of topics. If nothing is found, try
     * to find a semantical connected topic via WordNet lookup.
     * 
     * @param tokens
     *            The preprocessed user input.
     * @return Returns the best topic for the user input (hopefully).
     */
    private Topic getBestTopic(Vector<String> tokens, int rawWordCount) {
        return lookupInternal(tokens, rawWordCount);
    }

    /**
     * Look for topics via their keys. If a key matches exactly the topic is
     * returned. Else the topic with highest score is returned.
     * 
     * @param tokens
     *            The preprocessed user input to find a suitable topic for.
     * @param rawInputWordCount
     *            The number of words in the original input
     * @return Return the best Topic found.
     */
    private Topic lookupInternal(Vector<String> tokens, int rawInputWordCount) {
        Map<String, Topic> keysAndTopics = new TreeMap<String, Topic>();
        // for every word
        long start = System.currentTimeMillis();
        for (String recentToken : tokens) {
            Vector<Topic> candidateTopics = new Vector<Topic>();
            Vector<Integer> candidateTopicsIndices = Mind.topicsIndices.get(recentToken);
            start = System.currentTimeMillis();
            if (candidateTopicsIndices != null)
                for (Integer integer : candidateTopicsIndices) {
                    // Log.logger.info("key " + recentToken);
                    Topic rec = Mind.topics.elementAt(integer);
                    double score = 0;
                    int inputHits = Scoring.checkSimple(tokens, rec,
                            this.isWordNetEnabled);
                    score = inputHits / (double) rawInputWordCount;
                    if (rec.getName().equals(recentToken)
                            || rec.containsKey(recentToken)) {
                        // Log.logger
                        // .debug("[found a topic containing a key, topic is '"
                        // + rec.getName()
                        // + "', key is '"
                        // + recentToken + "']");
                        // exact match
                        addToFinalCandidates(keysAndTopics, candidateTopics,
                                rec, score);
                    }

                    // compare synonymes of every word in the input with each
                    // topic:
                    inputHits = Scoring.checkSimple(tokens, rec,
                            this.isWordNetEnabled);
                    score = inputHits / (double) rawInputWordCount;
                    if (score > 0) {
                        // Log.logger
                        // .debug("[found a topic with score, topic is '"
                        // + rec.getName() + "', score is '"
                        // + score + "']");
                        // approxiamte match, remember
                        addToFinalCandidates(keysAndTopics, candidateTopics,
                                rec, score);
                    }

                    candidateTopics.add(rec);
                }
            if (candidateTopics.contains(this.lastTopic)) {
                Log.logger.info("Sticking to topic: "
                        + this.lastTopic.getName());
                return this.lastTopic;
            }
            // check every topic
            // for (Topic rec : Mind.topics) {
            // double score = 0;
            // if (rec.getName().equals(recentToken)
            // || rec.containsKey(recentToken)) {
            // Log.logger
            // .debug("[found a topic containing a key, topic is '"
            // + rec.getName()
            // + "', key is '"
            // + recentToken + "']");
            // // exact match, done
            // return rec;
            // }
            //
            // // compare synonymes of every word in the input with each
            // // topic:
            // int inputHits = Scoring.checkSimple(tokens, rec,
            // this.isWordNetEnabled);
            // score = inputHits / (double) rawInputWordCount;
            // if (score > 0) {
            // Log.logger.debug("[found a topic with score, topic is '"
            // + rec.getName() + "', score is '" + score + "']");
            // // approxiamte match, remember
            // addToFinalCandidates(keysAndTopics, candidateTopics, rec,
            // score);
            // }
            // }
            // every noun-like word in input that has a topic with score > 0
            // is added to mapping:
            Topic result = Scoring.getTopicWithMaxScore(candidateTopics);
            if (result != null) {
                String message = "[best topic for key '" + recentToken
                        + "' seems to be '" + result.getName()
                        + "' with score " + result.getScore() + ".]";
                Log.logger.debug(message);
                keysAndTopics.put(recentToken, result);
            }
            Log.logger.info("[lookup for: " + recentToken + " took "
                    + (System.currentTimeMillis() - start) / 1000 + "s]");
            start = System.currentTimeMillis();
        }
        Log.logger.info("[internal lookup took "
                + (System.currentTimeMillis() - start) / 1000 + "s]");
        start = System.currentTimeMillis();
        // retrieve the one with highest rating:
        Topic result = Scoring.getTopicWithMaxScore(keysAndTopics.values());
        Log.logger.info("[getting best topic took "
                + (System.currentTimeMillis() - start) / 1000 + "s]");
        start = System.currentTimeMillis();
        if (result != null) {
            String message = "[best topic seems to be '" + result.getName()
                    + "' with score " + result.getScore() + ".]";
            Log.logger.debug(message);
            return (result.getScore() > 0) ? result : null;
        }

        return null;
    }

    /**
     * @param keysAndTopics
     *            The mapping of strings to topics
     * @param candidateTopics
     *            The topics
     * @param rec
     *            The topic to add
     * @param score
     *            The score
     */
    private static void addToFinalCandidates(Map<String, Topic> keysAndTopics,
            Vector<Topic> candidateTopics, Topic rec, double score) {
        if (keysAndTopics.containsValue(rec)) {
            for (Object element : keysAndTopics.values()) {
                Topic topic = (Topic) element;
                if (topic == rec && rec.getScore() < score) {
                    rec.setScore(score);
                    candidateTopics.add(rec);
                }
            }
        } else {
            rec.setScore(score);
            candidateTopics.add(rec);
        }
    }

    /**
     * @return Returns the name of the last, ie. recent topic.
     */
    public String getLastTopicName() {
        if (this.lastTopic != null) {
            return this.lastTopic.getName();
        } else {
            return "[no topic]";
        }
    }

    /**
     * @param inputWord
     *            The input word
     * @return The Topic[] containing the topics found
     */
    public static Topic[] findTopics(String inputWord) {
        Vector topics = (Vector) topicsIndices.get(inputWord);
        if (topics == null) {
            return null;
        }
        Topic[] res = new Topic[topics.size()];
        for (int i = 0; i < res.length; i++) {
            Integer topicPos = (Integer) topics.elementAt(i);
            Topic result = null;
            if (topicPos != null) {
                result = (Topic) topics.elementAt(topicPos.intValue());
            }
            res[i] = result;
        }
        return res;
    }

    /**
     * @return A dummy answer like "hehe" " yeah" "uh" etc.
     */
    private String getAnswerDummy() {
        Random randomGen = new Random();
        int numberOfAnswers = this.answerDummies.size();
        if (numberOfAnswers > 0) {
            String result = this.answerDummies.elementAt(randomGen
                    .nextInt(numberOfAnswers));
            while (result.equals(this.lastAnswer)) {
                result = this.answerDummies.elementAt(randomGen
                        .nextInt(numberOfAnswers));
            }
            return result;
        } else {
            return null;
        }
    }

    /**
     * @return The mapping of keys to topic-indices
     */
    public Map<String, Vector<Integer>> getMap() {
        return Mind.topicsIndices;
    }

    /**
     * @return The topics
     */
    public Vector<Topic> getTopics() {
        return Mind.topics;
    }

    /**
     * Saves the internal map to disk.
     * 
     * @throws ParserConfigurationException
     *             If saving fails.
     */
    public synchronized void saveMap(String topicFileName)
            throws ParserConfigurationException {
        try {
            MapToDOM d = new MapToDOM();
            Document dom = d.createDOM(topicsIndices, topics);
            String loc = topicFileName;
            FileWriter out = new FileWriter(loc);
            XMLOutputter outp = new XMLOutputter();
            try {
                outp.setFormat(Format.getPrettyFormat());
                outp.output(dom, out);
            } catch (Exception x) {
                x.printStackTrace();
            }
            Log.logger.debug("[saved topics]: " + loc);
        } catch (ParserConfigurationException x) {
            x.printStackTrace();
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

}
