package Model;

public class Account {
    private int accountId;
    private int userId;
    private double balance;

    // Constructor
    public Account(int accountId, int userId, double balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    // Getter and Setter for accountId
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    // Getter and Setter for userId
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Getter and Setter for balance
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
