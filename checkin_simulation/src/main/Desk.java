package main;
import java.util.concurrent.ThreadLocalRandom;

public class Desk implements Runnable {
    /* =======================
        INSTANCE VARIABLES
	======================= */
    private PassengerQueue passengerQueue;
    private int processingTime;
    private int deskNumber;

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
    public Desk(PassengerQueue passengerQueue, int processingTime, int deskNumber){
        this.passengerQueue = passengerQueue;
        this.processingTime = processingTime;
        this.deskNumber = deskNumber;
    }

    public Desk(PassengerQueue passengerQueue, int deskNumber){
        this(passengerQueue, 3000, deskNumber);
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
                 Passenger passengerToCheckIn = passengerQueue.acceptNewPassenger();
                 System.out.println("Desk nº" + deskNumber + ": accepted to check-in the client " + passengerToCheckIn.getFullName());
                 // randomSign outputs either 1 or -1
                 int randomSign = ThreadLocalRandom.current().nextInt(0, 2) * 2 - 1;
                 // The random integer corresponds to 1000,2000 milliseconds
                 int randomInt = ThreadLocalRandom.current().nextInt(0, 3) * 1000;
                 int randomMillis = randomSign * randomInt;
                 // The thread will sleep for a duration between 1 second (3000 - 2000) and 5 seconds (3000 + 2000)
                 int totalTime = processingTime + randomMillis;
                 Thread.sleep(totalTime);
                 System.out.println("Desk nº" + deskNumber + ": " + totalTime + "ms to check-in the client");
             }
             catch (InterruptedException e) { }
         }
     }
}
