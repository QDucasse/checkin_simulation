
package main;

import main.exceptions.FlightNotFoundException;

import java.util.Objects;

public class Passenger {
    /**
     * Different results the check-in operation can output.
     */
    public enum CheckinResult {
        /**
         * Check-in passed without issues.
         */
        DONE,

        /**
         * Flight reference is erroneous.
         */
        ERR_FLIGHT_REFERENCE,

        /**
         * Baggage weight exceeds the maximum set by the flight.
         */
        WARNING_BAGGAGE_WEIGHT,

        /**
         * Baggage volume exceeds the maximum set by the flight
         */
        WARNING_BAGGAGE_VOLUME,

        /**
         * Check-in is already done.
         */
        WARNING_ALREADY_DONE,

        /**
         * Hold is full, cannot accept passenger and the luggage
         */
        ERR_HOLD_FULL,
        /**
         * Flight is full
         */
        ERR_FLIGHT_IS_FULL
    }

    /* =======================
        INSTANCE VARIABLES
    ======================= */

    private String firstName;
    private String lastName;
    private String flightReference;
    private String bookingReference;
    private Baggage baggage;
    private boolean checkedIn;
    private String priority;
    private CheckinResult result;


    /* =======================
           CONSTRUCTORS
    ======================= */

    /**
     * Passenger class holding name (as first name and last name) flight reference, booking reference and an indication
     * on the check-in status.
     * @param firstName
     *    The first name of the passenger.
     * @param lastName
     *    The last name of the passenger.
     * @param flightReference
     *    The flight reference of the flight the passenger wish to take.
     * @param bookingReference
     *    The booking reference associated with the passenger (<Capitalised Letter> <Capitalised Letter> <Digit> <Capitalised Letter> <Capitalised Letter> <Digit>).
     * @param baggage
     *    The baggage the passenger travels with.
     * @param checkedIn
     *    Status of the check-in operation (done or not).
     * @param priority
     *     Passenger priority (Economic, Business or First class)
     */
    public Passenger(String firstName, String lastName, String flightReference, String bookingReference, Baggage baggage, boolean checkedIn, String priority) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.flightReference = flightReference;
        this.bookingReference = bookingReference;
        this.baggage = baggage;
        this.checkedIn = checkedIn;
        this.priority = priority;
    }

    public Passenger(String firstName, String lastName, String flightReference, String bookingReference, boolean checkedIn, String priority) {
        this(firstName,lastName,flightReference,bookingReference,null,checkedIn, priority);
    }

    public Passenger(String firstName, String lastName, String flightReference, String bookingReference, boolean checkedIn) {
        this(firstName,lastName,flightReference,bookingReference,null,checkedIn, "Economic");
    }

    /* =======================
            ACCESSORS
    ======================= */

    /**
     * Getter of the first name.
     * @return firstName
     *    The first name of the passenger.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Getter of the last name.
     * @return lastName
     *    The last name of the passenger.
     */
    public String getLastName() {
        return lastName;
    }


    /**
     * Getter of the last name.
     * @return fullName
     *    The full name of the passenger (first name + last name).
     */
    public String getFullName() { return firstName + " " + lastName; }

    /**
     * Getter of the flight reference.
     * @return flightReference
     *    The flight reference of the passenger.
     */
    public String getFlightReference() {
        return flightReference;
    }

    /**
     * Getter of the booking reference.
     * @return bookingReference
     *    The booking reference of the passenger.
     */
    public String getBookingReference() {
        return bookingReference;
    }

    /**
     * Getter of the baggage.
     * @return baggage
     *    The baggage of the passenger.
     */
    public Baggage getBaggage() {
        return baggage;
    }

    /**
     * Setter of the baggage.
     * @param baggage
     *    The baggage to be set to the passenger.
     */
    public void setBaggage(Baggage baggage) {
        this.baggage = baggage;
    }

    /**
     * Getter of the check-in status
     * @return checkedIn
     *    The status of the check-in operation (done or not).
     */
    public boolean getCheckedIn() {
        return checkedIn;
    }

    /**
     * Getter for priority status
     * @return priority
     *     Status of the passenger priority class (Economic, Business, First class)
     */
    public String getPriority() {
        return priority;
    }

    /* =======================
             METHODS
    ======================= */

    /**
     * Check-in operation performed by the passenger in a given airport.
     * @param airport
     *    The airport the passenger wishes to check-in.
     * @return checkInResult
     *    The result of the check-in operation (enumeration above).
     */
    public synchronized CheckinResult checkIn(Airport airport) {
        Flight targetFlight;

        if (checkedIn) {
            // Check in is already done -> Abort
            this.result = CheckinResult.WARNING_ALREADY_DONE;
            return result;
        }

        try {
            targetFlight = airport.getFlightFromRef(flightReference);
            // Baggage weight is exceeded -> Fee

            if (targetFlight.isFull())
                this.result = CheckinResult.ERR_FLIGHT_IS_FULL;

            else if (targetFlight.totalVolume() + baggage.getVolume() > targetFlight.getMaxVolume()) {
                this.result = CheckinResult.ERR_HOLD_FULL;
            }

            else if (baggage.getWeight() > targetFlight.getBaggageMaxWeight()){
                targetFlight.addPassenger(this);
                checkedIn = true;
                this.result = CheckinResult.WARNING_BAGGAGE_WEIGHT;
            }
            // Baggage volume is exceeded -> Fee
            else if (baggage.getVolume() > targetFlight.getBaggageMaxVolume()){
                targetFlight.addPassenger(this);
                checkedIn = true;
                this.result = CheckinResult.WARNING_BAGGAGE_VOLUME;
            }
            else {
                targetFlight.addPassenger(this);
                checkedIn = true;
                this.result = CheckinResult.DONE;
            }

        }
        catch (FlightNotFoundException e) {
            // Flight reference is wrong -> Abort
            this.result = CheckinResult.ERR_FLIGHT_REFERENCE;
        }
        return result;
    }

    public CheckinResult getResult() {
        return result;
    }

    /* =======================
        OVERRIDDEN METHODS
    ======================= */


    /**
     * Returns the textual representation of a passenger.
     * @return string
     *      Textual representation of a passenger
     */
    @Override
    public String toString(){
        return getFullName() + " with booking " + getBookingReference() + "\n";
    }



    /**
     * Test equality between a passenger and another object by comparing their attributes.
     * @param o
     *      The object to compare to the instance
     * @return
     *      True if they are the same, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return checkedIn == passenger.checkedIn &&
                Objects.equals(firstName, passenger.firstName) &&
                Objects.equals(lastName, passenger.lastName) &&
                Objects.equals(flightReference, passenger.flightReference) &&
                Objects.equals(bookingReference, passenger.bookingReference) &&
                Objects.equals(baggage, passenger.baggage);
    }

    /**
     * Generates a correct hashcode with the object attributes.
     * @return hash
     *      The hashcode of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, flightReference, bookingReference, baggage, checkedIn);
    }
}
