package RukkhiBank.services;

import RukkhiBank.models.BankAccount;
import RukkhiBank.storage.RukkhiBankJdbc;

public class TransactionManager {

    //Deposit logic for
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

    //withdraw
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

    // New method for transferring funds
    public static boolean transferFunds(String fromAccountNumber, String toAccountNumber, double amount) {
        // Validate the amount
        if (amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive value.");
            return false;
        }

        // Fetch both the sender and recipient accounts
        BankAccount fromAccount = AccountManager.getAccount(fromAccountNumber);
        BankAccount toAccount = AccountManager.getAccount(toAccountNumber);

        if (fromAccount == null) {
            System.out.println("Sender account not found!");
            return false;
        }

        if (toAccount == null) {
            System.out.println("Recipient account not found!");
            return false;
        }

        // Check if the sender has sufficient balance
        if (fromAccount.getBalance() < amount) {
            System.out.println("Insufficient funds in sender account.");
            return false;
        }

        // Perform the transaction
        fromAccount.withdraw(amount); // Deduct from sender
        toAccount.deposit(amount);     // Add to recipient

        // Update both accounts in the database
        if (RukkhiBankJdbc.updateBalance(fromAccount) && RukkhiBankJdbc.updateBalance(toAccount)) {
            System.out.println("Fund transfer successful!");
            System.out.println("Sender New Balance: ₹" + fromAccount.getBalance());
            System.out.println("Recipient New Balance: ₹" + toAccount.getBalance());
            return true;
        } else {
            System.out.println("Failed to update balance in the database.");
            return false;
        }
    }

}