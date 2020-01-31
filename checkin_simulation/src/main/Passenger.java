
package main;

import java.io.Serializable;

public class Passenger {

    public static final int EXCESS_FEE = 12;
    public static final int ERR_FLIGHT_REF = -1;

    private final String name;
    private final Airport airport;
    private String flightReference;
    private String bookingReference;
    private boolean checkedIn;
    private Baggage baggage;

    public Passenger(Airport airport, String name, String flightReference, String bookingReference, boolean checkedIn) {
        this.airport = airport;
        this.name = name;
        this.flightReference = flightReference;
        this.bookingReference = bookingReference;
        this.checkedIn = checkedIn;
        this.baggage = null;
    }

    public synchronized CheckinResult checkIn(String flightReference, Baggage baggage) {
        if (checkedIn == true)
            // Check in is already done, abort her
            return CheckinResult.WARNING_ALREADY_DONE;

        if (!this.flightReference.equalsIgnoreCase(flightReference))
            return CheckinResult.ERR_FLIGHT_REFERENCE;
        return CheckinResult.DONE;
    }

    public String getName() {
        return name;
    }

    public String getFlightReference() {
        return flightReference;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public Baggage getBaggage() {
        return baggage;
    }

    public enum CheckinResult {
        DONE,
        ERR_FLIGHT_REFERENCE,
        ERR_BAGGAGE_WEIGHT,
        ERR_BAGGAGE_DIMENSIONS,
        WARNING_ALREADY_DONE
    }

}
