import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMySQL {
    public static Connection getConn() throws SQLException {
        String hostName = "localhost";
        String dbName = "accra";
        Integer portNumber = 3306;
        String port = portNumber.toString();
        String connectionURL = "jdbc:mysql://" + hostName + ":" + port + "/" + dbName;
        String user = "root";
        String password = "zxYP01!2";
        DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        return DriverManager.getConnection(connectionURL, user, password);
    }
}
