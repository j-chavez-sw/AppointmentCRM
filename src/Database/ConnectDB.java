package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.Instant;
import java.io.IOException;
import java.sql.SQLException;


public class ConnectDB {

    private static final String databaseName = "U0621z";
    private static final String DB_URL = "jdbc:mysql://3.227.166.251/" + databaseName;
    private static final String username = "U0621z";
    private static final String password = "53688672381";
    private static final String driver = "com.mysql.jdbc.Driver";
    public static Connection conn;

    public static Connection makeConnection() throws ClassNotFoundException, SQLException{
        Class.forName(driver);
        conn = (Connection) DriverManager.getConnection(DB_URL, username, password);
        return conn;
    }

    public static void closeConnection() throws SQLException{
        conn.close();
    }
}