package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private final AccountDAO accountDAO;

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account checkBalance(int userId) {
        return accountDAO.getBalanceById(userId);
    }

    public Account deposit(int userId, double amount) {
        return accountDAO.depositByUserId(userId, amount);
    }

    public Account withdraw(int userId, double amount) {
        Account account = checkBalance(userId);

        if (account.getBalance() < amount) {
            System.out.println("Insufficient funds.");
            return account;
        } else {
            return accountDAO.withdrawByUserId(userId, amount);
        }
    }

    public Account transfer(int fromUserId, int toUserId, double amount) {
        Account fromAccount = checkBalance(fromUserId);
        Account toAccount = checkBalance(toUserId);

        if (fromAccount.getBalance() < amount) {
            System.out.println("Insufficient funds.");
            return fromAccount;
        } else {
            accountDAO.withdrawByUserId(fromUserId, amount);
            accountDAO.depositByUserId(toUserId, amount);
            return checkBalance(fromUserId);
        }
    }

}
