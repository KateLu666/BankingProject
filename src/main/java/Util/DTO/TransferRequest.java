package Util.DTO;

public class TransferRequest {
    private int fromUserId;
    private int toUserId;
    private double amount;

    public TransferRequest(int fromUserId, int toUserId, double amount) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
    }

    public TransferRequest() {
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public double getAmount() {
        return amount;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
