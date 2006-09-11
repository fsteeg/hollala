package com.quui.chat.tests;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.quui.chat.commands.RubyCaller;

public class RubyCallerTest {

    private RubyCaller caller;

    @Before
    public void init() {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream("config/hollaka.properties"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        caller = new RubyCaller("src-ruby", p);
    }

    @Test
    public void call() {
        // String string = caller.exec("!tuennes", "testing1")[0];
        // Assert.assertEquals("Testing1", string);
        // string = caller.exec("!kappes", "testing2");
        // Assert.assertEquals("Testing2", string);
        // string = caller.exec("kappesdudu", "testing2");
        // Assert.assertEquals("", string);
    }

    @Test
    public void callAll() {
//        for (String command : caller.getMap().keySet()) {
//            String string = (String) caller.exec(command, "test")[0];
//            Assert.assertTrue(string != null);
//        }
    }

}
