package main;
import main.exceptions.FlightNotFoundException;
import main.exceptions.NegativeDimensionException;
import main.exceptions.NullDimensionException;
import java.io.IOException;
import java.util.Observable;
import java.util.Random;

public class Desk extends Observable implements Runnable {
    /* =======================
        INSTANCE VARIABLES
	======================= */
    private PassengerQueue passengerQueue;
    private Passenger currentPassenger = null;
    private int processingTime;
    private int deskNumber;
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
    public Desk(Airport airport, PassengerQueue passengerQueue, int processingTime, int deskNumber){
        this.airport= airport;
        this.passengerQueue = passengerQueue;
        this.processingTime = processingTime;
        this.deskNumber = deskNumber;
    }

    public Desk(Airport airport, PassengerQueue passengerQueue, int deskNumber){
        this(airport, passengerQueue, 3000, deskNumber);
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

    /**
     * Output the current passenger.
     * @return currentPassenger
     *      Passenger being checked in.
     */
    public Passenger getCurrentPassenger() {
        return currentPassenger;
    }


     /* =======================
             METHODS
    ======================= */

    /**
     * While the queue is not empty, the desk will accept passengers for check-in. Each time a passenger is accepted,
     * the desk will process it for a certain time (processing time ± 2s).
     */
     public void run() {
         while (!passengerQueue.isEmpty()) {
             try {
                 Passenger passengerToCheckIn = passengerQueue.acceptNewPassenger();
                 currentPassenger = passengerToCheckIn;
                 if (currentPassenger != null) {

                     //Set random baggage dimensions to passenger joining the queue
                     // Log passenger accepted
                     setRandomBaggageToPassenger(passengerToCheckIn);


                     AirportLogger.logDeskPassengerAccepted(this, passengerToCheckIn);
                     //Check-in passenger
                     checkIn(passengerToCheckIn);

                     setChanged();
                     notifyObservers();
                     clearChanged();

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
             }
             catch (InterruptedException | IOException | NullDimensionException | NegativeDimensionException e) { e.printStackTrace(); }
         }
         currentPassenger = null;

         setChanged();
         notifyObservers();
         clearChanged();
     }


    /**
     * Method to associate random baggage dimension to every passenger in the passenger list
     * Dimensions are included between 1 and 200
     * @throws NegativeDimensionException
     *      Baggage dimensions should not be negative.
     * @throws NullDimensionException
     *      Baggage dimensions should not be null.
     */

    public void setRandomBaggageToPassenger(Passenger targetPassenger) throws NegativeDimensionException, NullDimensionException {
        Random random = new Random();
        // All dimensions are integers between 20 and 200
        int bWeight = random.nextInt(20) + 1;
        int bLength = random.nextInt(50) + 10;
        int bWidth = random.nextInt(70) + 20;
        int bHeight = random.nextInt(100) + 40;
        Baggage randomBaggage = new Baggage(bLength, bHeight, bWidth, bWeight);
        targetPassenger.setBaggage(randomBaggage);
    }

    /**
     * Method used for the checkIn button
     * Set the check-in value to true if the passenger last name and booking reference match.
     * Set a baggage for a passenger, display excess fee if applied.
     */

    public void checkIn(Passenger targetPassenger) {

            try {
                String targetFlightRef = targetPassenger.getFlightReference();
                Flight targetFlight = airport.getFlightFromRef(targetFlightRef);
                Passenger.CheckinResult result = targetPassenger.checkIn(airport);

                String fee = Integer.toString(targetFlight.getExcessFee());
                switch(result){
                    case DONE:
                        AirportLogger.logCheckInComplete(this, targetPassenger);
                        break;
                    case ERR_FLIGHT_REFERENCE:
                        AirportLogger.logCheckInErrorFlight(this, targetPassenger);
                        break;
                    case WARNING_ALREADY_DONE:
                        AirportLogger.logCheckInAlreadyDone(this, targetPassenger);
                        break;
                    case WARNING_BAGGAGE_VOLUME:
                        AirportLogger.logCheckInBaggageVolume(this, targetPassenger, fee);
                        break;
                    case WARNING_BAGGAGE_WEIGHT:
                        AirportLogger.logCheckInBaggageWeight(this, targetPassenger, fee);
                        break;
                    case ERR_FLIGHT_IS_FULL:
                        AirportLogger.logCheckInFlightFull(this, targetPassenger);
                        break;
                    case ERR_HOLD_FULL:
                        AirportLogger.logCheckInHoldFull(this, targetPassenger);
                }
            } catch (FlightNotFoundException | IOException e) {
                e.printStackTrace();
            }
    }

}
