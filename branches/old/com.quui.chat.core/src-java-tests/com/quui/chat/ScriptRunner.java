/** 
 Project "com.quui.chat.core" (C) 2006 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.quui.chat;

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