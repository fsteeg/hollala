/** 
Project "com.quui.chat.core" (C) 2006 Fabian Steeg

This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/
package com.quui.chat.commands;

import java.util.Collection;

/**
 * babelfish.altavista.com based translation
 * 
 * @author Fabian Steeg (fsteeg)
 */
public class Babelfish {

    private static final String REGEX = "<td bgcolor=white class=s><[^>]*>(.*)</div>";

    static String ADRESS;

    /**
     * @param source
     *            The source lang, like "en" or "de"
     * @param dest
     *            The source lang
     * @param text
     *            The text to translate
     * @return The original and the translated text
     */
    public static String staticTranslate(String source, String dest, String text) {
        text = text.trim();
        String orig = text;
        text = text.replaceAll(" ", "%20");
        ADRESS = "http://babelfish.altavista.com/tr?doit=done&intl=1&tt=urltext&lp="
                + source + "_" + dest + "&submit=Translate&trtext=" + text;
        // though it says utf-8 on their site, it doesnt seem to be true, sigh
        String string = RegExLookup.open(ADRESS, "iso-8859-1");
        Collection<String> results = RegExLookup.match(string, REGEX);
        // create the result string
        String result = "[" + source + "] " + orig + " # " + "[" + dest + "] ";
        for (String s : results) {
            int indexOf = s.indexOf("\t");
            if (indexOf != -1)
                result = result.trim() + " " + s.substring(0, indexOf).trim();
            else
                result = result.trim() + " " + s.substring(0).trim();
        }
        return result;
    }

    public String translate(String source, String dest, String text) {
        return Babelfish.staticTranslate(source, dest, text);
    }
}
