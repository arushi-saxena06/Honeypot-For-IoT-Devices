import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainServer {
    public static void main(String[] args) {
        System.out.println("Starting Honeypot Backend...");

        // Start HTTP and Telnet honeypots
        new Thread(() -> new HttpHoneypot(8080).start()).start();
        new Thread(() -> new TelnetHoneypot(2323).start()).start();

        // Test DB connection
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/honeypotdb", "root", "root")) {
            System.out.println("✅ Connected to MySQL successfully!");
        } catch (SQLException e) {
            System.out.println("⚠️ MySQL connection failed: " + e.getMessage());
        }

        System.out.println("Backend running...");
    }
}
