import java.sql.Connection;
import java.sql.DriverManager;

/**
 * DBUtil: simple utility class to obtain a JDBC connection.
 * Update USER/PASS to match your local MySQL credentials.
 */
public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/pantry_tracker";
    private static final String USER = "root"; // <-- change this
    private static final String PASS = "password"; // <-- change this

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
