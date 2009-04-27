package com.quui.chat.tests;

import java.io.File;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.jruby.IRuby;
import org.jruby.runtime.builtin.IRubyObject;

public class ScriptRunner extends TestCase{
    private static final String DATA = "src-ruby-tests/data/";

    public void test() {
        IRuby runtime = org.jruby.Ruby.getDefaultInstance();
        try {
            File f = new File(DATA + "snippets1.rb");
            runtime.loadFile(f, false);
            f = new File(DATA + "snippets2.rb");
            runtime.loadFile(f, false);
            IRubyObject rubyObject = runtime.evalScript("init");
            Assert.assertNotNull(rubyObject);
            rubyObject = runtime.evalScript("init");
            Assert.assertNotNull(rubyObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}