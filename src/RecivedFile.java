import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class RecivedFile {
    String plantname, filename, encoding, someattr, tablename;
    Boolean autoprocessed, someflag;
    java.sql.Timestamp recivedate;
    String[] header;
    String[] dataTypes;
    ArrayList<String[]> allRows;

    public RecivedFile(String[] header, String[] dataTypes, ArrayList<String[]> allRows) {
        this.header = header;
        this.dataTypes = dataTypes;
        this.allRows = allRows;
    }

    @Override
    public String toString() {
        return "RecivedFile{" +
                "\nplantname='" + plantname + '\'' +
                "\nfilename='" + filename + '\'' +
                "\nencoding='" + encoding + '\'' +
                "\nsomeattr='" + someattr + '\'' +
                "\ntablename='" + tablename + '\'' +
                "\nautoprocessed=" + autoprocessed +
                "\nsomeflag=" + someflag +
                "\nrecivedate=" + recivedate +
                "\nheader=" + Arrays.toString(header) +
                "\ndataTypes=" + Arrays.toString(dataTypes) +
                "\nallRows:" +
                "\n" + toPrintForToString(allRows) +
                "\n}";
    }

    public String getPlantname() {
        return plantname;
    }
    public void setPlantname(String plantname) {
        this.plantname = plantname;
    }
    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public String getEncoding() {
        return encoding;
    }
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    public String getSomeattr() {
        return someattr;
    }
    public void setSomeattr(String someattr) {
        this.someattr = someattr;
    }
    public String getTablename() {
        return tablename;
    }
    public void setTablename(String tablename) {
        this.tablename = tablename;
    }
    public Boolean getAutoprocessed() {
        return autoprocessed;
    }
    public void setAutoprocessed(Boolean autoprocessed) {
        this.autoprocessed = autoprocessed;
    }
    public Boolean getSomeflag() {
        return someflag;
    }
    public void setSomeflag(Boolean someflag) {
        this.someflag = someflag;
    }
    public Timestamp getRecivedate() {
        return recivedate;
    }
    public void setRecivedate(Timestamp recivedate) {
        this.recivedate = recivedate;
    }
    public String[] getHeader() {
        return header;
    }
    public void setHeader(String[] header) {
        this.header = header;
    }

    public String[] getDataTypes() {
        return dataTypes;
    }
    public void setDataTypes(String[] dataTypes) {
        this.dataTypes = dataTypes;
    }
    public ArrayList<String[]> getAllRows() {
        return allRows;
    }
    public void setAllRows(ArrayList<String[]> allRows) {
        this.allRows = allRows;
    }

    private String toPrintForToString(ArrayList<String[]> allRows) {
        String ansver = "";
        for (String[] rows : allRows) {
            for (String a : rows) {
                ansver = ansver + a + " ";
            }
            ansver = ansver + "\t\t\t" + rows.length + "\n";
        }
        return ansver;
    }

    /**
     * Метод меняет непонятный для MySQL формат даты на понятный
     * пример: 03/15/2011 --> 2011/03/15
     */
    protected int changeDatesForMySQL () {
        for (String[] rows : allRows) {
            for (int i = 0; i < rows.length; i++) {
                if (header[i].equals("Date")){
                    rows[i] = DataTypeDefinition.dateToMySQLFormat(rows[i]);
                }
            }
        }
        return -1;
    }

    protected int writeToDb (){
        /**
         * Метод для записи подготовленного экземпляра
         *
         * / Запись в accra.recivedfiles
         * plantname='null'
         * filename='Alarm-20150216'
         * encoding='UTF-16'
         * someattr='null'
         * tablename='alarm210813123852'
         * autoprocessed=null
         * someflag=null
         * recivedate=2021-08-13 12:38:52.255
         * / Если не записалось, то авария.
         *
         * / Запись в персональную таблицу
         * String[]header, String[]dataTypes, ArrayList<String[]>allRows
         *
         * / Если записи не произошло (writeToDb = -1), то
         * дальнейший обработчик должен
         * записать autoprocessed = false;
         *
         */
        try{
            // writing to recivedfiles
            String requestToRecivedfiles = "INSERT INTO recivedfiles VALUES " +
                    "(null, ?, ?, ?, ?, 0, 0, null, ?);";
            System.out.println("plantname = " + plantname);
            System.out.println("filename = " + filename);
            System.out.println("encoding = " + encoding);
            System.out.println("recivedate = " + recivedate);
            System.out.println("tablename = " + tablename);
//            Boolean one, two;
            Connection conn = ConnectionMySQL.getConn();
            PreparedStatement st = conn.prepareStatement(requestToRecivedfiles);
            st.setString(1, plantname);
            st.setString(2,filename);
            st.setString(3,encoding);
            st.setTimestamp(4, recivedate);
            st.setString(5,tablename);

            st.executeUpdate();

            // make unique table
            String requestToMakeTable = "CREATE TABLE " + tablename + " (";
            for (int i = 0; i < dataTypes.length; i++) {
                requestToMakeTable += (header[i] + " " + DataTypesChangingForMySQL.usualToSQL(dataTypes[i]));
                if (dataTypes.length-i>1){requestToMakeTable += ", ";}
            }
            requestToMakeTable += ");";
            System.out.println(requestToMakeTable);
            st = conn.prepareStatement(requestToMakeTable);
            System.out.println(st.toString());

            st.executeUpdate();

            // write to unique table
            for (String[] lexemes: allRows ) {
                String requestRow = "INSERT INTO " + tablename + " VALUES (";
                for (int i = 0; i < dataTypes.length; i++) {
                    if(dataTypes[i].equals("Integer")
                            || dataTypes[i].equals("Float")
                            || dataTypes[i].equals("Boolean"))
                    {
                        requestRow += lexemes[i];
                    } else {
                        requestRow += "'" + lexemes[i] + "'";
                    }
                    if (dataTypes.length-i>1){requestRow += ", ";}
                }
                requestRow += ");";
                st = conn.prepareStatement(requestRow);
//                System.out.println(requestRow);
                st.executeUpdate();
            }
            // write flag AUTOPROCESSED to accra.recievedfiles
            String requestAutoprocessed = "UPDATE recivedfiles SET autoprocessed = 1 " +
                    "WHERE tablename = '" + tablename + "';";
            System.out.println(requestAutoprocessed);
            st = conn.prepareStatement(requestAutoprocessed);
            st.executeUpdate();


            st.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("***********************************/*/*/*/*/*/*////*");
            // ЗАПИШИ ЗДЕСЬ ФАЙЛ НА ДИСК В ПАПКУ НЕПАРСЕННЫХ ФАЙЛОВ !!!
            return -1;
        }
        return 0;
    }

}








