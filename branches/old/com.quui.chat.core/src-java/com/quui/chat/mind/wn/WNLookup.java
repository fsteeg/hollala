/** 
 Project "com.quui.chat.core" (C) 2004 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package com.quui.chat.mind.wn;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Vector;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.IndexWordSet;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.dictionary.Dictionary;

/**
 * Convenience access methods to the WordNet Dictionary
 * 
 * @author Fabian Steeg (fsteeg)
 */
public class WNLookup {
    private static Dictionary dict;

    /**
     * use init
     */
    public WNLookup() {
        // init(loc);
    }

    /**
     * @param loc
     *            The Location of the WN-File ti init JWNL with
     * @throws FileNotFoundException
     */
    public void init(String loc) throws FileNotFoundException {
        try {
            JWNL.initialize(new FileInputStream(loc));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        dict = Dictionary.getInstance();
    }

    /**
     * @param originalWord
     *            The word to stem
     * @return The stemmed word
     */
    public static String getStaticStem(String originalWord) {
        if (originalWord == null)
            return null;
        String result = new String();
        IndexWordSet words;
        try {
            words = dict.lookupAllIndexWords(originalWord);
            Iterator iter = words.getIndexWordCollection().iterator();
            IndexWord word;
            while (iter.hasNext()) {
                word = (IndexWord) iter.next();
                if (word != null) {
                    result = word.getLemma();
                    return result;
                }
            }
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        return "[ nothing found ]";
    }

    public String getStem(String word) {
        return WNLookup.getStaticStem(word);
    }

    /**
     * @param originalWord
     *            The word to check
     * @return Retruns true if the word is a content word (noun, verb, adjective
     *         or adverb)
     */
    public static boolean isContentWord(String originalWord) {
        if (originalWord == null)
            return false;
        IndexWordSet words;
        try {
            words = dict.lookupAllIndexWords(originalWord);
            Iterator iter = words.getIndexWordCollection().iterator();
            IndexWord word;
            while (iter.hasNext()) {
                word = (IndexWord) iter.next();
                if (word != null) {
                    POS r = word.getPOS();
                    if (r.equals(POS.NOUN) || r.equals(POS.VERB)
                            || r.equals(POS.ADJECTIVE) || r.equals(POS.ADVERB))
                        return true;
                }
            }
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param ss
     *            The synset
     * @return The elements of the definitions of synset as cleaned strings
     *         which can be used as answers
     */
    public static String[] getDefsAsAnswers(Synset ss) {
        String[] answers = ss.getGloss().split(";");
        for (int i = 0; i < answers.length; i++) {
            String def = answers[i].replaceAll("\"", "").trim();

            if (def.indexOf('(') == -1 && def.indexOf(')') == -1) {
                answers[i] = def;
                continue;
            }
            StringBuffer r = new StringBuffer();
            char[] c = def.toCharArray();
            boolean inParens = false;
            for (int j = 0; j < c.length; j++) {
                if (c[j] == '(')
                    inParens = true;
                if (!inParens)
                    r.append(c[j]);
                if (c[j] == ')') {
                    // skip blank after closing paren:
                    j++;
                    inParens = false;
                }
            }
            answers[i] = r.toString();
        }
        return answers;
    }

    /**
     * @return The Dictionary
     */
    public static Dictionary getDict() {
        if (dict == null)
            throw new NullPointerException("No dict yet!");
        return dict;
    }

    public static String getStaticDefString(String word) {
        String result = "";
        Vector<Synset> defs = SemanticRelations.getSynsets(word);
        if (defs.size() == 0)
            result += "[ nothing found ]";
        for (Synset s : defs) {
            result = result + " " + s.getGloss() + " #";
        }
        return result.toLowerCase().replaceAll(word.toLowerCase(),
                word.charAt(0) + ".");
    }

    public String getDefString(String word) {
        System.out.println("Getting def string: " + word);
        return WNLookup.getStaticDefString(word);
    }

    public static String getPOS(String originalWord) {
        if (originalWord == null)
            return null;
        String result = new String();
        IndexWordSet words;
        try {
            words = dict.lookupAllIndexWords(originalWord);
            Iterator iter = words.getIndexWordCollection().iterator();
            IndexWord word;
            while (iter.hasNext()) {
                word = (IndexWord) iter.next();
                if (word != null) {
                    result = word.getPOS().getLabel();
                    return result;
                }
            }
        } catch (JWNLException e) {
            e.printStackTrace();
        }

        return result;
    }

}