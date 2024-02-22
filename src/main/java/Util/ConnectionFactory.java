package Util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    private static final ConnectionFactory instance = new ConnectionFactory();
    private Properties properties = new Properties();

    private ConnectionFactory() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find db.properties");
            }
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Error loading db.properties", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    instance.properties.getProperty("db.url"),
                    instance.properties.getProperty("db.user"),
                    instance.properties.getProperty("db.password"));
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }
}
