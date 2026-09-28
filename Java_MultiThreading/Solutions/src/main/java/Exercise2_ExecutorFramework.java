package main.java;
/**
 * Exercise 2: Executor Framework
 * 
 * OBJECTIVE: Use ExecutorService to parallelize this computation.
 * 
 * TASK:
 * 1. Create an ExecutorService with a fixed thread pool
 * 2. Submit tasks using execute(Runnable) to calculate prime numbers in parallel
 * 3. Collect results using a thread-safe shared structure (e.g., synchronized list or concurrent collection)
 * 4. Shutdown the executor properly (shutdown(), awaitTermination())
 * 5. Compare execution time with sequential version
 * 
 * MULTIPLE APPROACHES:
 * You can use different ExecutorService types:
 * - Option 1: Use newFixedThreadPool() (recommended for this exercise)
 * - Option 2: Use newCachedThreadPool() (creates threads as needed)
 * - Option 3: Use newWorkStealingPool() (uses work-stealing algorithm)
 * Try implementing with different thread pool types and compare their behavior!
 * 
 * EXPECTED OUTPUT:
 * - Same number of primes found
 * - Reduced execution time
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class Exercise2_ExecutorFramework {
    private static final int RANGE_START = 2;
    private static final int RANGE_END = 100000;
    private static final int NUM_THREADS = 4;
    
    // Helper method to check if a number is prime
    private static boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }
        static class Thread1 implements Runnable{
            int i;
            List<Integer> primes;
            
            public Thread1(int i, List<Integer> primes) {
                this.i = i;
                this.primes = primes;
            }

        @Override
        public void run() {
             if (isPrime(i)) {
                primes.add(i);
            }
        }
        
    }
    public static void main(String[] args) {
        // Sequential version
        long startTime = System.nanoTime();
        List<Integer> primes = Collections.synchronizedList(new ArrayList<>());
        ExecutorService executor= Executors.newFixedThreadPool(NUM_THREADS);
        // Submit tasks using execute() - returns void
       for (int i = RANGE_START; i <= RANGE_END; i++) {
            final int taskId = i;
           // executor.execute(new Thread1(taskId, primes));
           executor.execute(()->{
             if (isPrime(taskId)) {
                primes.add(taskId);
            }
           });
        
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
        
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        Collections.sort(primes);
        System.out.println("Parallel Version:");
        System.out.println("Found " + primes.size() + " primes");
        System.out.println("Execution time: " + duration + " ms");
        System.out.println("First 10 primes: " + primes.subList(0, Math.min(10, primes.size())));
        
        // TODO: Implement parallel version
    }
}
