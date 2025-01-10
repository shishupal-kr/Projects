package RukkhiBank.main;

import RukkhiBank.models.BankAccount;
import RukkhiBank.services.TransactionManager;
import RukkhiBank.storage.RukkhiBankJdbc;
import java.util.Scanner;
import static RukkhiBank.services.Security.verifyAdmin;

public class RukkhiBankApp {
    private static final Scanner sc = new Scanner(System.in);

    // Create Account
    private static void createAccount() {
        sc.nextLine(); // Consume leftover newline
        System.out.println("Enter Account Holder Name: ");
        String accountHolderName = sc.nextLine();

        System.out.println("Choose Account Type:");
        System.out.println("1. Savings");
        System.out.println("2. Current");
        System.out.print("Enter your choice (1 or 2): ");
        String accountType;
        int choice = sc.nextInt();
        sc.nextLine();
        accountType = (choice == 1) ? "Savings" : (choice == 2) ? "Current" : "Savings";

        System.out.println("Enter Account Number: ");
        String accountNumber = sc.nextLine();
        System.out.println("Enter Email: ");
        String email = sc.nextLine();
        System.out.println("Enter Initial Amount To Deposit: ");
        double initialBalance = sc.nextDouble();

        BankAccount account = new BankAccount(accountHolderName, accountNumber, accountType, email, initialBalance);

        if (RukkhiBankJdbc.insertAccount(account)) {
            System.out.println("Account created successfully!");
        } else {
            System.out.println("Failed to create account.");
        }
    }

    private static void deposit() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Account Number: ");
        String accountNumber = sc.nextLine();
        System.out.println("Enter Amount to Deposit: ");
        double amount = sc.nextDouble();


        // Delegate the deposit operation to TransactionManager
        if (!TransactionManager.deposit(accountNumber, amount)) {
            System.out.println("Deposit failed. Please try again.");
        }
    }

    private static void withdraw() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Account Number: ");
        String accountNumber = sc.nextLine();
        System.out.println("Enter Amount to Withdraw: ");
        double amount = sc.nextDouble();

        // Delegate the withdrawal operation to TransactionManager
        if (!TransactionManager.withdraw(accountNumber, amount)) {
            System.out.println("Withdrawal failed. Please try again.");
        }
    }

    // View Balance
    private static void viewBalance() {
        sc.nextLine();
        System.out.println("Enter Account Number: ");
        String accountNumber = sc.nextLine();

        BankAccount account = RukkhiBankJdbc.getAccount(accountNumber);

        if (account == null) {
            System.out.println("Account not found!");
        } else {
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Account Holder Name: "+ account.getAccountHolderName());
            System.out.println("Balance: ₹" + account.getBalance());
        }
    }

    // Delete Account
    private static void deleteAccount() {
        // Verify admin password before deleting
        if (!verifyAdmin()) {
            System.out.println("Invalid admin password. Deletion aborted.");
            return;
        }
        sc.nextLine();
        System.out.println("Enter Account Number to Delete: ");
        String accountNumber = sc.nextLine();

        if (RukkhiBankJdbc.deleteAccount(accountNumber)) {
            System.out.println("Account deleted successfully!");
        } else {
            System.out.println("Failed to delete account or account not found.");
        }
    }
    private static void Exit() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Press any key to exit...");

        // Waiting for user input before exiting
        sc.nextLine();  // This reads the next line of input to simulate a "Press any key to exit"

        System.out.println("Exiting the Banking System... Goodbye!");
        System.exit(0);  // This will terminate the program
    }

    private static void transferFunds() {
        sc.nextLine(); // Consume newline
        System.out.println("Enter Your Account Number: ");
        String fromAccount = sc.nextLine();

        System.out.println("Enter Recipient's Account Number: ");
        String toAccount = sc.nextLine();

        System.out.println("Enter Amount to Transfer: ");
        double amount = sc.nextDouble();

        // Call the TransactionManager's transferFunds method
        if (!TransactionManager.transferFunds(fromAccount, toAccount, amount)) {
            System.out.println("Fund transfer failed. Please check account details or balance.");
        }
    }

    // Main Menu
    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\n--- Rukkhi Bank ---");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. View Balance");
            System.out.println("5. Delete Account");
            System.out.println("6. Exit");
            System.out.println("7. Transfer Funds");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1 -> createAccount();
                case 2 -> deposit();
                case 3 -> withdraw();
                case 4 -> viewBalance();
                case 5 -> deleteAccount();
                case 6 -> Exit();
                case 7 -> transferFunds();
                case 99 -> {
                    if (verifyAdmin()) {
                        RukkhiBankJdbc.fetchAccounts();
                    } else {
                        System.out.println("Unauthorized access attempt.");
                    }
                }
                case 100 -> {
                    System.out.println("Thank you for using Rukkhi Bank. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}