package com.quui.chat.tests;


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
