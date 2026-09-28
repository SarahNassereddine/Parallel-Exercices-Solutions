/**
 * Exercise 3: Atomic Classes
 * 
 * OBJECTIVE: Use AtomicInteger to safely increment a counter from multiple threads.
 * 
 * TASK:
 * 1. Replace the regular int counter with AtomicInteger
 * 2. Use incrementAndGet() or getAndIncrement() for thread-safe increments
 * 3. Verify that the final count matches expected value
 * 4. Compare with synchronized version (optional)
 * 
 * MULTIPLE APPROACHES:
 * This exercise can be solved using different synchronization mechanisms:
 * - Option 1: Use AtomicInteger (recommended for this exercise)
 * - Option 2: Use synchronized blocks or methods
 * - Option 3: Use ReentrantLock
 * Try implementing at least two approaches and compare their performance!
 * 
 * EXPECTED OUTPUT:
 * - Final count should be exactly NUM_THREADS * INCREMENTS_PER_THREAD
 * - No race conditions or lost updates
 */

import java.util.concurrent.atomic.AtomicInteger;

public class Exercise3_AtomicClasses {
    private static final int NUM_THREADS = 10;
    private static final int INCREMENTS_PER_THREAD = 1000;
    
    // Sequential version (problematic with multiple threads)
    private static int counter = 0;
    
    public static void main(String[] args) throws InterruptedException {
        // Sequential version
        counter = 0;
        for (int i = 0; i < NUM_THREADS * INCREMENTS_PER_THREAD; i++) {
            counter++; // Not thread-safe!
        }
        System.out.println("Sequential count: " + counter);
        System.out.println("Expected: " + (NUM_THREADS * INCREMENTS_PER_THREAD));
        
        // TODO: Implement parallel version
    }
}
