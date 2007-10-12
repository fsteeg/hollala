/** 
 Project "com.quui.chat.core" (C) 2006 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.quui.chat;

import com.quui.chat.commands.Babelfish;

import junit.framework.TestCase;

public class TranslateTest extends TestCase {

    /*
     * Test method for 'com.quui.chat.commands.Translate.translate(String,
     * String, String)'
     */
    public void testTranslate() {
        System.out.println(Babelfish.staticTranslate("de", "en",
                "der weg ist das ziel"));
        System.out.println(Babelfish.staticTranslate("de", "fr",
                "der weg ist das ziel"));
        System.out.println(Babelfish.staticTranslate("de", "es",
                "der weg ist das ziel"));
        System.out.println(Babelfish.staticTranslate("en", "de",
                "the journey is the destination"));
        System.out.println(Babelfish.staticTranslate("en", "de",
                "the door has been closed"));
    }

}
