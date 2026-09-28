/**
 * Exercise 8: Reader-Writer Pattern with Conditions
 * 
 * OBJECTIVE: Implement a reader-writer pattern where readers read all data first (100 times),
 * then writers write all data (100 times), using ReentrantLock and Condition variables.
 * 
 * TASK:
 * 1. Create a shared data structure (array or list) that readers and writers access
 * 2. Use ReentrantLock to protect the shared data
 * 3. Use Condition variables to coordinate: readers read ALL data, then writers write ALL data
 * 4. Reader threads read from the shared data structure (100 read operations total)
 * 5. Writer threads write to the shared data structure (100 write operations total)
 * 6. Implement the condition: "Read all data first, then write all data"
 * 
 * MULTIPLE APPROACHES:
 * You can implement this in different ways:
 * - Option 1: Use a boolean flag (readingPhase) with Condition to coordinate phases
 * - Option 2: Use counters to track how many reads/writes are done
 * - Option 3: Use two separate phases with barriers
 * Try implementing with a readingPhase flag first!
 * 
 * EXPECTED OUTPUT:
 * - All 100 read operations complete before any write operation starts
 * - All 100 write operations complete
 * - Proper phase coordination using Condition variables
 * - No race conditions or deadlocks
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Exercise8_ReaderWriter {
    private static final int DATA_SIZE = 10;
    private static final int NUM_READERS = 5;
    private static final int NUM_WRITERS = 5;
    private static final int READS_PER_READER = 20; // Total: 100 reads
    private static final int WRITES_PER_WRITER = 20; // Total: 100 writes
    
    // Shared data structure
    private static List<Integer> sharedData = new ArrayList<>();
    
    // Sequential version
    public static void sequentialVersion() {
        System.out.println("Sequential Version:");
        
        // Initialize shared data
        for (int i = 0; i < DATA_SIZE; i++) {
            sharedData.add(i);
        }
        
        // Read all data
        System.out.println("Reading phase:");
        for (int i = 0; i < READS_PER_READER * NUM_READERS; i++) {
            int index = i % DATA_SIZE;
            int value = sharedData.get(index);
            System.out.println("Read: index=" + index + ", value=" + value);
        }
        
        // Write all data
        System.out.println("Writing phase:");
        for (int i = 0; i < WRITES_PER_WRITER * NUM_WRITERS; i++) {
            int index = i % DATA_SIZE;
            sharedData.set(index, i);
            System.out.println("Write: index=" + index + ", value=" + i);
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        // Initialize shared data
        sharedData.clear();
        for (int i = 0; i < DATA_SIZE; i++) {
            sharedData.add(i);
        }
        
        sequentialVersion();
        
        // Reset shared data
        sharedData.clear();
        for (int i = 0; i < DATA_SIZE; i++) {
            sharedData.add(i);
        }
        
        // TODO: Implement parallel version
        // 1. Create shared lock and condition variables
        // 2. Create shared state variables (readingPhase, counters)
        // 3. Create Reader and Writer instances
        // 4. Create and start threads
        // 5. Wait for all threads to complete
    }
    
    // Reader class
    static class Reader implements Runnable {
        private int readerId;
        private List<Integer> sharedData;
        private ReentrantLock lock;
        private Condition canRead;
        private Condition canWrite;
        private int readsToPerform;
        private boolean[] readingPhase;
        private int[] readsCompleted;
        private int totalReads;
        
        public Reader(int readerId, List<Integer> sharedData, ReentrantLock lock,
                     Condition canRead, Condition canWrite, int readsToPerform,
                     boolean[] readingPhase, int[] readsCompleted, int totalReads) {
            this.readerId = readerId;
            this.sharedData = sharedData;
            this.lock = lock;
            this.canRead = canRead;
            this.canWrite = canWrite;
            this.readsToPerform = readsToPerform;
            this.readingPhase = readingPhase;
            this.readsCompleted = readsCompleted;
            this.totalReads = totalReads;
        }
        
        @Override
        public void run() {
            // TODO: Implement reader logic
            // Perform readsToPerform read operations
            // For each read:
            //   - Acquire lock
            //   - Wait if not in reading phase (use while loop with await())
            //   - Read from shared data
            //   - Increment readsCompleted counter
            //   - If all reads are done, switch to writing phase and signal writers
            //   - Release lock
        }
    }
    
    // Writer class
    static class Writer implements Runnable {
        private int writerId;
        private List<Integer> sharedData;
        private ReentrantLock lock;
        private Condition canRead;
        private Condition canWrite;
        private int writesToPerform;
        private boolean[] readingPhase;
        private int[] writesCompleted;
        private int totalWrites;
        
        public Writer(int writerId, List<Integer> sharedData, ReentrantLock lock,
                     Condition canRead, Condition canWrite, int writesToPerform,
                     boolean[] readingPhase, int[] writesCompleted, int totalWrites) {
            this.writerId = writerId;
            this.sharedData = sharedData;
            this.lock = lock;
            this.canRead = canRead;
            this.canWrite = canWrite;
            this.writesToPerform = writesToPerform;
            this.readingPhase = readingPhase;
            this.writesCompleted = writesCompleted;
            this.totalWrites = totalWrites;
        }
        
        @Override
        public void run() {
            // TODO: Implement writer logic
            // Perform writesToPerform write operations
            // For each write:
            //   - Acquire lock
            //   - Wait if in reading phase (use while loop with await())
            //   - Write to shared data
            //   - Increment writesCompleted counter
            //   - If all writes are done, switch back to reading phase and signal readers
            //   - Release lock
        }
    }
}
