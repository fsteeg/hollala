package com.quui.chat.tests;


import com.quui.chat.wordgame.WordGameScores;

import junit.framework.TestCase;

public class WordGameScoresTest extends TestCase {

    /*
     * Test method for 'com.quui.chat.commands.WordGameScores.getScores()'
     */
    public void testGetScores() {

        String scores = WordGameScores.getStaticScores(5);
        assertTrue(scores != null);
        System.out.println("Scores: " + scores);

    }

}
