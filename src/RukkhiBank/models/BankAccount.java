package RukkhiBank.models;
import java.io.*;

public class BankAccount implements Serializable {
    private final String AccountHolderName;
    private final String AccountNumber;
    private final String AccountType;
    private final String Email;
    private double Balance;

    public BankAccount(String AccountHolderName, String AccountNumber,
                       String AccountType, String Email, double balance) {
        this.AccountHolderName = AccountHolderName;
        this.AccountNumber = AccountNumber;
        this.AccountType = AccountType;
        this.Email = Email;
        this.Balance = balance;
    }

    //get Account Detail
    public String getAccountHolderName() {
        return AccountHolderName;
    }
    public String getAccountNumber() {
        return AccountNumber;
    }
    public String getAccountType() {
        return AccountType;
    }
    public String getEmail() {
        return Email;
    }
    public double getBalance() {
        return Balance;
    }

    // implement Deposit logic
    public void deposit(double amount) {
        if (amount > 0) {
            this.Balance += amount;
            System.out.println("\nDeposited Successfully: ₹" + amount);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }
    //implement withdraw logic
    public void withdraw(double amount){
        if (amount > 0 && this.Balance >= amount) {
            this.Balance -= amount;
            System.out.println("\nWithdrawn Successfully: ₹" + amount);
        } else {
            System.out.println("Invalid withdrawal amount or insufficient funds.");
        }
    }

}