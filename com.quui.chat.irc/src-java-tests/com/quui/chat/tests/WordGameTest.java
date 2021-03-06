package com.quui.chat.tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import junit.framework.TestCase;
import net.didion.jwnl.JWNLException;

import com.quui.chat.Talk;
import com.quui.chat.wordgame.WordGame;

public class WordGameTest extends TestCase {

    private WordGame w;

    /*
     * Test method for 'com.quui.chat.commands.Word.getRandomWord()'
     */
    public void testGetRandomWord() {
        String word;
        try {
            word = w.getRandomWord(false).getLemma();
            assertTrue(word != null);
        } catch (JWNLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /*
     * Test method for 'com.quui.chat.commands.Word.scramble(String)'
     */
    public void testScramble() {
        String word;
        try {
            word = w.getRandomWord(false).getLemma();
            System.out.println("Word: " + word);
            String scrambled = w.scramble(word);
            assertTrue(word != null);
            assertTrue(!scrambled.equals(word));
            System.out.println("Scrambled: " + scrambled);
        } catch (JWNLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void setUp() throws Exception {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream("config/hollaka.properties"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        @SuppressWarnings("unused")
        Talk t = new Talk("config", "topics-test.xml", p.getProperty("wn"), p
                .getProperty("log"));
        w = new WordGame();
        super.setUp();
    }
}
