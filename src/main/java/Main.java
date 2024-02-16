import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import Service.AccountService;
import Service.UserService;

public class Main {
    public static void main(String[] args) {

        String url = "jdbc:mysql://database-1.cdwq0eawwfkf.us-east-1.rds.amazonaws.com:3306/BankingAppDb";
        String username = "admin";
        String password = "adminpassword";

        try (Connection conn = DriverManager.getConnection(url,username, password)) {
//            System.out.println("Connected to the database successfully.");

            Statement stmt = conn.createStatement();
            Scanner scanner = new Scanner(System.in);

            String createUserTable =
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "  user_id INT NOT NULL AUTO_INCREMENT, " +
                            "  email VARCHAR(255) NOT NULL UNIQUE, " +
                            "  password VARCHAR(255) NOT NULL, " +
                            "  customer_name VARCHAR(255) NOT NULL, " +
                            "  PRIMARY KEY (user_id)" +
                            ")";

            String createAccountTable =
                    "CREATE TABLE IF NOT EXISTS accounts (" +
                    "account_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT, " +
                    "balance DECIMAL(10, 2) NOT NULL, " +
                    "FOREIGN KEY (user_id) REFERENCES users(user_id)" + ");";

            stmt.execute(createUserTable);
            stmt.execute(createAccountTable);

            UserService userService = new UserService(conn);
            AccountService accountService = new AccountService(conn);

            while (true) {
                System.out.println("\n***********  Welcome to Bank of Kate!  ************\n");
                System.out.println("What would you like to do today?");
                System.out.println("1) Register");
                System.out.println("2) Login");
                System.out.println("3) Exit");
                System.out.println("Enter your choice: ");

                int welcomeChoice = scanner.nextInt();
                scanner.nextLine();

                switch (welcomeChoice) {
                    case 1:
                        System.out.print("Email: ");
                        String emailReg = scanner.nextLine();
                        System.out.print("Password: ");
                        String passwordReg = scanner.nextLine();
                        System.out.print("Customer Name: ");
                        String nameReg = scanner.nextLine();
                        userService.registerUser(emailReg, passwordReg, nameReg);
                        break;
                    case 2:
                        System.out.print("Email: ");
                        String email = scanner.nextLine();
                        System.out.print("Password: ");
                        String passwordLogin = scanner.nextLine();
                        int userId = userService.loginUser(email, passwordLogin);
                        if (userId != -1) {
                            System.out.println("Login successful.");
                            boolean sessionActive = true;
                            while (sessionActive) {
                                System.out.println("1. View Balance");
                                System.out.println("2. Deposit");
                                System.out.println("3. Withdraw");
                                System.out.println("4. Logout");
                                System.out.print("Select an option: ");
                                int userChoice = scanner.nextInt();
                                scanner.nextLine(); // Consume newline

                                switch (userChoice) {
                                    case 1:
                                        System.out.println("Your balance is: " + accountService.getBalance(userId));
                                        break;
                                    case 2:
                                        System.out.print("Amount to deposit: ");
                                        double depositAmount = scanner.nextDouble();
                                        accountService.deposit(userId, depositAmount);
                                        break;
                                    case 3:
                                        System.out.print("Amount to withdraw: ");
                                        double withdrawAmount = scanner.nextDouble();
                                        accountService.withdraw(userId, withdrawAmount);
                                        break;
                                    case 4:
                                        sessionActive = false;
                                        System.out.println("Logged out.");
                                        break;
                                    default:
                                        System.out.println("Invalid option.");
                                        break;
                                }
                            }
                        } else {
                            System.out.println("Login failed.");
                        }
                        break;
                    case 3:
                        System.out.println("\nExiting Bank of Kate. Goodbye!");
                        return; // Use return instead of System.exit(0) for a cleaner exit
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
        }
    }
}