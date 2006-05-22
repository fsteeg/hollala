/** 
 Project "com.quui.chat.core" (C) 2006 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.quui.chat;

import com.quui.chat.Talk;
import com.quui.chat.wordgame.WordGame;

import net.didion.jwnl.JWNLException;
import junit.framework.TestCase;

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
        @SuppressWarnings("unused")
        Talk t = new Talk("config", "topics-test.xml", GlobalProperties
                .getInstance().getWNLocation(), GlobalProperties.getInstance()
                .getLogLocation());
        w = new WordGame();
        super.setUp();
    }

}
