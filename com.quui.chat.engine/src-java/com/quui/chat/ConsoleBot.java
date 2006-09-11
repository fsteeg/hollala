/** 
 Project "com.quui.chat.core" (C) 2006 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.quui.chat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConsoleBot {

    private static String NAME = "Console";

    private static String topicFile = "topics-" + NAME.toLowerCase() + ".xml";

    private static Talk talk;

    /**
     * @param args
     *            nothing, using values in code
     */
    public static void main(String[] args) {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream("config/global.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String logFolder = p.getProperty("log") + "/logs-" + NAME.toLowerCase();
        String wnLocation = p.getProperty("wn");
        String topicFolder = p.getProperty("config");
        talk = new Talk(topicFolder, topicFile, wnLocation, logFolder);
        talk.talk();
    }
}
