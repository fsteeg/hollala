import java.util.List;

/**
 * dict.leo.org based german-english and english-german translation
 * 
 * @author Fabian Steeg (fsteeg)
 */
public class Leo {
    // leo adress for search queries
    private final static String ADRESS = "http://dict.leo.org/?lp=ende&lang=de&searchLoc=0&cmpType=relaxed&relink=on&sectHdr=on&spellToler=on&search=";

    // regex to match and capture the word or expressions in both languages from
    // the pretty messy leo html
    private final static String REGEX = "<td nowrap width=\"5%\">(?:(?:&#160;)|(?:<img.+?/>))</td>(.*?)<td nowrap width=\"2%\">&#160;</td>.*?<td valign=\"middle\" width=\"43%\">(.*?)</td>";//"<td class=\"td1\" nowrap width=\"5%\">(.*?)<td class=\"td1\" nowrap width=\"2%\">&nbsp;</td>.*?<td class=\"td1\" valign=\"middle\" width=\"43%\">(.*?)</td>";

    // the encoding the leo site uses
    private final static String ENCODING = "iso-8859-15";

    /**
     * @param word
     *            The german or english word to look up
     * @return The result, containing multiple words, english and german
     */
    public static String lookup(String word) {
        String lookupAdress = ADRESS + word;
        WebsiteLookup lookup = new WebsiteLookup(lookupAdress, ENCODING);
//        System.out.println(lookup.content);
        List<String> results = lookup.match(REGEX);
        String result = word + ": ";
        int lastIndex = 0;
        for (String s : results) {
            // abbreviate the query word:
            String changed = s.toLowerCase().replaceAll(word.toLowerCase(),
                    word.charAt(0) + ".").trim();
            if ((result.length() - lastIndex) > 250) {
                result += "#";
                lastIndex = result.lastIndexOf(changed);
            }
            if (s.equals(WebsiteLookup.DELIM))
                result = result.trim() + "; ";
            else
                result = result + "\"" + changed + "\"" + " ";
        }
        return result;
    }

    /**
     * Usage demo
     */
    public static void main(String[] args) {
        System.out.println(Leo.lookup("door"));
        System.out.println(Leo.lookup("boden"));
    }
}
