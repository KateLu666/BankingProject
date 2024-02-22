package Service;

import DAO.UserDAO;

public class UserService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean registerUser(String email, String password, String customerName) {
        if (!isValidPassword(password)) {
            System.out.println("Password must be at least 5 characters long and contain at least one number.");
            return false;
        }

        if (!isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return false;
        }

       boolean registrationSuccess = userDAO.registerUser(email, password, customerName);
        if (registrationSuccess) {
            System.out.println("User registered successfully for email: " + email);
            return true;
        } else {
            System.out.println("User registration failed for email: " + email);
            return false;
        }
    }

    public boolean loginUser(String email, String password) {
        boolean loginSuccess = userDAO.loginUser(email, password);
        if (loginSuccess) {
            System.out.println("Login successful for email: " + email);
            return true;
        } else {
            System.out.println("Login failed for email: " + email);
            return false;
        }
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 5 && password.matches(".*\\d.*");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }
}