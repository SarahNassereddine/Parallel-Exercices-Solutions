package main.java;
/**
 * Exercise 6 - Solution using ReentrantLock with Condition variables
 * Producer-Consumer pattern with bounded buffer, using await()/signalAll().
 */
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Solution6_ReentrantLockCondition {
    private static final int BUFFER_SIZE = 10;
    private static final int NUM_PRODUCERS = 2;
    private static final int NUM_CONSUMERS = 2;
    private static final int ITEMS_PER_PRODUCER = 50;

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
            for (int i = 0; i < itemsToProduce; i++) {
                lock.lock();
                try {
                    while (buffer.size() == BUFFER_SIZE) {
                        notFull.await();
                    }
                    int item = producerId * 1000 + i;
                    buffer.add(item);
                    System.out.println("Producer " + producerId + " produced: " + item
                            + " (buffer size: " + buffer.size() + ")");
                    notEmpty.signalAll();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

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
            for (int i = 0; i < itemsToConsume; i++) {
                lock.lock();
                try {
                    while (buffer.isEmpty()) {
                        notEmpty.await();
                    }
                    int item = buffer.remove(0);
                    System.out.println("Consumer " + consumerId + " consumed: " + item
                            + " (buffer size: " + buffer.size() + ")");
                    notFull.signalAll();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        List<Integer> buffer = new ArrayList<>();
        ReentrantLock lock = new ReentrantLock();
        Condition notFull = lock.newCondition();
        Condition notEmpty = lock.newCondition();

        int itemsPerConsumer = (ITEMS_PER_PRODUCER * NUM_PRODUCERS) / NUM_CONSUMERS;

        Thread[] producers = new Thread[NUM_PRODUCERS];
        Thread[] consumers = new Thread[NUM_CONSUMERS];

        System.out.println("=== Parallel Producer-Consumer (ReentrantLock + Condition) ===");
        System.out.println("Producers: " + NUM_PRODUCERS + ", Consumers: " + NUM_CONSUMERS);
        System.out.println("Items per producer: " + ITEMS_PER_PRODUCER);
        System.out.println("Items per consumer: " + itemsPerConsumer);
        System.out.println("Total items: " + (ITEMS_PER_PRODUCER * NUM_PRODUCERS) + "\n");

        for (int i = 0; i < NUM_PRODUCERS; i++) {
            producers[i] = new Thread(new Producer(i, buffer, lock, notFull, notEmpty, ITEMS_PER_PRODUCER));
            producers[i].start();
        }

        for (int i = 0; i < NUM_CONSUMERS; i++) {
            consumers[i] = new Thread(new Consumer(i, buffer, lock, notFull, notEmpty, itemsPerConsumer));
            consumers[i].start();
        }

        for (Thread p : producers) p.join();
        for (Thread c : consumers) c.join();

        System.out.println("\nAll items produced and consumed successfully!");
        System.out.println("Buffer is empty: " + buffer.isEmpty());
    }
}
