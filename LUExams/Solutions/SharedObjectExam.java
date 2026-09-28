import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedObjectExam {
    public StringBuilder sharedString;
    private final static Lock lock= new ReentrantLock();
    private static final Condition canTake=lock.newCondition();
    private static int state=0;

    
    public SharedObjectExam() {
        sharedString = new StringBuilder(); 
    }

    public void appendString(String text) {
            sharedString.append(text);
    }

   class Task1 implements Runnable {
        public void run() {
            lock.lock();
           try{ while(state!=0){
                canTake.await();
            }
            appendString("T1");
            state++;
            System.out.println("END OF T1"); 
            canTake.signalAll();

          } 
        catch(InterruptedException e){
            e.printStackTrace();
        }
        finally{lock.unlock();}
        }
    }

    class Task2 implements Runnable {
        public void run() {
            lock.lock();
            try{
            while(state!=1){
                canTake.await();
            }
            appendString("T2");
            state++;
            System.out.println("END OF T2");
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

    class Task3 implements Runnable {
        public void run() {
            lock.lock();
           try{ while(state!=2){
                if(state>2){
                    canTake.signalAll();
                    return;
                }
                canTake.await();
            }
        appendString("T3");
        System.out.println("END OF T3");  
        state++;
        canTake.signalAll();  
        }
    catch(InterruptedException e){
        e.printStackTrace();
    }
    finally{lock.unlock();}
    }
    }

    class Task4 implements Runnable {
        public void run() {
            lock.lock();
            try{
                while(state!=3){
                    if(state==5){
                        canTake.signalAll();
                        return;
                    }
                    canTake.await();
                }
                appendString("T4");
                state=5;
                System.out.println("END OF T4"); 
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

    class Task5 implements Runnable {
        public void run() {
            lock.lock();
            try{while(state!=2){
                if(state>2){
                    canTake.signalAll();
                    return;
                }
                canTake.await();
            }
            appendString("T5");
            state=5;
            
            System.out.println("END OF T5"); 
            canTake.signalAll();
        }
    catch(InterruptedException e)
{

    e.printStackTrace();
}
finally{
    lock.unlock();
}}
    }

    class EndTask implements Runnable {
        public void run() {
            lock.lock();
            try{

    while(state!=5){
        canTake.await();
            }
            appendString("END");
            System.out.println("THE END");
            System.out.println(sharedString.toString());
            
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
            finally{lock.unlock();}
        
            }
        
    }

    public static void main(String[] args) throws InterruptedException {
        SharedObjectExam sharedObject = new SharedObjectExam();

        Thread t1 = new Thread(sharedObject.new Task1());
        Thread t2 = new Thread(sharedObject.new Task2());
        Thread t3 = new Thread(sharedObject.new Task3());
        Thread t4 = new Thread(sharedObject.new Task4());
        Thread t5 = new Thread(sharedObject.new Task5());
        Thread endThread = new Thread(sharedObject.new EndTask());

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        endThread.start();
        if(endThread.isAlive())endThread.join();
        System.out.println("END OF MAIN as endThread is Alive : " + endThread.isAlive());
      
    }
}
