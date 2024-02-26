package Service;

import DAO.UserDAO;
import Model.User;
import Util.DTO.LoginCreds;

public class UserService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User registerUser(User user) {
        User registeredUser = userDAO.registerUser(user.getEmail(), user.getPassword(), user.getCustomerName());
        if (registeredUser != null) {
            System.out.println("User registered successfully for email: " + user.getEmail());
            return registeredUser;
        } else {
            System.out.println("User registration failed for email: " + user.getEmail());
            return null;
        }
    }

    public User loginUser(LoginCreds loginCreds) {
        User loggedInUser = userDAO.loginUser(loginCreds.getEmail(), loginCreds.getPassword());
        if (loggedInUser != null) {
            System.out.println("User logged in successfully for email: " + loginCreds.getEmail());
            return loggedInUser;
        } else {
            System.out.println("User login failed for email: " + loginCreds.getEmail());
            return null;
        }
    }

    public boolean emailExists(String email) {
        return userDAO.emailExists(email);
    }

    public boolean isValidPassword(String password) {
        return password.length() >= 5 && password.matches(".*\\d.*");
    }

    public boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

}
