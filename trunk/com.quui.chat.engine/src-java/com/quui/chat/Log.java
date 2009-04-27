/** 
 Project "com.quui.chat.core" (C) 2006 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.quui.chat;

import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.varia.LevelMatchFilter;

/**
 * Log4J based logging to two different html files for info and debug
 * 
 * @author Fabian Steeg (fsteeg)
 */
public class Log {
    /**
     * The logger, use info() or debug()
     */
    public static Logger logger;

    /**
     * @param logLoc
     *            The folder to log to, will contains two files: index.html (for
     *            info) and debug.html (for debug)
     */
    public static void init(String logLoc) {

        logger = Logger.getLogger(Log.class);

        try {
            FileAppender infoFileAppender = new FileAppender(new HTMLLayout(),
                    logLoc + "/index.html");

            FileAppender debugFileAppender = new FileAppender(new HTMLLayout(),
                    logLoc + "/debug.html");

            logger.addAppender(infoFileAppender);
            logger.addAppender(debugFileAppender);

//            ConsoleAppender consoleAppender = new ConsoleAppender(
//                    new SimpleLayout());

            // print all but debug messages:
            LevelMatchFilter debugLevelMatchFilter = new LevelMatchFilter();
            debugLevelMatchFilter.setLevelToMatch("debug");
            debugLevelMatchFilter.setAcceptOnMatch(false);
            infoFileAppender.addFilter(debugLevelMatchFilter);

//            consoleAppender.addFilter(debugLevelMatchFilter);

//            logger.addAppender(consoleAppender);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("Logger initialized, writing to: " + logLoc);
    }
}
