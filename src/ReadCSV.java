import java.io.*;
import java.sql.Timestamp;
import java.util.*;

public class ReadCSV {
     public static void main (String [] args) {
         Boolean isHeaderExists = true; // есть ли шапка у таблицы
         String[] dataTypes; // шаблон типов данных
         String[] header; // шапка таблицы
         final int MIN_ROWS_IN_FILE = 5; // минимальное число строк в файле
         ArrayList<String[]> allRows = new ArrayList<>(); // список всех строк файла
         // String[] - массив строк вместо одной строки - наследие работы с OpenCSV
         int wordsInRow = 0; // количество лексем в каждой строке
         String regexSymbol = "\t"; // символ-разделитель
         String filePath = "/home/andrewdr/IdeaProjects/CSV2MYSQL/";

         String fileName = "Sernur_Step.csv"; // bomer.txt//probe_UTF8_3.txt // Alarm-20150610.csv
         // Sernur_Step.csv// probe_UTF16_3.txt // Working-20170315.csv // Sernur_T.csv
         // Alarm-20150216.csv // Sernur_PF.csv // Other-20150418.csv // toReader_1.txt
         // filecsv.csv // date.csv
         String filedir = filePath + fileName;
         String charsetName = "UTF-16";
         // "UTF-8", "UTF-16LE", "UTF-16BE", "ISO-8859-1", "Windows-1252", "KOI8-R"
         /**
          * Читаем файл построчно в allRows средствами BufferedReader.
          * Сторонних библиотек не применяем (т.к. достаточно этой), к тому же
          * сторонняя OpenCSV опускает разделители, идущие подряд.
          */
         try {
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(
                             new FileInputStream(filedir)
                             , charsetName)
             );

             String line = reader.readLine();
             String[] words = line.split(regexSymbol);
             allRows.add(words);
             System.out.println("Parsing: " + filedir + ", charset: " + charsetName);
             while ((line = reader.readLine()) != null) {
                 words = line.split("\t");
                 allRows.add(words);
             }
         } catch (FileNotFoundException ex) {
             ex.printStackTrace();
         } catch (IOException ex) {
             ex.printStackTrace();
         }
         /**
          * Пересобираем allRows
          * Уничтожим все строки с неспецифичной длиной
          */
         { // Начало специальной области видимости для манипуляций с allRows
             // подсчёт слов в строках
             HashMap<Integer, Integer> countWordsInRows = new HashMap<>();
             countWordsInRows = WordsInRows.count(allRows);
             /**
              * Всякие несоответствия в allRows. По ним заканчиваем работу,
              * а в поток ошибок выдаём соответствующее сообщение
              */
             // Если ничего не подсчитали - аварийное завершение
             if (countWordsInRows.isEmpty()) {
                 System.err.println("Can't split the file to words. Try another charset (not "
                         + charsetName + ")");
                 return;
             }
             // Если одна строка - вероятно, не та кодировка - аварийное завершение
             if (countWordsInRows.size() == 1) {
                 System.err.println("Can't split the file to words. Try another charset (not "
                         + charsetName + ")");
                 return;
             }
             // Если строк меньше MIN_ROWS_IN_FILE - аварийное завершение *** MAGIC NUMBER
             if (countWordsInRows.size() < MIN_ROWS_IN_FILE) {
                 System.err.println("Too few lines in the file: " + filedir);
                 return;
             }
             // Пересчитаем количества нахождений каждой длины
             // и получим tree map вида: {0=1, 2=1, 13=1, 24=4242}
             TreeMap<Integer, Integer> lineLengths = new TreeMap<>();
             int a = 0;
             for (int i = 0; i < countWordsInRows.size(); i++) {
                 if (lineLengths.get(countWordsInRows.get(i)) == null) {
                     lineLengths.put(countWordsInRows.get(i), 1);
                 } else {
                     a = lineLengths.get(countWordsInRows.get(i)) + 1;
                     lineLengths.put(countWordsInRows.get(i), a);
                 }
             }
             // Получили {0=1, 2=1, 13=1, 24=4242}
             // Выберем специфичную длину |num|val| |frec|
             Integer[][] lineLengthArr = new Integer[lineLengths.size()][2];
             Float[] lineLengthArrFloat = new Float[lineLengths.size()];
             int iterator = 0;
             for (Map.Entry<Integer, Integer> entry : lineLengths.entrySet()) {
                 lineLengthArr[iterator][0] = entry.getKey();
                 lineLengthArr[iterator][1] = entry.getValue();
                 lineLengthArrFloat[iterator] = (float) entry.getValue() / (float) allRows.size();
                 iterator++;
             }
             Integer numberOfMaxFrec = 0; // наиболее часто встречающееся значения длины строки
             Float maxValueLengthFloat = (float) 0; // "встречаемость" в долях единицы
             for (int i = 0; i < iterator; i++) {
                 if (maxValueLengthFloat < lineLengthArrFloat[i]) {
                     maxValueLengthFloat = lineLengthArrFloat[i];
                     numberOfMaxFrec = lineLengthArr[i][0];
                 }
             }
             if (maxValueLengthFloat < 0.5) {
                 // Если большой разнобой (>50%) в длине строк, то аварийное завершение
                 System.err.println("Too many lines of different lengths");
                 return;
             }
             // Сотрём из allRows строки с неспецифичной длиной
             // Начнём с конца, т.к. индексы меняются при удалении
             for (int i = allRows.size() - 1; i >= 0; i--) {
                 if (allRows.get(i).length != numberOfMaxFrec) {
                     allRows.remove(i);
                 }
             }
             // пересчитаем countWordsInRows, т.к. изменился allRows
             countWordsInRows.clear();
             countWordsInRows = WordsInRows.count(allRows);
             wordsInRow = countWordsInRows.get(1);
             // уничтожим лишние символы и пробелы в словах
             allRows = WordsInRows.clean(allRows);
         } // Конец области видимости для манипуляций с allRows
         /**
          * Заполним шаблон типов данных String[] dataTypes
          * и шапку String[] header
          */
         { // Начало области видимости для шаблона типов данных
             // Определяем типы данных по строкам 4 и 5 (это уже точно не шапка)
             header = allRows.get(0).clone();
             dataTypes = new String[wordsInRow];
             String[] line_3 = allRows.get(3).clone();
             String[] line_3_types = new String[wordsInRow];
             String[] line_4 = allRows.get(4).clone();
             String[] line_4_types = new String[wordsInRow];;
             int headerCoincidence = 0;
             for (int i = 0; i < line_3.length; i++) {
                 line_3_types[i] = DataTypeDefinition.defType(line_3[i]);
                 line_4_types[i] = DataTypeDefinition.defType(line_4[i]);
                 if (!(line_3_types[i].equals(line_4_types[i]))) {
                     System.err.println("Не могу распарсить, разные типы данных в колонке N" + i);
                     return;
                 }
                 // проверка на наличие шапки
                 if (DataTypeDefinition.defType(header[i])
                         .equals(line_3_types[i])) {
                     headerCoincidence++;
                     if (headerCoincidence == wordsInRow) {
                         isHeaderExists = false;
                     } else {
                         isHeaderExists = true;
                     }
                 }
             }
             // fill DATATYPES
             boolean dateExist = false;
             for (int i = 0; i < wordsInRow; i++) {
                 dataTypes[i] = line_3_types[i];
                 // для "Date" проверим окружение
                 // если справа или слева есть "Time", то ставим флаг dateExist,
                 // при наличии флага все остальные сомнительные становятся float.
                 if (
                         (i-1) >= 0
                                 && line_3_types[i-1].equals("Time")
                                 && dataTypes[i].equals("Date")) {
                     dateExist = true;
//                     continue;
                 }
                 if ((i+1) < wordsInRow
                                 && line_3_types[i+1].equals("Time")
                                 && dataTypes[i].equals("Date")) {
                     dateExist = true;
//                     continue;
                 }
                 if (!dateExist) {
                     if (dataTypes[i].equals("Date")){
                         dataTypes[i] = "Float";
                     }
                 }
                 dateExist = false;
             }
              
             // fill HEADER from allRows (no 2 start variables here!!!)
             if (isHeaderExists) {
                 for (int i = 0; i < wordsInRow; i++) {
                     header[i] = header[i].replace("\u0000", "").trim();
                     header[i] = header[i].replace(" ", "_");
                     header[i] = header[i].replace(".", "_");
                     header[i] = header[i].replace(",", "_");
                     header[i] = header[i].replace("%", "");
                     header[i] = header[i].replace("/", "");

                     // put DATA and Time into starting
                     if (dataTypes[i].equals("Time")) {
                         header[i] = "Time";
                     }
                     if (dataTypes[i].equals("Date")) {
                         header[i] = "Date";
                     }
                 }
                 allRows.remove(0);
             } else {
                 for (String word : header) {
                     word = word.trim();
                 }
                 int q = 0;
                 for (String iter : line_3_types) {
                     header[q] = "Var_" + (q+1);
                     if (iter.equals("Date")) {
                         header[q] = "Date";
                     }
                     if (iter.equals("Time")) {
                         header[q] = "Time";
                     }
                     if (iter.equals("Bool")) {
                         header[q] = "Yes/No";
                     }
                     q++;
                 }
             }
         } // Конец области видимости для шаблона типов данных



         RecivedFile recivedFile = new RecivedFile(header, dataTypes, allRows);

         Timestamp ts = new Timestamp(System.currentTimeMillis());
         recivedFile.setFilename(fileName.substring(0, fileName.length()-4));
         recivedFile.setRecivedate(ts);
         recivedFile.setEncoding (charsetName);
         recivedFile.setTablename(StrainToName.nameToDb(recivedFile.getFilename())
                 + StrainToName.nameToName(ts.toString()));

         recivedFile.changeDatesForMySQL();
//         System.out.println(recivedFile);
         int i = recivedFile.writeToDb();
//         System.out.println(recivedFile.getFilename() + " <-------------------------");

         System.out.println(i + " <----- iiiiiiiiiiiiiiiii ");



     }

}
