
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author YOUR FULL NAME and ID
 */
public class ParallelColors {

    static private String Color = "Red";  
    static private Lock lock= new ReentrantLock();
    static private Condition canStart= lock.newCondition();
    
    public static void main(String[] args) {
    ExecutorService executor = Executors.newCachedThreadPool();
    // Create and launch 6 threads for each
    for (int i = 0; i < 6; i++) {
      executor.execute (new RedToYellow());
      executor.execute (new YellowToBlue());
      executor.execute (new BlueToRed());
    }
      executor.shutdown();
    // Wait until all tasks are finished
    while (!executor.isTerminated()) {
    }
 }

      /////Inner Class ////
    static class RedToYellow implements Runnable {

    @Override
    public void run() {

        lock.lock();
        try{
            while(Color!="Red"){
                canStart.await();
            }
        System.out.println("I am RedToYellow Thread.");
            System.out.println("The color is " + Color);
            System.out.println("I am RedToYellow Thread, I Should await for Red Color.");  
            Color = "Yellow";
            System.out.println("RedToYellow Thread: The new color is " + Color);     
            canStart.signalAll();

        }
        catch(InterruptedException e){
            e.printStackTrace();

        }
        finally{
            lock.unlock();
        }
     
    }
 }
    
       /////Inner Class ////
    static class YellowToBlue implements Runnable {

    @Override
    public void run() {
            System.out.println("I am YellowToBlue Thread.");
            System.out.println("The color is " + Color);
            System.out.println("I am YellowToBlue Thread, I Should await for Yellow Color.");
            Color = "Blue";
            System.out.println("YellowToBlue Thread: The new color is " + Color);
            }
}
    
    /////Inner Class ////
    static class BlueToRed implements Runnable {
    @Override
    public void run() {
            System.out.println("I am BlueToRed Thread.");
            System.out.println("The color is " + Color);
            System.out.println("I am BlueToRed Thread, I Should await for Blue Color.");
            Color = "Red";
            System.out.println("BlueToRed Thread: The new color is " + Color);
        }
    
    }
}


