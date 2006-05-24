/** 
 Project "com.quui.chat.core" (C) 2006 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.quui.chat.commands;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

import org.jruby.IRuby;
import org.jruby.RubyHash;

import com.quui.chat.GlobalProperties;

/**
 * @author Fabian Steeg (fsteeg)
 */
public class RubyCaller {
    private IRuby runtime;

    private String directory;

    protected Map<String, String> map;

    /**
     * @param directory
     *            The directory containing the Ruby programs to plug in
     */
    public RubyCaller(String directory) {
        this.directory = directory;
        map = new HashMap<String, String>();
        loadFiles();
    }

    /**
     * Loads the Ruby programs and stores the command-to-method mapping in the
     * Map
     */
    private void loadFiles() {
        runtime = org.jruby.Ruby.getDefaultInstance();
        try {

            String[] files = new File(directory).list(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    if (name.endsWith(".rb"))
                        return true;
                    return false;
                }
            });
            for (String string : files) {
                File f = new File(directory + File.separator + string);
                runtime.loadFile(f, false);
                RubyHash rubyObject = (RubyHash) runtime.evalScript("init");
                for (Object s : rubyObject.keys().getList()) {
                    System.out.println(s);
                    map.put(s.toString(), rubyObject.get(s).toString());
                }
                // map.put(rubyObject.get(0).toString(), rubyObject.get(1)
                // .toString());
            }
        } catch (Exception e) {
            System.out.println("Exception while sourcing file " + "");
            e.printStackTrace();
        }
    }

    /**
     * @param command
     *            The command to execute
     * @param param
     *            The param to the command
     * @return Return the result of the Ruby program mapped to the command. (Or
     *         an error message)
     */
    public String exec(String command, String param) {
        if (command.equals("help"))
            return returnHelp();

        String method = map.get(command);
        if (method == null)
            return "";
        System.out.println("About to exec: " + command + " with param: "
                + param + " with method: " + method);
        try {
            String call = method + "('" + param.trim() + "')";
            System.out.println("Call: " + call);
            return runtime.evalScript(call).toString();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return "Something went wrong!";
        }

    }

    /**
     * @return Return the currently active commands, as defined in ruby scripts.
     *         Built-in commands are added.
     */
    private String returnHelp() {
        String r = "Available commands: ";
        // ruby-commands
        for (String s : map.keySet()) {
            r = r + GlobalProperties.getInstance().getCommandPrefix() + s + " ";
        }
        // non-ruby-commands
        r = r.trim() + " " + GlobalProperties.getInstance().getCommandPrefix()
                + "word " + GlobalProperties.getInstance().getCommandPrefix()
                + "more";
        return r;
    }

    public Map<String, String> getMap() {
        return map;
    }

}
