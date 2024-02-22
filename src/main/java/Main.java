import Controller.UserController;
import DAO.UserDAO;
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

        UserService userService = new UserService(userDAO);

        UserController userController = new UserController(app, userService);

        userController.userEndpoint(app);

    }
}