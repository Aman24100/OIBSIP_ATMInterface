import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Transaction {
    private String type;
    private double amount;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}

class User {
    private String userId;
    private String userPin;
    private double balance;
    private List<Transaction> transactionHistory;

    public User(String userId, String userPin) {
        this.userId = userId;
        this.userPin = userPin;
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public boolean isValidPin(String pin) {
        return userPin.equals(pin);
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add(new Transaction("Deposit", amount));
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transactionHistory.add(new Transaction("Withdraw", amount));
            return true;
        }
        return false;
    }

    public void transfer(User recipient, double amount) {
        if (withdraw(amount)) {
            recipient.deposit(amount);
            transactionHistory.add(new Transaction("Transfer to " + recipient.getUserId(), amount));
        }
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }
}

public class ATMInterface {
    private static final String ADMIN_USER_ID = "admin";
    private static final String ADMIN_PIN = "admin123";
    private static final String QUIT_OPTION = "5";

    private static Scanner scanner;
    private static User currentUser;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter User PIN: ");
        String pin = scanner.nextLine();

        if (userId.equals(ADMIN_USER_ID) && pin.equals(ADMIN_PIN)) {
            System.out.println("Admin login successful.");
            adminMenu();
        } else {
            currentUser = new User(userId, pin);
            if (authenticateUser()) {
                System.out.println("User login successful.");
                userMenu();
            } else {
                System.out.println("Invalid user ID or PIN. Exiting...");
            }
        }

        scanner.close();
    }

    private static boolean authenticateUser() {
        // Perform user authentication here
        // You can use a database or hard-coded values to validate the user ID and PIN
        // For simplicity, we'll assume the user is authenticated if not an admin
        return !currentUser.getUserId().equals(ADMIN_USER_ID);
    }

    private static void adminMenu() {
        String option;
        do {
            displayAdminMenu();
            option = scanner.nextLine();
            switch (option) {
                case "1":
                    viewTransactionHistory();
                    break;
                case "2":
                    System.out.print("Enter User ID: ");
                    String userId = scanner.nextLine();
                    User user = findUserById(userId);
                    if (user != null) {
                        viewTransactionHistory(user);
                    } else {
                        System.out.println("User not found.");
                    }
                    break;
                case QUIT_OPTION:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (!option.equals(QUIT_OPTION));
    }

    private static void userMenu() {
        String option;
        do {
            displayUserMenu();
            option = scanner.nextLine();
            switch (option) {
                case "1":
                    viewTransactionHistory(currentUser);
                    break;
                case "2":
                    withdraw();
                    break;
                case "3":
                    deposit();
                    break;
                case "4":
                    transfer();
                    break;
                case QUIT_OPTION:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (!option.equals(QUIT_OPTION));
    }

    private static void viewTransactionHistory() {
        System.out.println("Transaction History (All Users):");
        for (User user : getAllUsers()) {
            viewTransactionHistory(user);
        }
    }

    private static void viewTransactionHistory(User user) {
        System.out.println("Transaction History for User ID: " + user.getUserId());
        List<Transaction> transactions = user.getTransactionHistory();
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction transaction : transactions) {
                System.out.println(transaction.getType() + ": " + transaction.getAmount());
            }
        }
    }

    private static void withdraw() {
        System.out.print("Enter amount to withdraw: ");
        double amount = Double.parseDouble(scanner.nextLine());
        if (currentUser.withdraw(amount)) {
            System.out.println("Withdrawal successful.");
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    private static void deposit() {
        System.out.print("Enter amount to deposit: ");
        double amount = Double.parseDouble(scanner.nextLine());
        currentUser.deposit(amount);
        System.out.println("Deposit successful.");
    }

    private static void transfer() {
        System.out.print("Enter recipient's User ID: ");
        String userId = scanner.nextLine();
        User recipient = findUserById(userId);
        if (recipient != null) {
            System.out.print("Enter amount to transfer: ");
            double amount = Double.parseDouble(scanner.nextLine());
            currentUser.transfer(recipient, amount);
            System.out.println("Transfer successful.");
        } else {
            System.out.println("Recipient not found.");
        }
    }

    private static User findUserById(String userId) {
        // Perform user lookup by ID here
        // You can use a database or a list of pre-defined users
        // For simplicity, we'll use a hard-coded user for demo purposes
        if (userId.equals("john")) {
            return new User("john", "1234");
        }
        return null;
    }

    private static List<User> getAllUsers() {
        // Retrieve all users from a database or other data source
        // For simplicity, we'll use a list of hard-coded users for demo purposes
        List<User> users = new ArrayList<>();
        users.add(new User("john", "1234"));
        users.add(new User("emma", "5678"));
        return users;
    }

    private static void displayAdminMenu() {
        System.out.println("\nAdmin Menu:");
        System.out.println("1. View Transaction History (All Users)");
        System.out.println("2. View Transaction History (Specific User)");
        System.out.println(QUIT_OPTION + ". Quit");
        System.out.print("Enter option: ");
    }

    private static void displayUserMenu() {
        System.out.println("\nUser Menu:");
        System.out.println("1. View Transaction History");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Transfer");
        System.out.println(QUIT_OPTION + ". Quit");
        System.out.print("Enter option: ");
    }
}

