package DAO;

import Model.Account;

import Util.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class AccountDAO {
    public Account getBalanceById(int userId) {
        String sql = "SELECT * FROM accounts WHERE userId = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Account(rs.getInt("accountId"), userId, rs.getDouble("balance"));
            }
        } catch (SQLException e) {
            System.out.println("Error getting account: " + e.getMessage());
        }
        return null;
    }

    public Account depositByUserId(int userId, double amount) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive.");
            return null;
        }
        String sqlUpdate = "UPDATE accounts SET balance = balance + ? WHERE userId = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, userId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                return getBalanceById(userId);
            }
        } catch (SQLException e) {
            System.out.println("Error during deposit: " + e.getMessage());
        }
        return null;
    }

    public Account withdrawByUserId(int userId, double amount) {
        Account account = getBalanceById(userId);
        if (account == null || account.getBalance() < amount) {
            System.out.println("Insufficient funds or account not found.");
            return account;
        }
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return null;
        }

        String sqlUpdate = "UPDATE accounts SET balance = balance - ? WHERE userId = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, userId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                return getBalanceById(userId);
            }
        } catch (SQLException e) {
            System.out.println("Error during withdrawal: " + e.getMessage());
        }
        return null;
    }

    public Account transferByUserId(int fromUserId, int toUserId, double amount) {
        Account fromAccount = getBalanceById(fromUserId);
        Account toAccount = getBalanceById(toUserId);
        if (fromAccount == null || toAccount == null || fromAccount.getBalance() < amount) {
            System.out.println("Insufficient funds or account not found.");
            return fromAccount;
        }
        if (amount <= 0) {
            System.out.println("Transfer amount must be positive.");
            return null;
        }

        String sqlUpdateFrom = "UPDATE accounts SET balance = balance - ? WHERE userId = ?";
        String sqlUpdateTo = "UPDATE accounts SET balance = balance + ? WHERE userId = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmtFrom = conn.prepareStatement(sqlUpdateFrom);
             PreparedStatement pstmtTo = conn.prepareStatement(sqlUpdateTo)) {
            conn.setAutoCommit(false);
            pstmtFrom.setDouble(1, amount);
            pstmtFrom.setInt(2, fromUserId);
            pstmtTo.setDouble(1, amount);
            pstmtTo.setInt(2, toUserId);
            int affectedRowsFrom = pstmtFrom.executeUpdate();
            int affectedRowsTo = pstmtTo.executeUpdate();
            if (affectedRowsFrom > 0 && affectedRowsTo > 0) {
                conn.commit();
                return getBalanceById(fromUserId);
            }
        } catch (SQLException e) {
            System.out.println("Error during transfer: " + e.getMessage());
        }
        return null;
    }

}
