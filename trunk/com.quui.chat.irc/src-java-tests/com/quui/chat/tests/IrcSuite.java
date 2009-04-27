package com.quui.chat.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A suite to run all tests.
 * @author fsteeg
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( { LeoTest.class, PPTest.class, RubyCallerTest.class,
        TranslateTest.class, WordGameScoresTest.class, WordGameTest.class,
        ScriptRunner.class })
public class IrcSuite {

}
