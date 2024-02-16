package Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountService {
    private Connection conn;

    public AccountService(Connection conn) {
        this.conn = conn;
    }

    public double getBalance(int userId) {
        String sql = "SELECT balance FROM accounts WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            System.out.println("Error getting balance: " + e.getMessage());
        }
        return 0.0;
    }

    public void deposit(int userId, double amount) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive.");
            return;
        }
        String sql = "UPDATE accounts SET balance = balance + ? WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, userId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                double newBalance = getBalance(userId);
                System.out.println("Deposit successful. Current balance: " + newBalance);
            } else {
                System.out.println("Deposit failed.");
            }
        } catch (SQLException e) {
            System.out.println("Error during deposit: " + e.getMessage());
        }
    }

    public void withdraw(int userId, double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return;
        }
        double balance = getBalance(userId);
        if (balance < amount) {
            System.out.println("Insufficient funds.");
            return;
        }
        String sql = "UPDATE accounts SET balance = balance - ? WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, userId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                double newBalance = getBalance(userId);
                System.out.println("Withdrawal successful. Current balance: " + newBalance);
            } else {
                System.out.println("Withdrawal failed.");
            }
        } catch (SQLException e) {
            System.out.println("Error during withdrawal: " + e.getMessage());
        }
    }
}
