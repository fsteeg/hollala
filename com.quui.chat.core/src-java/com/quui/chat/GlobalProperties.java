/** 
 Project "com.quui.chat.core" (C) 2006 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package com.quui.chat;

import java.io.FileInputStream;
import java.util.Properties;

public class GlobalProperties {
    private static GlobalProperties theInstance = null;

    private static final String propertiesLocation = "config/global.properties";

    private String wnLocation;

    private String logLocation;

    private String configLocation;

    private String commandPrefix;

    private String rubyHome;

    private int millis;

    /**
     * The default constructor is private because this is an implementation of
     * the Singleton Pattern.
     */
    private GlobalProperties() {
        Properties p = new Properties();
        try {
            FileInputStream in = new FileInputStream(propertiesLocation);
            p.load(in);
            in.close();
            wnLocation = p.getProperty("wn");
            logLocation = p.getProperty("log");
            configLocation = p.getProperty("config");
            commandPrefix = p.getProperty("command");
            rubyHome = p.getProperty("ruby");
            millis = Integer.parseInt(p.getProperty("millis"));
            new Talk("config", "topics-test.xml", wnLocation, logLocation);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load properties file: "
                    + propertiesLocation + ".");
        }
    }

    /**
     * @return The Singleton
     */
    public static GlobalProperties getInstance() {
        if (theInstance == null) {
            theInstance = new GlobalProperties();
        }
        return theInstance;
    }

    public String getWNLocation() {
        return this.wnLocation;
    }

    public String getLogLocation() {
        return logLocation;
    }

    public String getConfigLocation() {
        return configLocation;
    }

//    public String getCommandPrefix() {
//        return commandPrefix;
//    }

    public String getRubyHome() {
        return rubyHome;
    }

    public int getMillisForOneWord() {
        // TODO Auto-generated method stub
        return millis;
    }
}
