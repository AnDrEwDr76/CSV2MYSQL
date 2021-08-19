import java.util.ArrayList;

public class PrintOnScreen {


    public static void arrayList (ArrayList<String[]> allRows) {
        for (String[] rows : allRows) {
            for (String word : rows) {
                System.out.print(word + " ");
            }
            System.out.println("");
        }
    }

    public static void arrayList (ArrayList<String[]> allRows, String separ) {
        for (String[] rows : allRows) {
            for (String word : rows) {
                System.out.print(word + separ);
            }
            System.out.println("");
        }
    }

    public static void arrayList (String[] allRows) {
        for (String word : allRows) {
            System.out.print(word + " ");
        }
        System.out.println("");
    }

    public static void arrayList (String[] allRows, String separ) {
        for (String word : allRows) {
            System.out.print(word + separ);
        }
        System.out.println("");
    }


}
