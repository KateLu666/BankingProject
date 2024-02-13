package Model;

public class User {
    private int userId;
    private String email;
    private String password;
    private String customerName;

    // Constructor
    public User(int userId, String email, String password, String customerName) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.customerName = customerName;
    }

    // Getter and Setter for userId
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and Setter for customerName
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
