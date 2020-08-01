package main;
import main.exceptions.BookingRefAndNameNoMatchException;
import main.exceptions.FlightNotFoundException;
import main.exceptions.NegativeDimensionException;
import main.exceptions.NullDimensionException;

import java.io.IOException;
import java.util.Random;

public class Desk implements Runnable {
    /* =======================
        INSTANCE VARIABLES
	======================= */
    private PassengerQueue passengerQueue;
    private int processingTime;
    private int deskNumber;
    private int closingTimeDesk;
    private Airport airport;

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
    public Desk(Airport airport, PassengerQueue passengerQueue, int processingTime, int deskNumber, int closingTimeDesk){
        this.airport= airport;
        this.passengerQueue = passengerQueue;
        this.processingTime = processingTime;
        this.deskNumber = deskNumber;
        this.closingTimeDesk = closingTimeDesk;
    }

    public Desk(Airport airport, PassengerQueue passengerQueue, int deskNumber){
        this(airport, passengerQueue, 3000, deskNumber, 10000);
    }

    /* =======================
           ACCESSORS
    ======================= */

    /**
     * Output the actual desk number.
     * @return deskNumber
     *      Number of the desk.
     */
    public int getDeskNumber() {
        return deskNumber;
    }


     /* =======================
             METHODS
    ======================= */

    /**
     * While the queue is not empty, the desk will accept passengers for check-in. Each time a passenger is accepted,
     * the desk will process it for a certain time (processing time Â± 2s).
     */
     public void run() {
         while (!passengerQueue.getDone()) {
             try {

                 Passenger passengerToCheckIn = passengerQueue.acceptNewPassenger();
                 // Log passenger accepted
                 AirportLogger.logDeskPassengerAccepted(this, passengerToCheckIn);
                 //Check-in passenger
                 checkIn(passengerToCheckIn);
                 // Creation of a random time for the check-in to happen.
                 Random random = new Random();
                 // The randomSign outputs either 1 or -1
                 int randomSign = random.nextInt(2) * 2 - 1;
                 // The random integer corresponds to 1000,2000 milliseconds
                 int randomInt = random.nextInt(3) * 1000;
                 int randomMillis = randomSign * randomInt;
                 // The thread will sleep for a duration between 1 second (3000 - 2000) and 5 seconds (3000 + 2000)
                 int totalTime = processingTime + randomMillis;
                 Thread.sleep(totalTime);

                 AirportLogger.logDeskTimeTaken(this, totalTime);
             }
             catch (InterruptedException | IOException e) { e.printStackTrace(); }
         }
     }


    /**
     * Method to associate random baggage dimension to every passenger in the passenger list
     * Dimensions are included between 1 and 200
     * @throws NegativeDimensionException
     *      Baggage dimensions should not be negative.
     * @throws NullDimensionException
     *      Baggage dimensions should not be null.
     */

    public Baggage setRandomBaggage() throws NegativeDimensionException, NullDimensionException {
        Random random = new Random();
        int bWeight = random.nextInt(200);
        bWeight += 1;
        int bLength = random.nextInt(200);
        bLength += 1;
        int bWidth = random.nextInt(200);
        bWidth += 1;
        int bHeight = random.nextInt(200);
        bHeight += 1;
        Baggage inputBaggage = new Baggage(bLength, bHeight, bWidth, bWeight);
        return inputBaggage;
    }

    /**
     * Method used for the checkIn button
     * Set the check-in value to true if the passenger last name and booking reference match.
     * Set a baggage for a passenger, display excess fee if applied.
     */

    private void checkIn(Passenger targetPassenger) {

            try {
                Baggage inputBaggage = setRandomBaggage();
                targetPassenger.setBaggage(inputBaggage);
                String pName = targetPassenger.getFullName();
                String targetFlightRef = targetPassenger.getFlightReference();
                Flight targetFlight = airport.getFlightFromRef(targetFlightRef);
                Passenger.CheckinResult checkInResult = targetPassenger.checkIn(airport);
                String fee = Integer.toString(targetFlight.getExcessFee());
                switch(checkInResult){
                    case DONE:
                        System.out.println(pName + ": this passenger is now checked-in with his baggage.");
                        break;
                    case ERR_FLIGHT_REFERENCE:
                        System.out.println(pName + ": this flight reference associated to this passenger does not exist.");
                        break;
                    case WARNING_ALREADY_DONE:
                        System.out.println(pName + ": this passenger is already checked-in.");
                        break;
                    case WARNING_BAGGAGE_VOLUME:
                        System.out.println(pName + ": the dimensions are exceeded, the passenger has to pay: " + fee + "£");
                        break;
                    case WARNING_BAGGAGE_WEIGHT:
                        System.out.println(pName + ": the weight is exceeded, the passenger has to pay: " + fee + "£");
                        break;
                    case WARNING_FLIGH_IS_FULL:
                        System.out.println(pName + ": this flight is full");
                        break;
                }
            } catch (NullDimensionException e) {
            e.printStackTrace();
        } catch (NegativeDimensionException e) {
            e.printStackTrace();
        } catch (FlightNotFoundException e) {
                e.printStackTrace();
            }
    }

}
