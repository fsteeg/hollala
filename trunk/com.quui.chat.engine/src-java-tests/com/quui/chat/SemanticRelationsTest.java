/** 
 Project "com.quui.chat.core" (C) 2006 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.quui.chat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import junit.framework.TestCase;

import com.quui.chat.mind.wn.SemanticRelations;

public class SemanticRelationsTest extends TestCase {
    String word = "cold";

    Talk t = null;

    protected void setUp() throws Exception {
        super.setUp();
        Properties p = new Properties();
        try {
            p.load(new FileInputStream("config/global.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        t = new Talk("config", "topics-test.xml", p.getProperty("wn"), p
                .getProperty("log"));
    }

    /*
     * Test method for
     * 'com.quui.chat.mind.wn.SemanticRelations.getHyperString(String)'
     */
    public void testGetHyperString() {
        String s = SemanticRelations.getHyperString(word);
        System.out.println("Hyper:" + s);
        assertTrue(s != null);
    }

    /*
     * Test method for
     * 'com.quui.chat.mind.wn.SemanticRelations.getHypoString(String)'
     */
    public void testGetHypoString() {
        String s = SemanticRelations.getHypoString(word);
        System.out.println("Hypo:" + s);
        assertTrue(s != null);
    }

    /*
     * Test method for
     * 'com.quui.chat.mind.wn.SemanticRelations.getAntoString(String)'
     */
    public void testGetAntoString() {
        String s = SemanticRelations.getAntoString(word);
        System.out.println("Anto:" + s);
        assertTrue(s != null);
    }

    /*
     * Test method for
     * 'com.quui.chat.mind.wn.SemanticRelations.getSiblingsString(String)'
     */
    public void testGetSiblingsString() {
        String s = SemanticRelations.getSiblingsString(word);
        System.out.println("Siblings:" + s);
        assertTrue(s != null);
    }

    /*
     * Test method for
     * 'com.quui.chat.mind.wn.SemanticRelations.getMeroString(String)'
     */
    public void testGetMeroString() {
        String s = SemanticRelations.getMeroString(word);
        System.out.println("Mero:" + s);
        assertTrue(s != null);
    }

    /*
     * Test method for
     * 'com.quui.chat.mind.wn.SemanticRelations.getHoloString(String)'
     */
    public void testGetHoloString() {
        String s = SemanticRelations.getHoloString(word);
        System.out.println("Holo:" + s);
        assertTrue(s != null);
    }

}
