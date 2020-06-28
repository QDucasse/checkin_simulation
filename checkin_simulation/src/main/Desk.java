package main;
import java.util.concurrent.ThreadLocalRandom;

public class Desk implements Runnable {
    /* =======================
        INSTANCE VARIABLES
	======================= */
    private PassengerQueue passengerQueue;
    private int processingTime;

    /* =======================
          CONSTRUCTORS
	======================= */

    /**
     * The desk is a central element to go through the check-in of the passengers.
     * @param passengerQueue
     *      The queue holding all the passengers waiting to check-in.
     * @param processingTime
     *      The time this desk takes to process one passenger.
     */
    public Desk(PassengerQueue passengerQueue, int processingTime){
        this.passengerQueue = passengerQueue;
        this.processingTime = processingTime;
    }

    public Desk(PassengerQueue passengerQueue){
        this(passengerQueue, 3000);
    }

     /* =======================
             METHODS
    ======================= */

    /**
     * While the queue is not empty, the desk will accept passengers for check-in. Each time a passenger is accepted,
     * the desk will process it for a certain time (processing time ± 2s).
     */
     public void run() {
         while (!passengerQueue.getDone()) {
             try {
                 // randomSign outputs either 1 or -1
                 int randomSign = ThreadLocalRandom.current().nextInt(0, 1) * 2 - 1;
                 // The random integer corresponds to 1000,2000 milliseconds
                 int randomInt = ThreadLocalRandom.current().nextInt(1, 2) * 1000;
                 int randomMillis = randomSign * randomInt;
                 // The thread will sleep for a duration between 1 second (3000 - 2000) and 5 seconds (3000 + 2000)
                 Thread.sleep(processingTime + randomMillis);
             }
             catch (InterruptedException e) { }
             Passenger passengerToCheckIn = passengerQueue.acceptNewPassenger();
         }
     }
}
