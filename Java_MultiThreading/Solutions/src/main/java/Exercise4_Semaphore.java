package main.java;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Exercise4_Semaphore {
    private static final int NUM_THREADS = 10;
    private static final int MAX_CONCURRENT = 3; // Maximum threads that can access resource simultaneously
    private static final Semaphore semaphore= new Semaphore(MAX_CONCURRENT);
    
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
        ExecutorService executor= Executors.newFixedThreadPool(NUM_THREADS);
        for (int i = 0; i < NUM_THREADS; i++) {
            int taskId=i;
            executor.execute(new Thread1(taskId));
       
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
        
        // TODO: Implement parallel version
    }
    static class Thread1 implements Runnable{
   int taskId;
        public Thread1(int taskId) {
    this.taskId = taskId;
}
        @Override
        public void run() {
         
            
            // TODO Auto-generated method stub
               try {
                semaphore.acquire();
               } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
               }
                try{
     accessResource(taskId);
                }
                finally{
                    semaphore.release(taskId);
                }
        }
        
    }
}
