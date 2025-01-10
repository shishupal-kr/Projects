package RukkhiBank.models;
import java.io.*;

public class BankAccount implements Serializable {
    private final String accountHolderName;
    private final String accountNumber;
    private final String accountType;
    private final String email;
    private double balance;

    public BankAccount(String AccountHolderName, String AccountNumber,
                       String AccountType, String Email, double balance) {
        this.accountHolderName = AccountHolderName;
        this.accountNumber = AccountNumber;
        this.accountType = AccountType;
        this.email = Email;
        this.balance = balance;
    }

    //get Account Detail
    public String getAccountHolderName() {
        return accountHolderName;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public String getAccountType() {
        return accountType;
    }
    public String getEmail() {
        return email;
    }
    public double getBalance() {
        return balance;
    }

    // implement Deposit logic
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            System.out.println("\nDeposited Successfully: ₹" + amount);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }
    //implement withdraw logic
    public void withdraw(double amount){
        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;
            System.out.println("\nWithdrawn Successfully: ₹" + amount);
        } else {
            System.out.println("Invalid withdrawal amount or insufficient funds.");
        }
    }

}