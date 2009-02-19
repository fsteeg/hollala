import org.apache.commons.lang.StringUtils;

public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
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
