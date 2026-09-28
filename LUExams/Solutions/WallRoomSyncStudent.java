
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WallRoomSyncStudent {

    // Vary them for testing
    private static final int NUM_WORKERS = 10;
    private static final int NUM_ROOMS = 3;

    private static final Lock lock = new ReentrantLock();
    private static final Condition canConstruct = lock.newCondition();
    private static final Condition canPaint = lock.newCondition();

    private  boolean[] constructed = new boolean[NUM_WORKERS];
    private  boolean[] painted = new boolean[NUM_WORKERS];

    private int builtCount = 0;
    private int paintedCount = 0;

    private boolean paintingPhase = false;

    public void constructor(int id) throws InterruptedException {
      
        for (int room = 0; room < NUM_ROOMS; room++) {
                   lock.lock();
       try{ while(paintingPhase ||constructed[id]){
            canConstruct.await();
        }
   
             
                constructed[id] = true;
                System.out.println("Constructor " + id + " built wall " + id + " in room " + room);
                builtCount++;
                if(builtCount==NUM_WORKERS){
                    paintingPhase=true;
                    builtCount=0;
                    
                      for(int i=0;i<NUM_WORKERS;i++){
                        constructed[i]=false;
                    }
                    canPaint.signalAll();
                   
                  
                }
              
                lock.unlock();}
            
            catch(InterruptedException e){
                e.printStackTrace();
            }}
         
          
        }

    public void painter(int id) throws InterruptedException {
       
            for (int room = 0; room < NUM_ROOMS; room++) {

                 lock.lock();
               
        try {
            while (!paintingPhase || painted[id]) {
                canPaint.await();
            }
              
                painted[id] = true;
                System.out.println("Painter " + id + " painted wall " + id + " in room " + room);
                paintedCount++;
                if (paintedCount == NUM_WORKERS) {
                     for(int i=0;i<NUM_WORKERS;i++){
                        painted[i]=false;
                    }
                    paintingPhase = false;
                    paintedCount=0;
                    canConstruct.signalAll();
                    
                }
               
                lock.unlock();
            }
         catch (InterruptedException e) {
            e.printStackTrace();
        }}

    }

    public static void main(String[] args) {
       

        WallRoomSyncStudent project = new WallRoomSyncStudent();
        System.out.println("Starting Room Construction & Painting Simulation");
        System.out.println("Number of Rooms     : " + NUM_ROOMS);
        System.out.println("Number of Workers   : " + NUM_WORKERS);
        System.out.println("Number of Walls/Room: " + NUM_WORKERS);
        System.out.println("----------------------------------------------");

        ExecutorService constructorPool = Executors.newFixedThreadPool(NUM_WORKERS);
        ExecutorService painterPool = Executors.newFixedThreadPool(NUM_WORKERS);

        for (int i = 0; i < NUM_WORKERS; i++) {
            int id = i;
            constructorPool.submit(() -> {
                try {
                    project.constructor(id);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        for (int i = 0; i < NUM_WORKERS; i++) {
            int id = i;
            painterPool.submit(() -> {
                try {
                    project.painter(id);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        constructorPool.shutdown();
        painterPool.shutdown();
    }
}
