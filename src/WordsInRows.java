import java.util.ArrayList;
import java.util.HashMap;

public class WordsInRows {

    public static HashMap<Integer, Integer> count(ArrayList<String[]> allRows) {
        HashMap<Integer, Integer> wordCount = new HashMap<>();
        int countWords = 0; // word's number in each rows[]
        int countRows = -1; // -1 для того, чтобы счёт в таблице начинался с нуля

        for (String[] rows : allRows) {
            for (String a : rows) {
                countWords++;
            }
            countRows++;
            wordCount.put(countRows, countWords);
            countWords = 0;
        }
        return wordCount;
    }

    public static ArrayList<String[]> clean (ArrayList<String[]> allRows) {
        ArrayList<String[]> newAllRows = new ArrayList<>();

        int iter = 0;
        for (String[] line : allRows) {
            for (int i = 0; i < line.length; i++) {
                line[i] = line[i].replace("\u0000", "");
                line[i] = line[i].trim();
            }
            newAllRows.add(iter, line);
            iter++;
        }
        return newAllRows;
    }



}
