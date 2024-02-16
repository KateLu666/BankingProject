package Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserService {
    private Connection conn;

    public UserService(Connection conn) {
        this.conn = conn;
    }

    public void registerUser(String email, String password, String customerName) {
        if (!isValidPassword(password)) {
            System.out.println("Password must be at least 5 characters long and contain at least one number.");
            System.out.println("Please try again.ß");
            return;
        }

        if (!isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return;
        }

        String sqlUser = "INSERT INTO users (email, password, customer_name) VALUES (?, ?, ?)";
        try (PreparedStatement pstmtUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {
            pstmtUser.setString(1, email);
            pstmtUser.setString(2, password); // Simplify for the example. Hash in a real app.
            pstmtUser.setString(3, customerName);
            int affectedRows = pstmtUser.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("User registered successfully.");
                // Attempt to retrieve the generated user ID
                try (ResultSet generatedKeys = pstmtUser.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);
                        // Insert an associated account for this user with an initial balance
                        insertAccountForUser(userId);
                    } else {
                        System.out.println("Failed to retrieve user ID after registration.");
                    }
                }
            } else {
                System.out.println("User registration failed");
            }
        } catch (SQLException e) {
            System.out.println("Error registering user: " + e.getMessage());
        }
    }

    private void insertAccountForUser(int userId) {
        String sqlAccount = "INSERT INTO accounts (user_id, balance) VALUES (?, ?)";
        try (PreparedStatement pstmtAccount = conn.prepareStatement(sqlAccount)) {
            pstmtAccount.setInt(1, userId);
            pstmtAccount.setDouble(2, 0.0); // Set initial balance, e.g., 0.0
            pstmtAccount.executeUpdate();
            System.out.println("Account created successfully for user ID: " + userId);
        } catch (SQLException e) {
            System.out.println("Error creating account for user ID " + userId + ": " + e.getMessage());
        }
    }

    public int loginUser(String email, String password) {
        String sql = "SELECT user_id, password FROM users WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getString("password").equals(password)) {
                    return rs.getInt("user_id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return -1;
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 5) {
            return false;
        }
        boolean containsNumber = password.matches(".*\\d.*");
        return containsNumber;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}
