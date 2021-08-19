import java.io.*;

public class test {

    /*
     * Make sure 0xFEFF is encoded as this byte sequence: EF BB BF, when
     * UTF-8 is being used, and parsed back into 0xFEFF.
     */
    public static void main(String[] args) throws Exception {

        /*
         * Write
         */
        FileOutputStream fos = new FileOutputStream("bom.txt");
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-16");
        osw.write(0xFFFE);

        osw.close();

        /*
         * Parse
         */
        FileInputStream fis = new FileInputStream("bom.txt");
        InputStreamReader isr = new InputStreamReader(fis, "ISO-8859-1");
        char bomChar = (char) isr.read();
        char bomChar2 = (char) isr.read();

        System.out.println("Parsed: "
                + Integer.toHexString(bomChar).toUpperCase()
                +Integer.toHexString(bomChar2).toUpperCase());
        if (bomChar != 0xFEFF) {
            throw new Exception("Invalid BOM: "
                    + Integer.toHexString(bomChar).toUpperCase());
        }
        isr.close();

    }

}