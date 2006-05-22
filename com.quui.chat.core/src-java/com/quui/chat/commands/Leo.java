/** 
Project "com.quui.chat.core" (C) 2006 Fabian Steeg

This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.quui.chat.commands;

import java.util.Collection;

/**
 * dict.leo.org based german-english and english-german translation
 * 
 * @author Fabian Steeg (fsteeg)
 */
public class Leo {
    /**
     * @param word
     *            The german or english word to look up
     * @return The result, containing multiple words, english and german
     */
    public static String lookup(String word) {
        String REGEX = "<td class=\"td1\" nowrap width=\"5%\">(.*?)<td class=\"td1\" nowrap width=\"2%\">&nbsp;</td>.*?<td class=\"td1\" valign=\"middle\" width=\"43%\">(.*?)</td>";
        String ADRESS = "http://dict.leo.org/?lp=ende&lang=de&searchLoc=0&cmpType=relaxed&relink=on&sectHdr=on&spellToler=on&search=";
        ADRESS += word;
        String string = RegExLookup.open(ADRESS, "iso-8859-15");
        Collection<String> results = RegExLookup.match(string, REGEX);
        string = RegExLookup.open(ADRESS, "iso-8859-15");
        results = RegExLookup.match(string, REGEX);
        String result = "[" + word + "]";
        int lastIndex = 0;
        int i = 0;
        for (String s : results) {

            String changed = s.toLowerCase().replaceAll(word.toLowerCase(),
                    word.charAt(0) + ".").trim();
            result = result + " " + changed;

            if ((result.length() - lastIndex) > 250) {
                result += "#";
                lastIndex = result.lastIndexOf(changed);
            } else if (i < results.size() - 1)
                result += ",";
            i++;

        }
        return result;
    }

    public String translate(String in) {
        return Leo.lookup(in);
    }
}
