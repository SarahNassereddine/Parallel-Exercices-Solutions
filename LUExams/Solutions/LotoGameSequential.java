
// INSERT HERE YOUR FULL NAME AND ID
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LotoGameSequential {
    // Keep this value before submitting your exam.
    private static final int NUM = 25;
    private static int globalX = -1, globalY = -1;
    private static final List<Integer> winners = new ArrayList<>();
    private static final Lock lock = new ReentrantLock();
    private static final Condition canDraw = lock.newCondition(); // drawing waits choosing(generation of rundoms) ends
                                                                  // to start->waits
    private static final Condition canCheck = lock.newCondition(); // cannnot check if drawing did not occure ->waits
    private static int countGenerated = 0;
    private static boolean generationTime = true;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(NUM + 1);

        // Create players
        for (int i = 0; i < NUM; i++) {
            Player player = new Player(i + 1);
            executor.execute(player);
        }

        executor.execute(new Loto());
        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.yield();
        }

        // Display comprehensive results
        System.out.println("\n=== FINAL RESULTS ===");
        System.out.println("Winning Loto numbers: " + globalX + ", " + globalY);
        System.out.println("Total winners: " + winners.size());
        if (!winners.isEmpty()) {
            System.out.println("Winning players:");
            for (int winnerId : winners) {
                System.out.println("Player " + winnerId);
            }
        } else {
            System.out.println("No winners this round!");
        }
    }

    static class Player implements Runnable {

        final int id;
        int x, y;

        Player(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            // Generate numbers (1-5)
         
                Random rand = new Random();
                x = rand.nextInt(3) + 1;
                y = rand.nextInt(3) + 1;
                System.out.println("Player " + id + " chose: " + x + ", " + y);
                   lock.lock();
            try {
                countGenerated++;
                if (countGenerated == NUM) {
                    generationTime = false;
                    canDraw.signalAll();
                }

                while (globalX == -1 || globalY == -1) {

                    canCheck.await();

                }
                // Check if winner
                if (x == globalX && y == globalY) {
                    winners.add(id);
                    System.out.println("Player " + id + " wins with " + x + ", " + y + "!");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        

            // This sleep is to show you that the code is not working in sequentail
            // and help you to be sure that your solution is correct. Do not remove it in
            // your solution
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(LotoGameSequential.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    static class Loto implements Runnable {

        @Override
        public void run() {

            // Generate winning numbers (1-5)
            lock.lock();
            try {
                while (generationTime) {
                    canDraw.await();

                }
                Random rand = new Random();
                globalX = rand.nextInt(3) + 1;
                globalY = rand.nextInt(3) + 1;

                System.out.println("\nLoto draws: " + globalX + ", " + globalY);
                canCheck.signalAll();

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }
    }
}
