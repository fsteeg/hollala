import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RegEx-based lookup for a website
 * 
 * @author Fabian Steeg (fsteeg)
 */
public class WebsiteLookup {
    /**
     * The delimiter inserted between matches (vs. groups within one match)
     */
    public static final String DELIM = " ";

    private String encoding;

    private String adress;

    String content;

    /**
     * @param adress
     *            The full adress to look up
     * @param encoding
     *            The encoding the website uses
     */
    public WebsiteLookup(String adress, String encoding) {
        if (encoding == null) {
            System.out
                    .println("Website defines no encoding! Please supply an encoding in constructor \"WebsiteLookup(adress,encoding)\". Trying UTF-8 for now.");
            encoding = "UTF-8";
        }
        this.adress = adress;
        this.encoding = encoding;
        this.content = open();
    }

    /**
     * This constructor tries to determine the encoding from the site. Use with
     * caution! If you know the encoding you should provide it.
     * 
     * @param adress
     *            The adress to use
     */
    public WebsiteLookup(String adress) throws MalformedURLException,
            IOException {
        this(adress, ((HttpURLConnection) new URL(adress).openConnection())
                .getContentEncoding());
    }

    /**
     * @return The content of the website
     */
    private String open() {
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
     * @param regex
     *            The regex to match agains the content
     * @return The List of captured groups of the content, the list will contain
     *         delimiter elements to separate matches
     */
    public List<String> match(String regex) {
        List<String> results = new Vector<String>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        boolean nothingFound = false;
        while (matcher.find()) {
            nothingFound = true;
            for (int i = 1; i <= matcher.groupCount(); i++) {
                String group = clean(matcher.group(i));
                // add each captured group
                results.add(group);
            }
            // add a delimiter between matches
            results.add(WebsiteLookup.DELIM);
        }
        if (!nothingFound) {
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
