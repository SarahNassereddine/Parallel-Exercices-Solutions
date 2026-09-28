import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TrafficLightSystem_ToStudent_V1 {
    private static String colorTurn="GREEN";
    private static final Lock lock = new ReentrantLock();
    private static final Condition canTake= lock.newCondition();

    private class GreenLight extends Thread {

        @Override
        public void run() {

            while (true) {
                lock.lock();
              try{  while(colorTurn !="GREEN"){
                    canTake.await();
                }
                System.out.println("GO");
                sleep(1000);
                colorTurn="YELLOW";
                canTake.signalAll();
            }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        finally{
            lock.unlock();
        }
        }
        }
    }

    private class YellowLight extends Thread {

        @Override
        public void run() {

            while (true) {
                lock.lock();
                try{
                    while(colorTurn!="YELLOW"){
                        canTake.await();
                    }
                System.out.println("Prepare to STOP");
                sleep(1000);
                colorTurn="RED";
                canTake.signalAll();
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                finally{lock.unlock();

                }
            }

        }
    }

    private class RedLight extends Thread {

        @Override
        public void run() {

            while (true) {
                lock.lock();
                try{
                    while(colorTurn!="RED"){
                        canTake.await();
                    }
                System.out.println("STOP");
            sleep(1000);
            colorTurn="GREEN";
            canTake.signalAll();
            }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                finally{
                    lock.unlock();
                }
            }

        }
    }

    public static void main(String[] args) {
        TrafficLightSystem_ToStudent_V1 system = new TrafficLightSystem_ToStudent_V1();

        Thread greenLightThread;
        greenLightThread = system.new GreenLight();
        Thread yellowLightThread = system.new YellowLight();
        Thread redLightThread = system.new RedLight();

        greenLightThread.start();
        yellowLightThread.start();
        redLightThread.start();
    }
}
