/** 
 Project "com.quui.chat.core" (C) 2006 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.quui.chat.mind;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;

import com.quui.chat.Log;
import com.quui.chat.mind.wn.WNLookup;

public class Scoring {
    /**
     * @param v
     *            The synsets
     * @param t
     *            The Topic
     * @return The number of words in the synset also found in the topic
     */
    public static int checkAmbig(Vector<Synset> v, Topic t) {
        int count = 0;
        boolean result = false;
        if (v == null)
            return count;
        for (Synset set : v) {
            Word[] w = set.getWords();
            for (Word word : w) {
                String rec = word.getLemma();
                String toLookup = WNLookup.getStaticStem(rec);
                result = t.containsKey(toLookup);
                if (result || t.containsKey(rec)) {
                    count++;
                    Log.logger.debug("[found a related word on my mind: '"
                            + toLookup + "', recent count for this topic '"
                            + t.getName() + "' is: " + count + "]");
                }
            }
        }
        return count;
    }

    /**
     * Check how good a given Vector of words matches a given topic by counting
     * how many of the words in the vector are keywords for the given topic.
     * 
     * @param v
     *            The Vector of words to check for applicability to the topic
     * @param t
     *            The topic to check for applicability on the words
     * @param isWordNetEnabled
     *            If true words are stemmed
     * @return Returns a score, the number of words in the Vector that are
     *         keywords for the topic
     * @throws JWNLException
     *             If sometinhg goes wrong during the WordNet-lookup
     */
    public static int checkSimple(Vector<String> v, Topic t,
            boolean isWordNetEnabled) {
        int count = 0;
        boolean result = false;
        if (v == null)
            return count;
        for (String rec : v) {
            String toLookup = rec;
            if (isWordNetEnabled) {
                toLookup = WNLookup.getStaticStem(rec);
            }
            result = t.containsKey(toLookup);
            if (result || t.containsKey(rec)) {
                count++;
                Log.logger.debug("[found a related word on my mind: '"
                        + toLookup + "', recent count for this topic '"
                        + t.getName() + "' is: " + count + "]");
            }

        }
        return count;
    }

    /**
     * Returns the topic with highest score from the collection of topics.
     * 
     * @param candidateTopics
     *            The collection of topics to check.
     * @return Returns the topic with highest score from the collection of
     *         topics.
     */
    public static Topic getTopicWithMaxScore(Collection candidateTopics) {
        double maxScore = 0;
        Topic bestTopic = null;
        for (Iterator iter = candidateTopics.iterator(); iter.hasNext();) {
            Topic recentTopic = (Topic) iter.next();
            double recentScore = recentTopic.getScore();
            if (recentScore >= maxScore) {
                maxScore = recentScore;
                bestTopic = recentTopic;
            }
        }
        return bestTopic;
    }
}
