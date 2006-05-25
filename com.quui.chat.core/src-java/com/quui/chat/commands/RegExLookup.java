/** 
 Project "com.quui.chat.core" (C) 2006 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.quui.chat.commands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RegEx-based lookup for a website
 * 
 * @author Fabian Steeg (fsteeg)
 */
public class RegExLookup {
    public RegExLookup() {
    }

    /**
     * @param adress
     *            The full adress to look up
     * @param encoding
     *            The encoding the website uses
     * @return The content of the website
     */
    public static String open(String adress, String encoding) {
        StringBuilder builder;
        try {
            URL url = new URL(adress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn
                    .getInputStream(), encoding));
            String line;
            builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * @param siteContent
     *            The content of the site
     * @param REGEX
     *            The regex to match agains the content
     * @return The Collection of matching sections of the content
     */
    public static Collection<String> match(String siteContent, String REGEX) {
        Collection<String> results = new Vector<String>();
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(siteContent.toString());
        boolean found = false;
        while (matcher.find()) {
            // String group0 = matcher.group(0);
            String group1 = matcher.group(1);
            String clean = clean(group1);
            if (matcher.groupCount() == 2) {
                String group2 = matcher.group(2);
                String clean2 = clean(group2);
                results.add(clean + " -- " + clean2);
            } else
                results.add(clean);
            found = true;
        }

        if (!found) {
            results.add("No match found.");
        }
        return results;
    }

    /**
     * @param s
     *            The string to clean from tags and stuff
     * @return The clean, human-readable text
     */
    private static String clean(String s) {
        s = s.replaceAll("\n", " ");
        s = s.replaceAll("<.+?>", " ");
        s = s.replaceAll("&.*?;", " ");
        s = s.replaceAll(" +", " ");
        return s;
    }
}
