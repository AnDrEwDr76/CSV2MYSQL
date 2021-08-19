import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrainToName {

    public static String nameToDb(String name) {
        name = name.replaceAll("[^\\p{L}\\p{Z}]", "");
//        name = name.replaceAll(" ", "");
//        name = name.replaceAll(":", "");
//        name = name.replaceAll("\\.", "");
//        name = name.replaceAll("", "");
//        name = name.replaceAll("/", "");
        name = name.toLowerCase(Locale.ROOT) + "";


        return name;
    }

    public static String nameToName (String name) {
        name = name.replaceAll("-", "");
        name = name.replaceAll(" ", "");
        name = name.replaceAll(":", "");
        name = name.replaceAll("\\.", "");
        name = name.replaceAll("", "");

        name = name.toLowerCase(Locale.ROOT);
        name = name.substring(2,name.length()-3);
        return name;
    }


}
