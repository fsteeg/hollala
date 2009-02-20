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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import junit.framework.TestCase;

/**
 * Tests for the mind
 * @author Fabian Steeg (fsteeg)
 */
public class MindTest extends TestCase {
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
        String wn = p.getProperty("wn");
        t = new Talk("config", "test.xml", wn == null || wn.equals("no") ? null
                : wn, p.getProperty("log"));
    }

    /**
     * Test method for 'com.quui.chat.mind.Mind.processConversation(String,
     * boolean)'
     */
    public void testProcessConversation() {
        // something known:
        String answer = t.process("hi", true);
        System.out.println("---------------Answer: " + answer);
        
        answer = t.process("ok fine", true);
        // t.save();
        System.out.println("---------------Answer: " + answer);
        // something new:
        answer = t.process("jackie", true);
        // t.save();
        System.out.println("---------------Answer: " + answer);
    }

}
