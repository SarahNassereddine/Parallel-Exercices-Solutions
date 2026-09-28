/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */


/**
 *
 * @author user
 */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FinalExam23_24_S_Solution {

    public final int NUMBER_OF_ROUNDS = 10;
    private int BPlayerValue;
    private int WPlayerValue;
    private final Arbitre arbitre = new Arbitre();
    private final BlackPlayer blackPlayer = new BlackPlayer();
    private final WhitePlayer whitePlayer = new WhitePlayer();
    boolean black_Turn=true; //black
    boolean gameover=false;
  

    // TODO: Declare your flags and synchronization variables here
    // Analysis Steps:
    // 1. What are the shared variables?
    // 2. Where is the critical section?
    // 3. Any condition where thread must wait?
    // 4. Who signals whom and when?

    boolean playerTurn = true;
    Lock lock = new ReentrantLock();
    Condition canPlay = lock.newCondition();
    Condition canArbitre = lock.newCondition();
      private final Condition canPlayInternally= lock.newCondition();

    // ─── ARBITRE ──────────────────────────────────────────────────────────────
    class Arbitre extends Thread {

        private int BPlayerPoints = 0;
        private int WPlayerPoints = 0;

        @Override
        public void run() {
            // TODO: Part 1 — Run for NUMBER_OF_ROUNDS rounds

            for (int i = 0; i < NUMBER_OF_ROUNDS; i++) {
                lock.lock();
                try {
                    while (playerTurn) {
                        canArbitre.await();
                    }

                    if (BPlayerValue > WPlayerValue) {
                        BPlayerPoints++;
                    } else if (WPlayerValue > BPlayerValue) {
                        WPlayerPoints++;
                    }

                    System.out.println("Round " + (i + 1) + ": BPlayerValue = " + BPlayerValue + ", WPlayerValue = "
                            + WPlayerValue);
                    System.out.println(
                            "Current Points: BPlayerPoints = " + BPlayerPoints + ", WPlayerPoints = " + WPlayerPoints);
                    playerTurn = true;
                        BPlayerValue=0;
                        WPlayerValue=0;
                    canPlay.signalAll();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }

            }

            // After all rounds — print final results
            System.out.println("Final Points: BPlayerPoints = " + BPlayerPoints + ", WPlayerPoints = " + WPlayerPoints);
            System.out.println("End of the " + NUMBER_OF_ROUNDS + " rounds...");

            // TODO: Part 2 — Handle tie scenario
            // Simulate tie
            System.out.println("Simulating that BPlayerPoints == WPlayerPoints");
            BPlayerPoints = WPlayerPoints = 2;

            if (BPlayerPoints == WPlayerPoints) {
                System.out.println("Players should now play a face-to-face gaming.");

            }

            System.out.println("---- DEATH OF ARBITRE ----");
        }

    }

    // ─── BLACK PLAYER ─────────────────────────────────────────────────────────
    class BlackPlayer extends Thread {

        private final Random random = new Random();

        @Override
        public void run() {
            // TODO: Part 1 — Run for NUMBER_OF_ROUNDS rounds

            for (int i = 0; i < NUMBER_OF_ROUNDS; i++) {
                lock.lock();
                try{
                    while(!playerTurn){
                        canPlay.await();
                    }
                    
                int value = random.nextInt(10) + 1;
                    BPlayerValue = value;
                    System.out.println("BlackPlayer generated: " + value);  

                    if(WPlayerValue!=0){
                        playerTurn=false;
                        canArbitre.signal();
                     
                    } 
                    while(BPlayerValue!=0){
                        canPlay.await();
                    }
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                finally{
                    lock.unlock();
                }

                              
            }
 
 
            // TODO: Part 2 — Face-to-face game
            lock.lock();
            try{
             while(!gameover){
                while(!black_Turn){
                    canPlayInternally.await();
                }
                    
                int value = random.nextInt(10) + 1;
                    BPlayerValue = value;
                    System.out.println("BlackPlayer generated: " + value);  

                    if(BPlayerValue==5){
                        gameover=true;
                         black_Turn=false;
                              canPlayInternally.signal();
                       return;}
                       black_Turn=false;
                       canPlayInternally.signal();
                     
            }
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
            finally{
                lock.unlock();
            }
           
         

 
 
            System.out.println(" --- DEATH OF BPLAYER ---");
        }

    }

    // ─── WHITE PLAYER ─────────────────────────────────────────────────────────
    class WhitePlayer extends Thread {

        private final Random random = new Random();

        @Override
        public void run() {
            // TODO: Part 1 — Run for NUMBER_OF_ROUNDS rounds

            for (int i = 0; i < NUMBER_OF_ROUNDS; i++) {
                   lock.lock();
                try{
                    while(!playerTurn){
                        canPlay.await();
                    }
                int value = random.nextInt(10) + 1;
                WPlayerValue = value;
                System.out.println("WhitePlayer generated: " + value);
                        if(BPlayerValue!=0){
                        playerTurn=false;
                        canArbitre.signal();
                     
                    } 
                         while(WPlayerValue!=0){
                        canPlay.await();
                    }

            }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                finally{
                    lock.unlock();
                }

        
        }

            // TODO: Part 2 — Face-to-face game
            // Loop until someone generates exactly 5:

              lock.lock();
            try{
             while(!gameover){
                while(black_Turn){
                    canPlayInternally.await();
                }
                    
                int value = random.nextInt(10) + 1;
                    WPlayerValue = value;
                    System.out.println("whitePlayer generated: " + value);  

                    if(WPlayerValue==5){
                        gameover=true;
                         black_Turn=true;
                     canPlayInternally.signal();
                       return;}
                     black_Turn=true;
                       canPlayInternally.signal();
                     
            }
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
            finally{
                lock.unlock();
            }
           
         


            System.out.println(" --- DEATH OF WPLAYER ---");
        }

    }

    // ─── MAIN ─────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        FinalExam23_24_S_Solution exam = new FinalExam23_24_S_Solution();
        exam.blackPlayer.start();
        exam.whitePlayer.start();
        exam.arbitre.start();
    }

}
