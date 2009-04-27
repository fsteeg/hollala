/**
 * Project "com.quui.chat.core" (C) 2006 Fabian Steeg This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.quui.chat;

import java.util.Vector;

import junit.framework.TestCase;

import com.quui.chat.Preprocessor;

/**
 * Test the preprocessing: Filtering of stopwords and weird chars
 * @author Fabian Steeg (fsteeg)
 */
public class PreprocessorTest extends TestCase {

    private Preprocessor p;

    /**
     * {@inheritDoc}
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() {
        Vector<String> stopWords = new Vector<String>();
        stopWords.add("he");
        p = new Preprocessor(false, stopWords);
    }

    public void testPreProcess1() {
        Vector r = process(p, "h^h^ he gut");
        assertEquals("gut", r.get(0));

    }

    public void testPreProcess2() {
        Vector r = process(p, "how are you?");
        assertEquals(3, r.size());

    }

    private Vector process(Preprocessor p, String in) {
        Vector r = p.preProcess(in);
        System.out.println(String.format("Tokenized '%s' into '%s'", in, r));
        return r;
    }

}
