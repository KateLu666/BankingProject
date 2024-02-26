import Controller.AccountController;
import Controller.UserController;
import DAO.AccountDAO;
import DAO.UserDAO;
import Service.AccountService;
import io.javalin.Javalin;
import Service.UserService;


public class Main {
    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(it -> {
                    it.anyHost();
                    it.exposeHeader("Authorization");
                });
            });
        }).start(8080);

        UserDAO userDAO = new UserDAO();
//        AccountDAO accountDAO = new AccountDAO();

        UserService userService = new UserService(userDAO);
//        AccountService accountService = new AccountService(accountDAO);

        UserController userController = new UserController(app, userService);
//        AccountController accountController = new AccountController(app, accountService);

        userController.userEndpoint(app);
//        accountController.accountEndpoint(app);

    }
}