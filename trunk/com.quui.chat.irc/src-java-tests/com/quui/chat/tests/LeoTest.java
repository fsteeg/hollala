package com.quui.chat.tests;


import com.quui.chat.commands.Leo;

import junit.framework.TestCase;

public class LeoTest extends TestCase {

    /*
     * Test method for 'com.quui.chat.commands.Leo.lookup(String)'
     */
    public void testLookup() {
        System.out.println(Leo.lookup("door"));
        // System.out.println(Leo.lookup("floor"));
        System.out.println(Leo.lookup("boden"));

    }

}
