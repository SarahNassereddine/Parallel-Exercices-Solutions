/**
 * Exercise 1: Creating Threads
 * 
 * OBJECTIVE: Convert this sequential program to use multiple threads.
 * 
 * TASK:
 * 1. Create a class that extends Thread or implements Runnable
 * 2. Divide the work (calculating squares) among multiple threads
 * 3. Each thread should calculate squares for a portion of the array
 * 4. Use join() to wait for all threads to complete
 * 5. Measure and compare execution time
 * 
 * MULTIPLE APPROACHES:
 * You can solve this exercise in different ways:
 * - Option 1: Create a class that extends Thread
 * - Option 2: Create a class that implements Runnable
 * - Option 3: Use lambda expressions with Runnable
 * Try implementing at least two different approaches to understand the differences!
 * 
 * EXPECTED OUTPUT:
 * - Same results as sequential version
 * - Reduced execution time (depending on number of cores)
 */

public class Exercise1_CreatingThreads {
    private static final int ARRAY_SIZE = 1000000;
    private static final int NUM_THREADS = 4;
    
    public static void main(String[] args) {
        int[] numbers = new int[ARRAY_SIZE];
        long[] squares = new long[ARRAY_SIZE];
        
        // Initialize array
        for (int i = 0; i < ARRAY_SIZE; i++) {
            numbers[i] = i + 1;
        }
        
        // Sequential computation
        long startTime = System.nanoTime();
        
        for (int i = 0; i < ARRAY_SIZE; i++) {
            squares[i] = (long) numbers[i] * numbers[i];
        }
        
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000; // Convert to milliseconds
        
        // Verify result (sum of first 10 squares)
        long sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += squares[i];
        }
        
        System.out.println("Sequential Version:");
        System.out.println("Sum of first 10 squares: " + sum);
        System.out.println("Execution time: " + duration + " ms");
        
        // TODO: Implement parallel version
    }
}
