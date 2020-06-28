package main;

public class WaitingLine implements Runnable {
    /* =======================
        INSTANCE VARIABLES
	======================= */
    private PassengerQueue passengerQueue;

     /* =======================
          CONSTRUCTORS
	======================= */
    /**
     * The waiting line is the "producer" of new passengers that need to check-in. It holds the actual passenger queue.
     *
     * @param passengerQueue
     *      The queue holding all the passengers waiting to check-in.
     */
     public WaitingLine(PassengerQueue passengerQueue){
         this.passengerQueue = passengerQueue;
     }

     /* =======================
             METHODS
    ======================= */

    /**
     * While the queue is not empty, the waiting line will propose passengers for check-in.
     */
     public void run() {
         for (int i = 0; i < 8; i++) {
             try {
                 Thread.sleep(50);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
             passengerQueue.proposeNewPassenger();
         }
         passengerQueue.setDone();
     }


}
