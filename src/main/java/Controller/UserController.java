package Controller;

import Model.User;
import Util.DTO.LoginCreds;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.Javalin;
import Service.UserService;

import java.sql.SQLException;

public class UserController {
    User sessionUser = null;
    private final UserService userService;
    private final Javalin app;

    public UserController(Javalin app, UserService userService){
        this.app = app;
        this.userService = userService;
    }

    public void userEndpoint(Javalin app) {
        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);
        app.delete("/logout", this::logoutUser);
    }

    private void postRegisterHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(ctx.body(), User.class);

        if (userService.emailExists(user.getEmail())) {
            ctx.status(409).result("Email already in use. Please choose another email.");
            return;
        }

        if (!userService.isValidEmail(user.getEmail())) {
            ctx.status(400).result("Invalid email format.");
            return;
        }

        if (!userService.isValidPassword(user.getPassword())) {
            ctx.status(400).result("Password must be at least 5 characters long and contain at least one number.");
            return;
        }

        if (sessionUser == null) {
            user = userService.registerUser(user);
            if (user != null) {
                ctx.status(201).json(user);
            } else {
                ctx.status(400).result("Registration failed. Check the provided data.");
            }
        } else {
            ctx.status(400).result("A user is already logged in. Please logout before registering a new user.");
        }
    }

    private void postLoginHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        LoginCreds loginCreds = mapper.readValue(ctx.body(), LoginCreds.class);

        if (!userService.isValidEmail(loginCreds.getEmail())) {
            ctx.status(400).result("Invalid email format.");
            return;
        }

        if (!userService.isValidPassword(loginCreds.getPassword())) {
            ctx.status(400).result("Password must be at least 5 characters long and contain at least one number.");
            return;
        }

        if (sessionUser == null) {
            User user = userService.loginUser(loginCreds);
            if (user != null) {
                sessionUser = user;
                ctx.status(200).json(user);
            } else {
                ctx.status(400).result("Login failed. Check the provided data.");
            }
        }
        else {
            ctx.status(400).result("A user is already logged in. Please logout before logging in a new user.");
        }
    }

    private void logoutUser(Context ctx) {
        if (sessionUser != null) {
            sessionUser = null;
            ctx.status(200).result("User logged out.");
        } else {
            ctx.status(400).result("No user is currently logged in.");
        }
    }

}
