package main;

import java.io.IOException;

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
         while(!passengerQueue.isEmpty()) {
             try {
                 Thread.sleep(10);
                 if (passengerQueue.peek() != null)
                    AirportLogger.logFirstPassengerInQueue(passengerQueue.peek());
             } catch (InterruptedException | IOException e) {
                 e.printStackTrace();
             }
             passengerQueue.proposeNewPassenger();
         }
         passengerQueue.setDone();
     }


    /**
     * Getter for passenger queue
     * @return passengerQueue
     *      List of passenger waiting in the queue
     */
    public PassengerQueue getPassengerQueue() {
        return passengerQueue;
    }



}
