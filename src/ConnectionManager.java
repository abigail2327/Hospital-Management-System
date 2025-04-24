// Group 9 - Abigail Da Costa (772001345), Praislin Peter (771003933), Kyaw Thu Hein (396006747) - April 20, 2025
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String DB_URL = "jdbc:mariadb://localhost:33061/Hospital"; // change the port number
    private static final String DB_USER = "root";  // change with your user name
    private static final String DB_PASSWORD = "asd123"; // Replace with actual password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    public static void disconnect(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed successfully.");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
