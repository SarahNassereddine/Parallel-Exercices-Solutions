/**
 * Exercise 4: Semaphore
 * 
 * OBJECTIVE: Use Semaphore to limit concurrent access to a shared resource.
 * 
 * TASK:
 * 1. Create a Semaphore with limited permits (e.g., 3)
 * 2. Multiple threads try to access a resource simultaneously
 * 3. Only MAX_CONCURRENT threads should access the resource at once
 * 4. Other threads should wait until a permit is available
 * 5. Release the permit after using the resource
 * 
 * EXPECTED OUTPUT:
 * - At most MAX_CONCURRENT threads accessing resource simultaneously
 * - All threads eventually get access
 * - Proper resource cleanup
 */

import java.util.concurrent.Semaphore;

public class Exercise4_Semaphore {
    private static final int NUM_THREADS = 10;
    private static final int MAX_CONCURRENT = 3; // Maximum threads that can access resource simultaneously
    
    // Simulated resource access
    private static void accessResource(int threadId) {
        System.out.println("Thread " + threadId + " is accessing the resource");
        try {
            Thread.sleep(1000); // Simulate work
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Thread " + threadId + " finished using the resource");
    }
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Sequential Version (all threads wait):");
        for (int i = 0; i < NUM_THREADS; i++) {
            accessResource(i);
        }
        
        // TODO: Implement parallel version
    }
}
