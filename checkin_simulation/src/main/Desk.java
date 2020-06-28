package main;

public class Desk implements Runnable {
    /* =======================
        INSTANCE VARIABLES
	======================= */
    private PassengerQueue passengerQueue;

    /* =======================
          CONSTRUCTORS
	======================= */
    public Desk(PassengerQueue passengerQueue){
        this.passengerQueue = passengerQueue;
    }

     /* =======================
            ACCESSORS
    ======================= */

     /* =======================
             METHODS
    ======================= */
     public void run() {
         while (!passengerQueue.getDone()) {
             try { Thread.sleep(100); }
             catch (InterruptedException e) { }
             Passenger passengerToCheckIn = passengerQueue.acceptNewPassenger();
         }
     }
}
