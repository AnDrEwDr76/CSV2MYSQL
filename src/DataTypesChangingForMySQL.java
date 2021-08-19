public class DataTypesChangingForMySQL {
    public static String usualToSQL(String dType) {
        String str = "VARCHAR(15)";
        switch (dType) {
            case "Integer":
                str =  "INT";
                break;
            case "Float":
                str =  "FLOAT";
                break;
            case "Boolean":
                str =  "BIT(1)";
                break;
            case "Date":
                str =  "DATE";
                break;
            case "Time":
                str =  "TIME";
                break;
            default:
                str =  "VARCHAR(20)";
        }
        return str;
    }
}
