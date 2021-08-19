import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MakeDbToDelete {
    public static void main (String [] args) {
        try{
            Connection conn = ConnectionMySQL.getConn();
/*
            String requestToDb1 = "CREATE DATABASE accra;";
*/
            String requestToDb1 = "UPDATE recivedfiles SET autoprocessed = 1 WHERE tablename = 'working210818213801';";
/*            String requestToDb2 =
                    "CREATE TABLE accra.customer" +
                    "(" +
                    "customer VARCHAR(20) PRIMARY KEY," +
                    "firstname VARCHAR(20)," +
                    "lastname VARCHAR(20)," +
                    "position VARCHAR(20)," +
                    "phonenumber VARCHAR(20)" +
                    ");";
            String requestToDb3 =
                    "CREATE TABLE accra.ipaddress" +
                    "(" +
                    "customer VARCHAR(20) PRIMARY KEY," +
                    "ipv4 INT," +
                    "ipv6  VARCHAR(45)," +
                    "FOREIGN KEY (customer) REFERENCES customer (customer)" +
                    "ON DELETE CASCADE" +
                    ");";
            String requestToDb4 =
                    "CREATE TABLE accra.plant" +
                    "(" +
                    "pid INT," +
                    "plantname VARCHAR(20) PRIMARY KEY," +
                    "customer VARCHAR(20)," +
                    "address VARCHAR(20)," +
                    "FOREIGN KEY (customer) REFERENCES customer (customer)" +
                    "ON DELETE CASCADE" +
                    ");";
            String requestToDb5 =
                    "CREATE TABLE accra.recivedfiles" +
                    "(" +
                    "id INT NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                    "plantname VARCHAR(20)," +
                    "filename VARCHAR(25)," +
                    "encoding VARCHAR(20)," +
                    "recivedate DATETIME," +
                    "autoprocessed BIT(1)," +
                    "someflag BIT(1)," +
                    "someattr VARCHAR(20)," +
                    "tablename VARCHAR(25) NOT NULL," +
                    "FOREIGN KEY (plantname) REFERENCES plant (plantname)" +
                    "ON DELETE CASCADE" +
                    ");";*/
            Statement st = null;
            st = conn.createStatement();
            ResultSet rs = null;
            st.executeUpdate(requestToDb1);
/*            st.executeUpdate(requestToDb2);
            st.executeUpdate(requestToDb3);
            st.executeUpdate(requestToDb4);
            st.executeUpdate(requestToDb5);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
