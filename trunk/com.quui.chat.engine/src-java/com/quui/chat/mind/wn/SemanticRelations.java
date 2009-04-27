/** 
 Project "com.quui.chat.core" (C) 2006 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.quui.chat.mind.wn;

import java.util.Iterator;
import java.util.Vector;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.IndexWordSet;
import net.didion.jwnl.data.PointerUtils;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.data.list.PointerTargetNode;
import net.didion.jwnl.data.list.PointerTargetNodeList;
import com.quui.chat.Log;
import com.quui.chat.mind.Mind;
import com.quui.chat.mind.Scoring;
import com.quui.chat.mind.Topic;

/**
 * Handles semantic relations like synonymes, hyperonyms and hyponyms
 * 
 * @author Fabian Steeg (fsteeg)
 */
public class SemanticRelations {

    /**
     * @param inputWord
     *            The word to check
     * @param maxScore
     *            The current max score
     * @param isWordNetEnabled
     *            if true stemming is used
     * @param rawWordCount
     *            The number of words in the original input
     * @param mind 
     * @return A topic found for a parent with a score higher than max score
     */
    public static Topic checkParents(String inputWord, double maxScore,
            boolean isWordNetEnabled, double rawWordCount, Mind mind) {
        Vector<String> parents = getParents(inputWord);
        Topic[] topicsArray = null;
        for (Iterator iterator = parents.iterator(); iterator.hasNext();) {
            String parent = (String) iterator.next();
            topicsArray = mind.findTopics(parent);
            if (topicsArray != null) {
                for (int i = 0; i < topicsArray.length; i++) {
                    Topic result = topicsArray[i];
                    if (result != null) {
                        double d = (double) Scoring.checkSimple(parents,
                                result, isWordNetEnabled)
                                / (double) rawWordCount;
                        result.setScore(d);
                        if (result.getScore() > maxScore) {
                            maxScore = result.getScore();
                            Log.logger
                                    .debug("[found new best topic in parents: "
                                            + result.getName()
                                            + " with score: " + maxScore);
                            return result;

                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param inputWord
     *            The word to check
     * @param maxScore
     *            The current max score
     * @param rawWordCount
     *            The number of words in the origninal input, for computing the
     *            score
     * @param mind 
     * @return A topic for a synonym with a higher score than max score
     */
    public static Topic checkSyns(String inputWord, double maxScore,
            double rawWordCount, Mind mind) {
        Vector<Synset> syns = getSynsets(inputWord);
        for (Synset set : syns) {
            Word[] w = set.getWords();
            for (int j = 0; j < w.length; j++) {
                String syn = w[j].getLemma();
                Topic[] topicsArray = mind.findTopics(syn);
                if (topicsArray != null) {
                    for (int i = 0; i < topicsArray.length; i++) {
                        Topic result = topicsArray[i];
                        if (result != null) {
                            // TODO aha hier ambig... das nochmal durchdenken...
                            double d = (double) Scoring
                                    .checkAmbig(syns, result)
                                    / (double) rawWordCount;
                            result.setScore(d);
                            if (result.getScore() > maxScore) {
                                Log.logger
                                        .debug("[found new best topic in synonymes: "
                                                + result.getName()
                                                + " with score: " + maxScore);

                                return result;

                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param inputWord
     *            The base word
     * @param maxScore
     *            The current max score
     * @param isWordNetEnabled
     *            If true words are stemmed
     * @param rawWordCount
     *            The number of words in the original input
     * @param mind 
     * @return A topic for a child with a higher score than max score
     */
    public static Topic checkChildren(String inputWord, double maxScore,
            boolean isWordNetEnabled, int rawWordCount, Mind mind) {
        Vector<String> children = getChildren(inputWord);
        Topic[] topicsArray = null;
        for (Iterator iterator = children.iterator(); iterator.hasNext();) {
            String child = (String) iterator.next();
            topicsArray = mind.findTopics(child);
            if (topicsArray != null) {
                for (int i = 0; i < topicsArray.length; i++) {
                    Topic result = topicsArray[i];
                    if (result != null) {
                        double d = (double) Scoring.checkSimple(children,
                                result, isWordNetEnabled)
                                / (double) rawWordCount;
                        result.setScore(d);
                        if (result.getScore() > maxScore) {
                            maxScore = result.getScore();
                            Log.logger
                                    .debug("[found new best topic in children: "
                                            + result.getName()
                                            + " with score: " + maxScore);

                            return result;

                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param s
     *            The base words
     * @return The hyponyms (children) of the word
     */
    public static Vector<String> getChildren(String s) {
        if (s == null)
            return null;
        Vector<String> result = new Vector<String>();
        IndexWordSet words;
        try {
            words = WNLookup.getDict().lookupAllIndexWords(s);
            Iterator iter = words.getIndexWordCollection().iterator();
            IndexWord word;
            // TODO mit mehr sem. relationen arbeiten... nett ev kohyponyme aber
            // auch partonyme, oder? das ufert natuerlich auch aus, hat ja nicht
            // alles was miteinander zu tun...
            while (iter.hasNext()) {
                word = (IndexWord) iter.next();
                if (word != null) {
                    // result.add(getWordLabel(word));
                    // for all senses...
                    for (int c = 0; c < word.getSenses().length; c++) {
                        // all hypernyms for the synset:
                        PointerTargetNodeList hyponyms = PointerUtils
                                .getInstance().getDirectHyponyms(
                                        word.getSense(c + 1));
                        result.addAll(getLemmata(hyponyms));
                    }
                }

            }
        } catch (JWNLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * @param s
     *            The base word
     * @return The synonymes for word
     */
    public static Vector<Synset> getSynsets(String s) {
        if (s == null)
            throw new NullPointerException("Word to lookup Syns for is null.");
        Vector<Synset> result = new Vector<Synset>();
        IndexWordSet words;
        try {
            words = WNLookup.getDict().lookupAllIndexWords(s);
            Iterator iter = words.getIndexWordCollection().iterator();
            IndexWord word;
            while (iter.hasNext()) {
                word = (IndexWord) iter.next();
                if (word != null) {
                    // for all senses...
                    for (int c = 0; c < word.getSenses().length; c++) {
                        result.add(word.getSense(c + 1));
                    }
                }

            }
        } catch (JWNLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Vector<String> getSynonymes(String s) {
        if (s == null)
            throw new NullPointerException("Word to lookup Syns for is null.");
        Vector<String> result = new Vector<String>();
        IndexWordSet words;
        try {
            words = WNLookup.getDict().lookupAllIndexWords(s);
            Iterator iter = words.getIndexWordCollection().iterator();
            IndexWord word;
            while (iter.hasNext()) {
                word = (IndexWord) iter.next();

                String lemma = word.getLemma();
                if (!lemma.toLowerCase().equals(s.toLowerCase()))
                    result.add(lemma);
            }
        } catch (JWNLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * @param baseWord
     *            The word to get the parents for
     * @return Retruns the hyperonyms (parents) for the word
     */
    public static Vector<String> getParents(String baseWord) {
        if (baseWord == null)
            return null;
        Vector<String> result = new Vector<String>();
        IndexWordSet words;
        try {
            words = WNLookup.getDict().lookupAllIndexWords(baseWord);
            Iterator iter = words.getIndexWordCollection().iterator();
            IndexWord word;
            while (iter.hasNext()) {
                word = (IndexWord) iter.next();
                if (word != null) {
                    // result.add(getWordLabel(word));
                    // for all senses...
                    for (int c = 0; c < word.getSenses().length; c++) {
                        // all hypernyms for the synset:
                        PointerTargetNodeList hypernyms = PointerUtils
                                .getInstance().getDirectHypernyms(
                                        word.getSense(c + 1));
                        result.addAll(getLemmata(hypernyms));
                    }
                }

            }
        } catch (JWNLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Vector<String> getAntonymes(String baseWord) {
        if (baseWord == null)
            return null;
        Vector<String> result = new Vector<String>();
        IndexWordSet words;
        try {
            words = WNLookup.getDict().lookupAllIndexWords(baseWord);
            Iterator iter = words.getIndexWordCollection().iterator();
            IndexWord word;
            while (iter.hasNext()) {
                word = (IndexWord) iter.next();
                if (word != null) {
                    // result.add(getWordLabel(word));
                    // for all senses...
                    for (int c = 0; c < word.getSenses().length; c++) {
                        // all hypernyms for the synset:
                        PointerTargetNodeList hypernyms = PointerUtils
                                .getInstance()
                                .getAntonyms(word.getSense(c + 1));
                        result.addAll(getLemmata(hypernyms));
                    }
                }

            }
        } catch (JWNLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Vector<String> getSiblings(String baseWord) {
        if (baseWord == null)
            return null;
        Vector<String> result = new Vector<String>();
        IndexWordSet words;
        try {
            words = WNLookup.getDict().lookupAllIndexWords(baseWord);
            Iterator iter = words.getIndexWordCollection().iterator();
            IndexWord word;
            while (iter.hasNext()) {
                word = (IndexWord) iter.next();
                if (word != null) {
                    // result.add(getWordLabel(word));
                    // for all senses...
                    for (int c = 0; c < word.getSenses().length; c++) {
                        // all hypernyms for the synset:
                        PointerTargetNodeList hypernyms = PointerUtils
                                .getInstance().getCoordinateTerms(
                                        word.getSense(c + 1));
                        result.addAll(getLemmata(hypernyms));
                    }
                }

            }
        } catch (JWNLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Vector<String> getMeronyms(String baseWord) {
        if (baseWord == null)
            return null;
        Vector<String> result = new Vector<String>();
        IndexWordSet words;
        try {
            words = WNLookup.getDict().lookupAllIndexWords(baseWord);
            Iterator iter = words.getIndexWordCollection().iterator();
            IndexWord word;
            while (iter.hasNext()) {
                word = (IndexWord) iter.next();
                if (word != null) {
                    // result.add(getWordLabel(word));
                    // for all senses...
                    for (int c = 0; c < word.getSenses().length; c++) {
                        // all hypernyms for the synset:
                        PointerTargetNodeList hypernyms = PointerUtils
                                .getInstance()
                                .getMeronyms(word.getSense(c + 1));
                        result.addAll(getLemmata(hypernyms));
                    }
                }

            }
        } catch (JWNLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Vector<String> getHolonyms(String baseWord) {
        if (baseWord == null)
            return null;
        Vector<String> result = new Vector<String>();
        IndexWordSet words;
        try {
            words = WNLookup.getDict().lookupAllIndexWords(baseWord);
            Iterator iter = words.getIndexWordCollection().iterator();
            IndexWord word;
            while (iter.hasNext()) {
                word = (IndexWord) iter.next();
                if (word != null) {
                    // result.add(getWordLabel(word));
                    // for all senses...
                    for (int c = 0; c < word.getSenses().length; c++) {
                        // all hypernyms for the synset:
                        PointerTargetNodeList hypernyms = PointerUtils
                                .getInstance()
                                .getHolonyms(word.getSense(c + 1));
                        result.addAll(getLemmata(hypernyms));
                    }
                }

            }
        } catch (JWNLException e) {
            e.printStackTrace();
        }

        return result;
    }

    // private static String getWordLabel(IndexWord word) {
    // String string = "# " + word.getLemma() + ", "
    // + word.getPOS().getLabel() + ": ";
    // if (word.getSenseCount() == 0)
    // string += "[ none ]";
    // return string;
    //    }

    private static Vector<String> getLemmata(PointerTargetNodeList hypernyms) {
        Vector<String> result = new Vector<String>();
//        if (hypernyms.size() == 0)
//            result.add("[ none ]");
        Iterator i = hypernyms.iterator();
        // ...add all hypernyms
        while (i.hasNext()) {
            Synset ss = ((PointerTargetNode) i.next()).getSynset();
            for (int j = 0; j < ss.getWords().length; j++) {
                Word w = ss.getWord(j);
                if (w != null) {
                    String lemma = w.getLemma();
                    // System.out.println("lemma: " + lemma + " Ende");
                    result.add(lemma);
                }
            }
        }
        return result;
    }

    public static String getHyperString(String word) {
        String result = "";
        Vector<String> parents = SemanticRelations.getParents(word);
//        if (parents.size() == 0)
//            result += "[ none ]";
        for (String s : parents) {
            result = result + s + " ";
        }
        return result.toLowerCase().replaceAll(word.toLowerCase(),
                word.charAt(0) + ".");
    }

    public static String getHypoString(String word) {
        String result = "";
        Vector<String> children = SemanticRelations.getChildren(word);
//        if (children.size() == 0)
//            result += "[ none ]";
        for (String s : children) {
            result = result + s + " ";
        }
        return result.toLowerCase().replaceAll(word.toLowerCase(),
                word.charAt(0) + ".");
    }

    public static String getAntoString(String word) {
        String result = "";
        Vector<String> children = SemanticRelations.getAntonymes(word);
//        if (children.size() == 0)
//            result += "[ none ]";
        for (String s : children) {
            result = result + s + " ";
        }
        return result.toLowerCase().replaceAll(word.toLowerCase(),
                word.charAt(0) + ".");
    }

    public static String getSiblingsString(String word) {
        String result = "";
        Vector<String> children = SemanticRelations.getSiblings(word);
//        if (children.size() == 0)
//            result += "[ none ]";
        for (String s : children) {
            result = result + s + " ";
        }
        return result.toLowerCase().replaceAll(word.toLowerCase(),
                word.charAt(0) + ".");
    }

    public static String getMeroString(String word) {
        String result = "";
        Vector<String> children = SemanticRelations.getMeronyms(word);
//        if (children.size() == 0)
//            result += "[ none ]";
        for (String s : children) {
            result = result + s + " ";
        }
        return result.toLowerCase().replaceAll(word.toLowerCase(),
                word.charAt(0) + ".");
    }

    public static String getHoloString(String word) {
        String result = "";
        Vector<String> children = SemanticRelations.getHolonyms(word);
//        if (children.size() == 0)
//            result += "[ none ]";
        for (String s : children) {
            result = result + s + " ";
        }
        return result.toLowerCase().replaceAll(word.toLowerCase(),
                word.charAt(0) + ".");
    }

    public static String getSynoString(String word) {
        String result = "";
        Vector<String> synonyms = SemanticRelations.getSynonymes(word);
//        if (synonyms.size() == 0)
//            result += "[ none ]";
        for (String s : synonyms) {
            result = result + s + " ";
        }
        return result.toLowerCase().replaceAll(word.toLowerCase(),
                word.charAt(0) + ".");
    }
}
