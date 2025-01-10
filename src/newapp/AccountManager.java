package newapp;

import RukkhiBank.services.Security;
import static RukkhiBank.services.AccountManager.accounts;
import static newapp.FileStorage.saveAccountsToFile;

public class AccountManager {
    // Consolidated deleteAccount method
    public static boolean deleteAccount(String accountNumber) {
        // Admin verification
        if (!Security.verifyAdmin()) {
            System.out.println("Invalid admin password. Deletion aborted.");
            return false;
        }

        // Deleting the account if it exists
        if (accounts.containsKey(accountNumber)) {
            accounts.remove(accountNumber);
            saveAccountsToFile(accounts);
            System.out.println("Account Deleted Successfully: " + accountNumber);
            return true;
        } else {
            System.out.println("Account not found.");
            return false;
        }
    }
}
