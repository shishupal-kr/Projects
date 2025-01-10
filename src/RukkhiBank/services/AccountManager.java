package RukkhiBank.services;
import RukkhiBank.models.BankAccount;
import newapp.FileStorage;

import java.util.HashMap;
import static newapp.FileStorage.saveAccountsToFile;


public class AccountManager {
         public static HashMap<String, BankAccount> accounts = new HashMap<>();

        static {
            // Load accounts at startup
            FileStorage.loadAccountsFromFile(AccountManager.accounts);
        }

    public static void addAccount(BankAccount account) {
        if (accounts.containsKey(account.getAccountNumber())) {
            System.out.println("Account with this number already exists. Cannot add.");
        } else {
            accounts.put(account.getAccountNumber(), account);
            saveAccountsToFile(accounts);
        }
    }


        public static BankAccount getAccount(String accountNumber) {

            return accounts.get(accountNumber);
        }

        public static void viewAllAccounts() {
            if (accounts.isEmpty()) {
                System.out.println("No accounts found.");
                return;
            }

            System.out.println("--- List of All Accounts ---");
            for (BankAccount account : accounts.values()) {
                System.out.println("Account Number: " + account.getAccountNumber());
                System.out.println("Account Name: " + account.getAccountHolderName());
                System.out.println("Account Type: " + account.getAccountType());
                System.out.println("Email: " + account.getEmail());
                System.out.println("Balance: ₹" + account.getBalance());
                System.out.println("----------------------------");
            }
        }

    public static boolean accountExists(String accountNumber) {
        return false;
    }

    public static void loadAccountsFromFile() {
    }
}
