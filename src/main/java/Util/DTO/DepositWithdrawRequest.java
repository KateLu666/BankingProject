package Util.DTO;

public class DepositWithdrawRequest {
    private double amount;

    public DepositWithdrawRequest(double amount) {
        this.amount = amount;
    }

    public DepositWithdrawRequest() {
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
