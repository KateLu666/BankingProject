package DAO;

import Model.User;
import Util.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {
    public User registerUser(String email, String password, String customerName) {
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            String sqlUser = "INSERT INTO users (email, password, customerName) VALUES (?, ?, ?)";
            try (PreparedStatement pstmtUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {
                pstmtUser.setString(1, email);
                pstmtUser.setString(2, password);
                pstmtUser.setString(3, customerName);
                int affectedRows = pstmtUser.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                long userId = -1;
                try (ResultSet generatedKeys = pstmtUser.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        userId = generatedKeys.getLong(1);
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }

                String sqlAccount = "INSERT INTO accounts (userId, balance) VALUES (?, ?)";
                try (PreparedStatement pstmtAccount = conn.prepareStatement(sqlAccount)) {
                    pstmtAccount.setLong(1, userId);
                    pstmtAccount.setDouble(2, 0.0);
                    pstmtAccount.executeUpdate();
                }
            }

            conn.commit();
            return new User(email, customerName);
        } catch (SQLException ex) {
            System.out.println("Error registering user: " + ex.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e) {
                    System.out.println("Error rolling back: " + e.getMessage());
                }
            }
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    System.out.println("Error closing connection: " + ex.getMessage());
                }
            }
        }
    }

    public User loginUser(String email, String password) {
        try (Connection conn = ConnectionFactory.getConnection()) {
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, email);
                pstmt.setString(2, password);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String customerName = rs.getString("customerName");
                        return new User(email, customerName);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error logging in user: " + ex.getMessage());
            return null;
        }
        return null;
    }

    public boolean emailExists(String email) {
        try (Connection conn = ConnectionFactory.getConnection()) {
            String sql = "SELECT * FROM users WHERE email = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, email);
                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error checking if email exists: " + ex.getMessage());
            return false;
        }
    }
}
