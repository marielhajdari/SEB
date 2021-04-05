package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;

    private static final String DB_URL = "jdbc:postgresql://localhost:5555/mariel";
    private static final String USER = "mariel";
    private static final String PASS = "postgres";

    public static DBConnection getInstance() {
        if (DBConnection.instance == null) {
            DBConnection.instance = new DBConnection();
        }
        return DBConnection.instance;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
        return null;
    }
}
