package Controller;

import Model.Account;
import Service.AccountService;
import Util.DTO.DepositWithdrawRequest;
import Util.DTO.TransferRequest;
import io.javalin.http.Context;
import io.javalin.Javalin;


public class AccountController {
    private final AccountService accountService;
    private final Javalin app;

    public AccountController(Javalin app, AccountService accountService) {
        this.app = app;
        this.accountService = accountService;
    }

    public void accountEndpoint(Javalin app) {
        app.get("/accounts/{userId}/balance", this::getBalanceHandler);
        app.post("/accounts/{userId}/deposit", this::postDepositHandler);
        app.post("/accounts/{userId}/withdraw", this::postWithdrawHandler);
        app.post("/accounts/{userId}/transfer", this::postTransferHandler);
    }

    private void getBalanceHandler(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("userId"));
        Account account = accountService.checkBalance(userId);
        if (account != null) {
            ctx.json(account);
        } else {
            ctx.status(404).result("Account not found.");
        }
    }

    private void postDepositHandler(Context ctx) {
        try {
            int userId = Integer.parseInt(ctx.pathParam("userId"));
            DepositWithdrawRequest depositRequest = ctx.bodyAsClass(DepositWithdrawRequest.class);

            if (depositRequest.getAmount() <= 0) {
                ctx.status(400).result("Invalid deposit amount. Amount must be positive.");
                return;
            }

            Account account = accountService.deposit(userId, depositRequest.getAmount());
            if (account != null) {
                ctx.json(account);
            } else {
                ctx.status(400).result("Deposit failed.");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid user ID format.");
        } catch (Exception e) {
            ctx.status(500).result("Internal server error: " + e.getMessage());
        }
    }


    private void postWithdrawHandler(Context ctx) {
        try {
            int userId = Integer.parseInt(ctx.pathParam("userId"));
            DepositWithdrawRequest withdrawRequest = ctx.bodyAsClass(DepositWithdrawRequest.class);

            if (withdrawRequest.getAmount() <= 0) {
                ctx.status(400).result("Invalid withdrawal amount. Amount must be positive.");
                return;
            }

            Account account = accountService.withdraw(userId, withdrawRequest.getAmount());
            if (account != null) {
                ctx.json(account);
            } else {
                ctx.status(400).result("Withdrawal failed. Insufficient funds or account not found.");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid user ID format.");
        } catch (Exception e) {
            ctx.status(500).result("Internal server error: " + e.getMessage());
        }
    }


    private void postTransferHandler(Context ctx) {
        try {
            int fromUserId = Integer.parseInt(ctx.pathParam("userId"));

            TransferRequest transferRequest = ctx.bodyAsClass(TransferRequest.class);

            if (transferRequest.getAmount() <= 0) {
                ctx.status(400).result("Transfer amount must be positive.");
                return;
            }

            Account fromAccount = accountService.transfer(
                    transferRequest.getFromUserId(),
                    transferRequest.getToUserId(),
                    transferRequest.getAmount()
            );

            if (fromAccount != null) {
                ctx.json(fromAccount);
            } else {
                ctx.status(400).result("Transfer failed. Check account details or balance.");
            }
        } catch (Exception e) {
            ctx.status(500).result("Internal server error: " + e.getMessage());
        }
    }
}
