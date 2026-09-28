package main.java;
/**
 * Exercise 5: Synchronization Mechanisms
 * 
 * OBJECTIVE: Use synchronized blocks or ReentrantLock to protect shared data.
 * 
 * TASK:
 * 1. Identify the critical section (shared data access)
 * 2. Use synchronized block or ReentrantLock to protect it
 * 3. Ensure thread-safe access to shared bank account
 * 4. Verify that final balance is correct
 * 
 * MULTIPLE APPROACHES:
 * This exercise can be solved using different synchronization mechanisms:
 * - Option 1: Use synchronized blocks or synchronized methods
 * - Option 2: Use ReentrantLock with lock() and unlock()
 * - Option 3: Use synchronized methods on the BankAccount class
 * Try implementing at least two approaches to understand the differences!
 * 
 * EXPECTED OUTPUT:
 * - Final balance should be correct (no race conditions)
 * - All transactions should be properly recorded
 */

public class Exercise5_Synchronization {
    private static class BankAccount {
        private int balance = 1000; // Starting balance
        
        public void deposit(int amount) {
            // Critical section - needs protection
            int temp = balance;
            temp += amount;
            balance = temp;
            System.out.println("Deposited: " + amount + ", Balance: " + balance);
        }
        
        public void withdraw(int amount) {
            // Critical section - needs protection
            if (balance >= amount) {
                int temp = balance;
                temp -= amount;
                balance = temp;
                System.out.println("Withdrawn: " + amount + ", Balance: " + balance);
            } else {
                System.out.println("Insufficient funds for withdrawal: " + amount);
            }
        }
        
        public int getBalance() {
            return balance;
        }
    }
    
    private static final int NUM_THREADS = 10;
    private static final int OPERATIONS_PER_THREAD = 100;
    
    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccount();
        
        System.out.println("Initial balance: " + account.getBalance());
        
        // Sequential version
        for (int i = 0; i < OPERATIONS_PER_THREAD; i++) {
            account.deposit(10);
            account.withdraw(5);
        }
        System.out.println("Sequential final balance: " + account.getBalance());
        System.out.println("Expected: " + (1000 + OPERATIONS_PER_THREAD * 5));
        
        // TODO: Implement parallel version
    }
}
