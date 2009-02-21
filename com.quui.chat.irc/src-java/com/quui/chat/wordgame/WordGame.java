/** 
 Project "com.quui.chat.core" (C) 2006 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.quui.chat.wordgame;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;

import com.quui.chat.mind.wn.SemanticRelations;
import com.quui.chat.mind.wn.WNLookup;
import com.quui.chat.ui.Hollaka;

/**
 * The classical wordnet game, implemented using wordnet
 * 
 * @author Fabian Steeg (fsteeg)
 */
public class WordGame {
    private Hollaka bot;

    private Random r = new Random();

    /**
     * @param bot
     *            The irc bot who plays word
     */
    public WordGame(Hollaka bot) {
        this.bot = bot;
    }

    /**
     * For unit tests
     */
    public WordGame() {
    }

    /**
     * @param channel
     *            The channel to play on
     */
    public void play(final String channel, final boolean hard) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    IndexWord word = getRandomWord(hard);
                    // System.out.println("Word is: " + word);
                    String scrambled = scramble(word.getLemma());
                    bot.wordGameRunning = true;
                    bot.sawGameDone = false;
                    bot.wordScore = 200;
                    if (hard)
                        bot.wordScore = bot.wordScore * 3;
                    bot.wordWord = word.getLemma();
                    String dots = "";
                    char[] toCharArray = word.getLemma().substring(2)
                            .toCharArray();
                    for (int i = 0; i < toCharArray.length; i++) {
                        dots = dots + ".";
                    }
                    int deduction = 10;
                    int thinkingTime = 15000;

                    // scrambled word
                    bot.send_privmsg(channel, "[w] [" + scrambled + "]");

                    if (isDone(thinkingTime, deduction))
                        return;

                    // part of speech
                    bot.send_privmsg(channel, "[w] Part of speech: "
                            + word.getPOS().getLabel());

                    if (isDone(thinkingTime, deduction))
                        return;

                    // beginning
                    bot.send_privmsg(channel, "[w] ["
                            + word.getLemma().charAt(0) + dots + ".]");

                    if (isDone(thinkingTime, deduction))
                        return;

                    // beginning and end
                    bot.send_privmsg(channel, "[w] ["
                            + word.getLemma().charAt(0)
                            + dots
                            + word.getLemma().charAt(
                                    word.getLemma().length() - 1) + "]");

                    if (isDone(thinkingTime, 60))
                        return;

                    // definition
                    String sendString = WNLookup.getStaticDefString(
                            word.getLemma()).split(";")[0].trim().replaceAll(
                            "#", "");
                    sendString = shortenIfRequired(sendString);
                    bot.send_privmsg(channel, "[w] Definition: " + sendString);

                    if (isDone(thinkingTime, deduction))
                        return;

                    // antonyms
                    String res = SemanticRelations.getAntoString(
                            word.getLemma()).replaceAll("\\[ none \\]", "")
                            .trim();
                    if (!res.equals("")) {
                        res = shortenIfRequired(res);
                        bot.send_privmsg(channel, "[w] Opposite: " + res);
                        if (isDone(thinkingTime, deduction))
                            return;
                    }

                    // synonymes
                    res = SemanticRelations.getSynoString(word.getLemma())
                            .replaceAll("\\[ none \\]", "").trim();
                    if (!res.equals("")) {
                        res = shortenIfRequired(res);
                        bot.send_privmsg(channel, "[w] Synonym: " + res);

                        if (isDone(thinkingTime, deduction))
                            return;
                    }
                    // hypernyms
                    if (word.getPOS().equals(POS.NOUN)
                            || word.getPOS().equals(POS.VERB)) {
                        res = SemanticRelations.getHyperString(word.getLemma())
                                .replaceAll("\\[ none \\]", "").trim();
                        if (!res.equals("")) {
                            res = shortenIfRequired(res);
                            bot.send_privmsg(channel, "[w] Parents: " + res);

                            if (isDone(thinkingTime, deduction))
                                return;
                        }
                    }
                    // hyponyms
                    if (word.getPOS().equals(POS.NOUN)) {
                        String hypo = SemanticRelations.getHypoString(
                                word.getLemma()).replaceAll("\\[ none \\]", "")
                                .trim();
                        if (!hypo.equals("")) {
                            hypo = shortenIfRequired(hypo);
                            bot.send_privmsg(channel, "[w] Children: " + hypo);

                            if (isDone(thinkingTime, deduction))
                                return;
                        }
                    }
                    // siblings
                    if (word.getPOS().equals(POS.NOUN)
                            || word.getPOS().equals(POS.VERB)) {
                        res = SemanticRelations.getSiblingsString(
                                word.getLemma()).replaceAll("\\[ none \\]", "")
                                .trim();

                        if (!res.equals("")) {
                            res = shortenIfRequired(res);
                            bot.send_privmsg(channel, "[w] Siblings: " + res);

                            if (isDone(thinkingTime, deduction))
                                return;
                        }
                    }
                    // holo- and meronyms
                    if (word.getPOS().equals(POS.NOUN)) {
                        String hypo = SemanticRelations.getMeroString(
                                word.getLemma()).replaceAll("\\[ none \\]", "")
                                .trim();
                        if (!hypo.equals("")) {
                            hypo = shortenIfRequired(hypo);
                            bot.send_privmsg(channel, "[w] Meronyms (parts): "
                                    + hypo);

                            if (isDone(thinkingTime, deduction))
                                return;
                        }

                        hypo = SemanticRelations.getHoloString(word.getLemma())
                                .replaceAll("\\[ none \\]", "").trim();
                        if (!hypo.equals("")) {
                            hypo = shortenIfRequired(hypo);
                            bot.send_privmsg(channel, "[w] Holonyms (whole): "
                                    + hypo);

                            if (isDone(thinkingTime, deduction))
                                return;
                        }
                    }
                    bot.send_privmsg(channel, "[w] Time over! The word was: "
                            + word.getLemma());
                    bot.wordGameRunning = false;
                    bot.sawGameDone = true;
                } catch (InterruptedException e) {
                    System.out.println("Threads caused trouble!");
                    bot.send_privmsg(channel, "Ooops! Something went wrong!");
                    bot.wordGameRunning = false;
                    bot.sawGameDone = true;
                    e.printStackTrace();
                } catch (JWNLException e) {
                    System.out.println("WN caused trouble!");
                    bot.send_privmsg(channel, "Ooops! Something went wrong!");
                    bot.wordGameRunning = false;
                    bot.sawGameDone = true;
                    e.printStackTrace();
                }

            }

            private String shortenIfRequired(String sendString) {
                if (sendString.length() > 300)
                    sendString = sendString.substring(0, 300) + "...";
                return sendString;
            }

        }).start();

    }

    /**
     * @param thinkingTime
     *            The time to give the player to think
     * @param deduction
     *            The score to substract from the total score
     * @return 'true' if the game has been solved, else 'false'
     * @throws InterruptedException
     *             On thread-related problems
     */
    protected boolean isDone(int thinkingTime, int deduction)
            throws InterruptedException {
        Thread.sleep(thinkingTime);
        if (!bot.wordGameRunning) {
            System.out.println("Game is done!");
            bot.sawGameDone = true;
            return true;
        }
        bot.wordScore = bot.wordScore - deduction;
        return false;
    }

    /**
     * @param hard
     * @return A random word from wordnet, any POS
     * @throws JWNLException
     *             On problems on WordNet lookup
     */
    public IndexWord getRandomWord(boolean hard) throws JWNLException {
        int n = r.nextInt(4);
        POS toPick = null;
        switch (n) {
        case 0:
            toPick = POS.ADJECTIVE;
            break;
        case 1:
            toPick = POS.ADVERB;
            break;
        case 2:
            toPick = POS.NOUN;
            break;
        case 3:
            toPick = POS.VERB;
            break;
        }
        try {
            new WNLookup().init("config/file_properties.xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        IndexWord word = WNLookup.getDict().getRandomIndexWord(toPick);
        // normally, don't use complex words
        boolean condition = word.getLemma().contains(" ");
        // for hard games, use only complex words
        // if(hard)
        // condition = !condition;
//        while (word.getLemma().length() < 4 || condition)
//            word = WNLookup.getDict().getRandomIndexWord(toPick);
        return WNLookup.getDict().getRandomIndexWord(toPick);
    }

    /**
     * @param word
     *            The original word to scramble
     * @return The scrambled word
     */
    public String scramble(String word) {
        char[] chars = word.toCharArray();
        ArrayList<Character> list = new ArrayList<Character>();
        for (char c : chars) {
            list.add(c);
        }
        Collections.shuffle(list);
        StringBuilder builder = new StringBuilder();
        for (Object object : list) {
            builder.append(object);
        }
        String result = builder.toString();
        return result;
    }
}
