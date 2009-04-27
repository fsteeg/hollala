/** 
 Project "com.quui.chat.core" (C) 2006 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.quui.chat.wordgame;

import java.io.FileInputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;

public class WordGameScores {
    /**
     * Location of the properties file containing the user names and scores
     */
    public static final String wordScorePropertiesLocation = "config/word-scores";

    private static Properties wordScoreProperties;

    public String getScores(int max) {
        return WordGameScores.getStaticScores(max);
    }

    /**
     * @return A string containing all playes and their score, sorted by ratio
     *         of points to playes games, '#'-delimited
     */
    @SuppressWarnings("unchecked")
    public static String getStaticScores(int max) {
        StringBuilder builder = new StringBuilder();
        initWordScoreProperties();
        ArrayList names = new ArrayList(wordScoreProperties.keySet());
        Collections.sort(names, new Comparator() {
            public int compare(Object o1, Object o2) {
                String val1 = (String) wordScoreProperties.get(o1);
                String val2 = (String) wordScoreProperties.get(o2);
                String[] vals1 = val1.split(",");
                int score1 = Integer.parseInt(vals1[0]);
                int freq1 = Integer.parseInt(vals1[1].trim());
                String[] vals2 = val2.split(",");
                int score2 = Integer.parseInt(vals2[0].trim());
                int freq2 = Integer.parseInt(vals2[1].trim());
                double ratio1 = score1 / (double) freq1;
                double ratio2 = score2 / (double) freq2;
                int compareTo = new Double(ratio2)
                        .compareTo(new Double(ratio1));
                return compareTo;
            }

        });
        int i = 1;
        for (Object object : names) {
            String property = wordScoreProperties.getProperty((String) object);
            int score = Integer.parseInt(property.split(",")[0].trim());
            int freq = Integer.parseInt(property.split(",")[1].trim());
            double d = score / (double) freq;

            builder.append(
                    i + ". " + object + ": " + property + " ("
                            + NumberFormat.getInstance().format(d) + ")")
                    .append(" # ");
            if (i == max)
                break;
            i++;
        }
        return builder.toString();
    }

    /**
     * loads the scores from the properties file
     */
    private static void initWordScoreProperties() {
        wordScoreProperties = new Properties();
        try {
            FileInputStream in = new FileInputStream(
                    wordScorePropertiesLocation);
            wordScoreProperties.load(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
