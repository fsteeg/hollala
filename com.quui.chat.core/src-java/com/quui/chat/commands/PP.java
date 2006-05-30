/** 
 Project "com.quui.chat.core" (C) 2006 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.quui.chat.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * @author Fabian Steeg (fsteeg)
 */
public class PP {

    /**
     * @return A random tip or quote from "The pragmatic programmer"
     */
    public static String getStaticPP() {
        List<String> quotes = getQuotes("config/pp-quotes");
        Collections.shuffle(quotes);
        return quotes.get(0);
    }

    /**
     * @return See getStaticPP()
     */
    public String getPP() {
        return PP.getStaticPP();
    }

    /**
     * @param string
     *            The location of the file containing the quotes
     * @return Returns all the quotes in the file
     */
    private static List<String> getQuotes(String string) {
        List<String> result = new Vector<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(string));
            String line = "";
            while ((line = reader.readLine()) != null)
                result.add(line);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Gets the tips, writes them to a file... This way I retrived the basic tip
     * from their website...
     */
    public void readFromWeb() {
        String loc = "http://www.pragmaticprogrammer.com/ppbook/extracts/rule_list.html";
        String regex = "<tr><td bgcolor=\"#99cc99\" height=20><large><b>(.*?)</b>.*?<td bgcolor=\"#ffffcc\">(.*?)</td></tr>";
        // System.out.println(content);
        Collection<String> quotes = new WebsiteLookup(loc, "iso-8859-1")
                .match(regex);
        int i = 1;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(
                    "config/generated-pp-quotes"));
            for (String string : quotes) {
                System.out.println(string);
                writer.write(string + "\n");
                i++;
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * To read the basic tips from the pragmatic progammer's website
     * 
     * @param a
     *            Nothing
     */
    public static void main(String[] a) {
        new PP().readFromWeb();
    }
}
