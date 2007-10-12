/** 
 Project "com.quui.chat.core" (C) 2006 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.quui.chat;

import java.util.Vector;

import junit.framework.TestCase;

import com.quui.chat.Preprocessor;

/**
 * Test the preprocessing: Filtering of stopwords and weird chars
 * 
 * @author Fabian Steeg (fsteeg)
 */
public class PreprocessorTest extends TestCase {

    /**
     * Test method for 'com.quui.chat.mind.Preprocessor.preProcess(String)'
     */
    public void testPreProcess() {
        Vector<String> words = new Vector<String>();
        words.add("he");
        Preprocessor p = new Preprocessor(false, words);
        Vector r = p.preProcess("h^h^ he gut");
        for (Object object : r) {
            System.out.println(object);
        }
        assertEquals("gut", r.get(0));

    }

}
