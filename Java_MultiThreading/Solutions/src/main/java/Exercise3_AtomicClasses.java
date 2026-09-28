package main.java;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Exercise3_AtomicClasses {
    private static final int NUM_THREADS = 10;
    private static final int INCREMENTS_PER_THREAD = 1000;
     private static final Object lock= new Object();
    
    // Sequential version (problematic with multiple threads)
     private static int counter = 0;
      // private static AtomicInteger counter;
    
    public static void main(String[] args) throws InterruptedException {
        // Sequential version
        //counter = new AtomicInteger(0);
       
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        // for (int i = 0; i < NUM_THREADS * INCREMENTS_PER_THREAD; i++) {
        //     //counter++;
        // }
        counter=0;
         for (int i = 0; i < NUM_THREADS; i++) {
            executor.execute(new ThreadCounter());
         }
         
        // Shutdown executor (graceful shutdown)
        executor.shutdown();
        
        // Wait for all tasks to complete
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Force shutdown if timeout
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        System.out.println("parallel count: " + counter);
        System.out.println("Expected: " + (NUM_THREADS * INCREMENTS_PER_THREAD));
        
        // TODO: Implement parallel version
    }
    // static class ThreadCounter implements Runnable{

    //     @Override
    //     public void run() {
    //         for(int i=0;i< INCREMENTS_PER_THREAD;i++)
    //       counter.incrementAndGet();
    //     }
        
    // }
      static class ThreadCounter implements Runnable{

        @Override
        public void run() {
            for(int i=0;i< INCREMENTS_PER_THREAD;i++)
          synchronized(lock){
        counter++;
    }
        }
        
    }
}
