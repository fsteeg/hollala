package com.quui.chat;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A suite to run all tests.
 * @author fsteeg
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( { MindTest.class, PreprocessorTest.class,
        SemanticRelationsTest.class })
public class ChatSuite {

}
