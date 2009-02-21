

import java.io.File;

import org.jruby.IRuby;
import org.jruby.runtime.builtin.IRubyObject;

public class ScriptRunner {
    public static void main(String[] args) throws Exception {
        IRuby runtime = org.jruby.Ruby.getDefaultInstance();
        try {
            File f = new File("ruby/snippets1.rb");
            runtime.loadFile(f, false);
            f = new File("ruby/snippets2.rb");
            runtime.loadFile(f, false);
            IRubyObject rubyObject = runtime.evalScript("test1");
            System.out.println(rubyObject.asSymbol());
            rubyObject = runtime.evalScript("test2");
            System.out.println(rubyObject.asSymbol());
        } catch (Exception e) {
            System.out.println("Exception while sourcing file " + args[0]);
            e.printStackTrace();
        }
    }
}