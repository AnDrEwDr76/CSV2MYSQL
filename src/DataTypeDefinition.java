import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataTypeDefinition {
    //*************************************
    private static String patternDateMMDDYYYY = "^(0?[1-9]|1[012])[- /.](0?[1-9]|[12][0-9]|3[01])[- /.](19|20)?[0-9]{2}$";
    private static Pattern ptrnDateMMDDYYYY = Pattern.compile(patternDateMMDDYYYY);
    private static String patternDateDDMMYYYY = "^(0?[1-9]|[12][0-9]|3[01])[- /.](0?[1-9]|1[012])[- /.](19|20)?[0-9]{2}$";
    private static Pattern ptrnDateDDMMYYYY = Pattern.compile(patternDateDDMMYYYY);
    private static String patternDateDDMM = "^(0?[1-9]|[12][0-9]|3[01])[- /.](0?[1-9]|1[012])$";
    private static Pattern ptrnDateDDMM = Pattern.compile(patternDateDDMM);
    private static String patternDateYYYYMMDD = "^(19|20)?[0-9]{2}[- /.](0?[1-9]|1[012])[- /.](0?[1-9]|[12][0-9]|3[01])$";
    //    private static String patternDateYYYYDDMM = "^(19|20)?[0-9]{2}[- /.](0?[1-9]|[12][0-9]|3[01])[- /.](0?[1-9]|1[012])$";
    private static Pattern ptrnDateYYYYMMDD = Pattern.compile(patternDateYYYYMMDD);
    private static String patternDateMMDD = "^(0?[1-9]|1[012])[- /.](0?[1-9]|[12][0-9]|3[01])$";
    private static Pattern ptrnDateMMDD = Pattern.compile(patternDateMMDD);
    //****** DATE LIKE NOTINTEGER *******
    private static String patternDateDDMM_NLN = "^(0?[1-9]|[12][0-9]|3[01])[.](0?[1-9]|1[012])$";
    private static Pattern ptrnDateDDMM_NLN = Pattern.compile(patternDateDDMM_NLN);
    private static String patternDateMMDD_NLN = "^(0?[1-9]|1[012])[.](0?[1-9]|[12][0-9]|3[01])$";
    private static Pattern ptrnDateMMDD_NLN = Pattern.compile(patternDateMMDD_NLN);
    //*************************************
    private static String patternInteger = "^[-+]?[\\d]+$";
    private static Pattern ptrnInteger = Pattern.compile(patternInteger);
    //*************************************
    private static String patternNotInteger = "^[-+]?[0-9]*[\\.][0-9]+$";
    private static Pattern ptrnNotInteger = Pattern.compile(patternNotInteger);
    //*************************************
    private static String patternTime = "^(0?[0-9]|1[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])*";
    private static Pattern ptrnTime = Pattern.compile(patternTime);
    //*************************************
    // PATTERNS FOR dateToFormat
    private static String patternD1MMDDYYYY = "^(0?[1-9]|1[012])[-](0?[1-9]|[12][0-9]|3[01])[-](19|20)?[0-9]{2}$";
    private static Pattern ptrnD1MMDDYYYY = Pattern.compile(patternD1MMDDYYYY);
    private static String patternD2MMDDYYYY = "^(0?[1-9]|1[012])[ ](0?[1-9]|[12][0-9]|3[01])[ ](19|20)?[0-9]{2}$";
    private static Pattern ptrnD2MMDDYYYY = Pattern.compile(patternD2MMDDYYYY);
    private static String patternD3MMDDYYYY = "^(0?[1-9]|1[012])[/](0?[1-9]|[12][0-9]|3[01])[/](19|20)?[0-9]{2}$";
    private static Pattern ptrnD3MMDDYYYY = Pattern.compile(patternD3MMDDYYYY);
    private static String patternD4MMDDYYYY = "^(0?[1-9]|1[012])[.](0?[1-9]|[12][0-9]|3[01])[.](19|20)?[0-9]{2}$";
    private static Pattern ptrnD4MMDDYYYY = Pattern.compile(patternD4MMDDYYYY);

    public static String defType (String lexem) {
        if (isItDate(lexem)) {return "Date";}
        if (isItTime(lexem)) {return "Time";}
        if (isItInt(lexem)) {return "Integer";}
        if (isItNotInteger(lexem)) {return "Float";}
        else {return "String";}
    }

    public static boolean isItDateLikeInteger(String lexem) {
        Matcher likeNotInteger_1 = ptrnDateDDMM_NLN.matcher(lexem);
        Matcher likeNotInteger_2 = ptrnDateMMDD_NLN.matcher(lexem);
        if (likeNotInteger_1.matches() || likeNotInteger_2.matches()) {
            return true;
        } else {
            return false;
        }

    }

    private static boolean isItDate(String lexem) {
        Matcher dateMMDDYYYY = ptrnDateMMDDYYYY.matcher(lexem);
        Matcher dateDDMMYYY = ptrnDateDDMMYYYY.matcher(lexem);
        Matcher dateDDMM = ptrnDateDDMM.matcher(lexem);
        Matcher dateYYYYMMDD = ptrnDateYYYYMMDD.matcher(lexem);
        Matcher dateMMDD = ptrnDateMMDD.matcher(lexem);
        if (dateMMDDYYYY.matches()) {
            return true; // MM- /.DD- /.YYYY or YY
        } else if (dateDDMMYYY.matches()) {
            return true; // DD- /.MM- /.YYYY or YY
        } else if (dateDDMM.matches()) {
            return true; // YYYY or YY- /.MM- /.DD
        } else if (dateYYYYMMDD.matches()) {
            return true;
        } else if (dateMMDD.matches()){
            return true;
        } else {
            return false;
        }
    }
    private static boolean isItInt(String lexem) {
        Matcher integer = ptrnInteger.matcher(lexem);
        if (integer.matches()) {
            return true;
        } else {
            return false;
        }
    }
    private static boolean isItNotInteger(String lexem) {
        Matcher nonInteger = ptrnNotInteger.matcher(lexem);
        if (nonInteger.matches()) {
            return true;
        } else {
            return false;
        }
    }
    private static boolean isItTime(String lexem) {
        Matcher time = ptrnTime.matcher(lexem);
        if (time.matches() ) {
            return true; // HH:MM:SS, H:MM:SS, HH:MM, H:MM
        } else {
            return false;
        }
    }

/*    public static String dateToFormat (String dateToF) {
        Matcher MMDDYYYY1 = ptrnD1MMDDYYYY.matcher(dateToF); // -
        Matcher MMDDYYYY2 = ptrnD2MMDDYYYY.matcher(dateToF); // space
        Matcher MMDDYYYY3 = ptrnD3MMDDYYYY.matcher(dateToF); // /
        Matcher MMDDYYYY4 = ptrnD4MMDDYYYY.matcher(dateToF); // .
        SimpleDateFormat toModification;
        String modificated = "0";

        if (MMDDYYYY1.matches()){
            toModification = new SimpleDateFormat("dd-MM-yyyy");
            System.out.println("dd-MM-yyyy");
        } else if (MMDDYYYY2.matches()){
            toModification = new SimpleDateFormat("dd MM yyyy");
        } else if (MMDDYYYY3.matches()){
            toModification = new SimpleDateFormat("dd/MM/yyyy");
            System.out.println("dd/MM/yyyy");
        } else if (MMDDYYYY4.matches()){
            toModification = new SimpleDateFormat("dd.MM.yyyy");
        } else {
            return dateToF;
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            modificated = newFormat.format(toModification.parse(dateToF));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return modificated;
    }*/

    public static String dateToMySQLFormat(String dateToF) {
        Matcher MMDDYYYY1 = ptrnD1MMDDYYYY.matcher(dateToF); // -
        Matcher MMDDYYYY2 = ptrnD2MMDDYYYY.matcher(dateToF); // space
        Matcher MMDDYYYY3 = ptrnD3MMDDYYYY.matcher(dateToF); // /
        Matcher MMDDYYYY4 = ptrnD4MMDDYYYY.matcher(dateToF); // .
        SimpleDateFormat toModification;
        String modificated = "0";

        if (MMDDYYYY1.matches()){
            toModification = new SimpleDateFormat("MM-dd-yyyy");
        } else if (MMDDYYYY2.matches()){
            toModification = new SimpleDateFormat("MM dd yyyy");
        } else if (MMDDYYYY3.matches()){
            toModification = new SimpleDateFormat("MM/dd/yyyy");
        } else if (MMDDYYYY4.matches()){
            toModification = new SimpleDateFormat("MM.dd.yyyy");
        } else {
            return dateToF;
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            modificated = newFormat.format(toModification.parse(dateToF));
        } catch (ParseException e) {
            e.printStackTrace();
            return dateToF; // return the incoming lexeme if can't parse
        }

        return modificated;
    }

}