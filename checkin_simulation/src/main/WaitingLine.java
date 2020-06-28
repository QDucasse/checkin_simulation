package main;

public class WaitingLine implements Runnable {
    /* =======================
        INSTANCE VARIABLES
	======================= */
    private PassengerQueue passengerQueue;

     /* =======================
          CONSTRUCTORS
	======================= */
     public WaitingLine(PassengerQueue passengerQueue){
         this.passengerQueue = passengerQueue;
     }

     /* =======================
            ACCESSORS
    ======================= */

     /* =======================
             METHODS
    ======================= */
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
