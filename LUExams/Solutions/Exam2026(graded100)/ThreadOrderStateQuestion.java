//112872_SARAH_NASSEREDDINE
package main.java;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadOrderStateQuestion {

   static Lock lock= new ReentrantLock();
    static Condition canTake= lock.newCondition();
   static int turn=1;
  

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        
        Thread t1 = new Thread(new Worker("T1", 1));
        Thread t2 = new Thread(new Worker("T2", 2));
        Thread t3 = new Thread(new Worker("T3", 3));
        Thread t4 = new Thread(new Worker("T4", 4));
        Thread t5 = new Thread(new Worker("T5", 5));

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        // TODO: Wait for all threads to finish
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();


        long endTime = System.currentTimeMillis();
        System.out.println("Execution time: " + (endTime - startTime) + " ms");
    }

    static class Worker implements Runnable {
        private final String name;
        private final int threadId;

        public Worker(String name, int threadId) {
            this.name = name;
            this.threadId = threadId;
            //we can pass hre also lock and conditions and turn but we declared them static..
        }

        @Override
        public void run() {
            // TODO: Implement the synchronization logic to enforce ordering

            lock.lock();
            try{
                while(threadId!=turn){
                    canTake.await();
                }
                turn++;
              
                 System.out.println(name + " has executed.");
                   canTake.signalAll();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            finally{
                lock.unlock();
            }
           
        }
    }
}
