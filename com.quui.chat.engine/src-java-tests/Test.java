import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

public class Test {

    /**
     * @param args
     * @throws MalformedURLException 
     * @throws URISyntaxException 
     */
    public static void main(String[] args) throws MalformedURLException, URISyntaxException {
        String escapeHtml = StringEscapeUtils.escapeHtml("http://www.google.com?q=a b");
        System.out.println(new URL("http://www.google.com?q=a b").toURI());
        
        
    // TODO Auto-generated method stub
        String string = "Company A, 123 Any Street Suite 200, Anytown, AK 99012";
        String string2 = "CA, 123 Any Street Ste 200, Anytown, AK 99012";
        String string3 = "Comp. A, 123 Any St., Suite 200, Anytown, AK 99012";
        int levenshteinDistance = StringUtils.getLevenshteinDistance(string, string3);
        System.out.println(string.length());
        System.out.println(string2.length());
        System.out.println(string3.length());
        System.out.println(levenshteinDistance);
    }
}
