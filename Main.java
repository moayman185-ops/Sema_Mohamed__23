class MySemaphore {
    private int permits;

    public MySemaphore(int permits) {
        this.permits = permits;
    }

    public synchronized void acquire() {
        while (permits == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        permits--;
    }

    public synchronized void release() {
        permits++;
        notifyAll();
    }
}
class Uploader extends Thread{
   private int id;
    public Uploader(int id){
        this.id = id;
    }
    public void run(){
        for(int j=1;j<=5;j++){
            int value = id*1000+j;
            Main.mutex.acquire();
            try{
                Main.slots[Main.next] = value;
                Thread.sleep(1);
                Main.next++;
            }catch(InterruptedException e){
            } 
            Main.mutex.release();
        }
    }
}

public class Main {
    static int[] slots = new int[30];                       
    static int next = 0;
    static MySemaphore mutex = new MySemaphore(1);

    public static void main(String[] args) 
        throws InterruptedException{
        Uploader[] uploaders = new Uploader[6];
        for(int i = 0; i < 6; i++){
            uploaders[i] = new Uploader(i+1);
            uploaders[i].start();
        }
        for(int i=0;i<6;i++){
            uploaders[i].join();
        }
        
        
        System.out.println("Slots filled: " + next);

        int sum = 0;
        int emptySlots = 0;
        for(int i=0;i<30;i++){
            sum += slots[i];
            if(slots[i] == 0){
                emptySlots++;
            }
        }
        System.out.println("Sum of all values: " + sum);
        System.out.println("Empty slots: " + emptySlots);    
        }
    }
