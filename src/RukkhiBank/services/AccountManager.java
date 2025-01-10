package RukkhiBank.services;
import RukkhiBank.models.BankAccount;
import RukkhiBank.storage.RukkhiBankJdbc;

import java.util.HashMap;

public class AccountManager {
         public static HashMap<String, BankAccount> accounts = new HashMap<>();


    public static BankAccount getAccount(String accountNumber) {
        if (accounts.containsKey(accountNumber)) {
            return accounts.get(accountNumber); // Account is found in memory
        } else {
            System.out.println("Account not found in AccountManager. Fetching from DB...");
            BankAccount account = RukkhiBankJdbc.getAccount(accountNumber);
            if (account != null) {
                accounts.put(accountNumber, account); // Add to in-memory cache
            }
            return account; // Return the fetched account (or null if not found in DB)
        }
    }

}
