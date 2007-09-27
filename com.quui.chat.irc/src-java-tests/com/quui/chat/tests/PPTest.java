package com.quui.chat.tests;

import junit.framework.TestCase;

import com.quui.chat.commands.PP;

public class PPTest extends TestCase {

	/*
	 * Test method for 'com.quui.chat.commands.PP.getPP()'
	 */
	public void testGetPP() {
		String res = PP.getStaticPP();
		assertTrue(res != null);
		System.out.println(res);
	}

	public void testAddPP() {
		String addStaticPP = PP.addStaticPP("Have fun!");
		assertTrue("Didn't work: " + addStaticPP, addStaticPP
				.startsWith("Alright"));
	}
}
