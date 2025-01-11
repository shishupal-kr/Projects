package test.java;

import RukkhiBank.models.BankAccount;
import RukkhiBank.services.TransactionManager;
import RukkhiBank.storage.RukkhiBankJdbc;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class test {

    @BeforeEach
    void setup() {
        // Create sample accounts in the database for testing
        RukkhiBankJdbc.insertAccount(new BankAccount("Sender", "123456", "Savings", "sender@example.com", 5000.0));
        RukkhiBankJdbc.insertAccount(new BankAccount("Receiver", "654321", "Savings", "receiver@example.com", 2000.0));
    }

    @Test
    void testTransferFunds_Success() {
        // Act
        boolean result = TransactionManager.transferFunds("123456", "654321", 1000.0);

        // Assert
        assertTrue(result, "Fund transfer should be successful.");

        // Verify updated balances
        BankAccount sender = RukkhiBankJdbc.getAccount("123456");
        BankAccount receiver = RukkhiBankJdbc.getAccount("654321");

        assertEquals(4000.0, sender.getBalance(), "Sender's balance should be updated.");
        assertEquals(3000.0, receiver.getBalance(), "Receiver's balance should be updated.");
    }

    @Test
    void testTransferFunds_InsufficientFunds() {
        // Act
        boolean result = TransactionManager.transferFunds("123456", "654321", 6000.0);

        // Assert
        assertFalse(result, "Fund transfer should fail due to insufficient funds.");
    }

    @Test
    void testTransferFunds_InvalidRecipient() {
        // Act
        boolean result = TransactionManager.transferFunds("123456", "999999", 1000.0);

        // Assert
        assertFalse(result, "Fund transfer should fail due to invalid recipient account.");
    }

    @Test
    void testTransferFunds_InvalidSender() {
        // Act
        boolean result = TransactionManager.transferFunds("999999", "654321", 1000.0);

        // Assert
        assertFalse(result, "Fund transfer should fail due to invalid sender account.");
    }
    @AfterEach
    void cleanup() {
        RukkhiBankJdbc.deleteAccount("123456");
        RukkhiBankJdbc.deleteAccount("654321");
    }
}