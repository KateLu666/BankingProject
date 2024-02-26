package Util.DTO;

public class LoginCreds {
    private String email;
    private String password;

    public LoginCreds() {
    }

    public LoginCreds(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
