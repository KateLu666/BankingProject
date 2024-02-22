package Controller;

import Model.User;
import io.javalin.http.Context;
import io.javalin.Javalin;
import Service.UserService;

public class UserController {
    User sessionUser = null;
    private final UserService userService;
    Javalin app;

    public UserController(Javalin app, UserService userService){
        this.app = app;
        this.userService = userService;
    }

    public void userEndpoint(Javalin app) {
        app.post("/register", this::registerUser);
        app.post("/login", this::loginUser);
    }

    private void registerUser(Context ctx) {
        User user = ctx.bodyAsClass(User.class);

        if (user.getEmail() == null || user.getPassword() == null || user.getCustomerName() == null) {
            ctx.status(400).result("Missing user information.");
            return;
        }

        boolean registrationSuccess = userService.registerUser(user.getEmail(), user.getPassword(), user.getCustomerName());
        if (registrationSuccess) {
            ctx.status(201).result("User registered successfully.");
        } else {
            ctx.status(400).result("Registration failed. Check the provided data.");
        }
    }

    private void loginUser(Context ctx) {
        User loginUser = ctx.bodyAsClass(User.class);

        if (loginUser.getEmail() == null || loginUser.getPassword() == null) {
            ctx.status(400).result("Missing login information.");
            return;
        }

        boolean loginSuccess = userService.loginUser(loginUser.getEmail(), loginUser.getPassword());
        if (loginSuccess) {
            ctx.status(200).result("Login successful.");
        } else {
            ctx.status(401).result("Login failed. Check your credentials.");
        }
    }
}
