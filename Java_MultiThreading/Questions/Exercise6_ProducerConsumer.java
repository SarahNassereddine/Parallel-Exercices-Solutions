/**
 * Exercise 6: Producer-Consumer Pattern with Conditions
 * 
 * OBJECTIVE: Implement a producer-consumer pattern using ReentrantLock and Condition variables.
 * 
 * TASK:
 * 1. Create a shared buffer (ArrayList) between producer and consumer threads
 * 2. Use ReentrantLock to protect the shared buffer
 * 3. Use Condition variables (notFull, notEmpty) for thread coordination
 * 4. Producer threads generate items and add them to the buffer (100 times)
 * 5. Consumer threads remove items from the buffer and process them (100 times)
 * 6. Handle buffer full/empty conditions using await() and signalAll()
 * 
 * MULTIPLE APPROACHES:
 * This exercise focuses on using Conditions, but you can also try:
 * - Option 1: Use ReentrantLock with Condition variables (required for this exercise)
 * - Option 2: Use BlockingQueue (simpler, but less control)
 * Try implementing with Conditions first to understand await() and signalAll()!
 * 
 * EXPECTED OUTPUT:
 * - All items produced are consumed
 * - No buffer overflow or underflow
 * - Proper coordination between producers and consumers
 * - Total items produced = Total items consumed = 100
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Exercise6_ProducerConsumer {
    private static final int BUFFER_SIZE = 10;
    private static final int NUM_PRODUCERS = 2;
    private static final int NUM_CONSUMERS = 2;
    private static final int ITEMS_PER_PRODUCER = 50; // Total: 100 items
    
    // Sequential version (no parallelism)
    public static void sequentialVersion() {
        System.out.println("Sequential Version:");
        for (int i = 0; i < ITEMS_PER_PRODUCER * NUM_PRODUCERS; i++) {
            int item = i + 1;
            System.out.println("Produced: " + item);
            System.out.println("Consumed: " + item);
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        sequentialVersion();
        
        // TODO: Implement parallel version
        // 1. Create shared buffer, lock, and condition variables
        // 2. Create Producer and Consumer instances
        // 3. Create and start threads
        // 4. Wait for all threads to complete
    }
    
    // Producer class
    static class Producer implements Runnable {
        private int producerId;
        private List<Integer> buffer;
        private ReentrantLock lock;
        private Condition notFull;
        private Condition notEmpty;
        private int itemsToProduce;
        
        public Producer(int producerId, List<Integer> buffer, ReentrantLock lock, 
                       Condition notFull, Condition notEmpty, int itemsToProduce) {
            this.producerId = producerId;
            this.buffer = buffer;
            this.lock = lock;
            this.notFull = notFull;
            this.notEmpty = notEmpty;
            this.itemsToProduce = itemsToProduce;
        }
        
        @Override
        public void run() {
            // TODO: Implement producer logic
            // Produce itemsToProduce items
            // For each item:
            //   - Acquire lock
            //   - Wait if buffer is full (use while loop with await())
            //   - Add item to buffer
            //   - Signal consumers (signalAll())
            //   - Release lock
        }
    }
    
    // Consumer class
    static class Consumer implements Runnable {
        private int consumerId;
        private List<Integer> buffer;
        private ReentrantLock lock;
        private Condition notFull;
        private Condition notEmpty;
        private int itemsToConsume;
        
        public Consumer(int consumerId, List<Integer> buffer, ReentrantLock lock,
                       Condition notFull, Condition notEmpty, int itemsToConsume) {
            this.consumerId = consumerId;
            this.buffer = buffer;
            this.lock = lock;
            this.notFull = notFull;
            this.notEmpty = notEmpty;
            this.itemsToConsume = itemsToConsume;
        }
        
        @Override
        public void run() {
            // TODO: Implement consumer logic
            // Consume itemsToConsume items
            // For each item:
            //   - Acquire lock
            //   - Wait if buffer is empty (use while loop with await())
            //   - Remove item from buffer
            //   - Process the item (print it)
            //   - Signal producers (signalAll())
            //   - Release lock
        }
    }
}
