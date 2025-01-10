package RukkhiBank.services;

import RukkhiBank.models.BankAccount;
import RukkhiBank.storage.RukkhiBankJdbc;

public class TransactionManager {

    /**
     * Handles the deposit operation.
     *
     * @param accountNumber The account number to deposit money into.
     * @param amount The amount to deposit.
     * @return true if the deposit was successful; false otherwise.
     */
    public static boolean deposit(String accountNumber, double amount) {
        // Validate deposit amount
        if (amount <= 0) {
            System.out.println("Invalid deposit amount. Please enter a positive value.");
            return false;
        }

        // Fetch the account using AccountManager
        BankAccount account = AccountManager.getAccount(accountNumber);
        if (account == null) {
            System.out.println("Account not found!");
            return false;
        }

        // Perform the deposit operation
        account.deposit(amount); // Update the account's balance in memory

        // Update the database
        if (RukkhiBankJdbc.updateBalance(account)) {
            System.out.println("Deposit successful!");
            System.out.println("New Balance: ₹" + account.getBalance());
            return true;
        } else {
            System.out.println("Failed to update balance in the database.");
            return false;
        }
    }

    public static boolean withdraw(String accountNumber, double amount) {
        // Validate withdrawal amount
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount. Please enter a positive value.");
            return false;
        }

        // Fetch the account using AccountManager
        BankAccount account = AccountManager.getAccount(accountNumber);
        if (account == null) {
            System.out.println("Account not found!");
            return false;
        }

        // Check if sufficient balance is available
        if (account.getBalance() < amount) {
            System.out.println("Insufficient funds!");
            return false;
        }

        // Perform the withdrawal operation
        account.withdraw(amount); // Update the account's balance in memory

        // Update the database
        if (RukkhiBankJdbc.updateBalance(account)) {
            System.out.println("Withdrawal successful!");
            System.out.println("Remaining Balance: ₹" + account.getBalance());
            return true;
        } else {
            System.out.println("Failed to update balance in the database.");
            return false;
        }
    }
}