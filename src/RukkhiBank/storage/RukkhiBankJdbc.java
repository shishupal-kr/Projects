package RukkhiBank.storage;

import RukkhiBank.models.BankAccount;
import java.sql.*;

public class RukkhiBankJdbc {

    private static final String url = "jdbc:mysql://127.0.0.1:3306/RukkhiBank";  // Your database URL
    private static final String username = "rukkhibank"; // Your database username
    private static final String password = "bankofrukkhi"; // Your database password

    // Method to insert a new account into the database
    public static boolean insertAccount(BankAccount account) {
        String query = "INSERT INTO BankAccount (accountHolderName, accountNumber, accountType, email, balance) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, account.getAccountHolderName());
            statement.setString(2, account.getAccountNumber());
            statement.setString(3, account.getAccountType());
            statement.setString(4, account.getEmail());
            statement.setDouble(5, account.getBalance());

            return statement.executeUpdate() > 0; // Return true if the record is inserted
        } catch (SQLException e) {
            System.err.println("Error while inserting account: " + e.getMessage());
        }
        return false;
    }

    // Method to fetch an account by account number
    public static BankAccount getAccount(String accountNumber) {
        String query = "SELECT * FROM BankAccount WHERE accountNumber = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, accountNumber);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return new BankAccount(
                        result.getString("accountHolderName"),
                        result.getString("accountNumber"),
                        result.getString("accountType"),
                        result.getString("email"),
                        result.getDouble("balance")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error while fetching account: " + e.getMessage());
        }
        return null; // Return null if the account is not found
    }

    // Update account balance in the database
    public static boolean updateBalance(BankAccount account) {
        String query = "UPDATE BankAccount SET balance = ? WHERE accountNumber = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDouble(1, account.getBalance());
            statement.setString(2, account.getAccountNumber());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error while updating balance: " + e.getMessage());
            return false;
        }
    }

    // Method to delete an account by account number
    public static boolean deleteAccount(String accountNumber) {
        String query = "DELETE FROM BankAccount WHERE accountNumber = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, accountNumber);

            return statement.executeUpdate() > 0; // Return true if the record is deleted
        } catch (SQLException e) {
            System.err.println("Error while deleting account: " + e.getMessage());
        }
        return false;
    }
    public static void fetchAccounts() {
        String query = "SELECT * FROM BankAccount";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {

            System.out.println("\n--Details of Accounts in Rukkhi Bank--");
            while (result.next()) {
                String accountNumber = result.getString("accountNumber");
                String accountHolderName = result.getString("accountHolderName");
                String accountType = result.getString("accountType");
                String email = result.getString("email");
                double balance = result.getDouble("balance");

                System.out.println("\nAccount Number: " + accountNumber);
                System.out.println("Account Holder Name: " + accountHolderName);
                System.out.println("Account Type: " + accountType);
                System.out.println("Email: " + email);
                System.out.println("Balance: ₹" + balance);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching accounts: " + e.getMessage());
        }
    }
    public static boolean transferFunds(String fromAccountNumber, String toAccountNumber, double amount) {
        String debitQuery = "UPDATE BankAccount SET balance = balance - ? WHERE accountNumber = ? AND balance >= ?";
        String creditQuery = "UPDATE BankAccount SET balance = balance + ? WHERE accountNumber = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            connection.setAutoCommit(false); // Start transaction

            try (PreparedStatement debitStatement = connection.prepareStatement(debitQuery);
                 PreparedStatement creditStatement = connection.prepareStatement(creditQuery)) {

                // Deduct from sender
                debitStatement.setDouble(1, amount);
                debitStatement.setString(2, fromAccountNumber);
                debitStatement.setDouble(3, amount);
                int debitResult = debitStatement.executeUpdate();

                if (debitResult == 0) {
                    System.out.println("Insufficient balance or account not found.");
                    connection.rollback();
                    return false;
                }

                // Add to receiver
                creditStatement.setDouble(1, amount);
                creditStatement.setString(2, toAccountNumber);
                int creditResult = creditStatement.executeUpdate();

                if (creditResult == 0) {
                    System.out.println("Recipient account not found. Rolling back transaction.");
                    connection.rollback();
                    return false;
                }

                connection.commit(); // Commit transaction
                return true;

            } catch (SQLException e) {
                System.err.println("Error during fund transfer: " + e.getMessage());
                connection.rollback(); // Rollback in case of error
            }
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
        return false;
    }

}