
package main;

import java.io.Serializable;

public class Passenger {

    public static final int EXCESS_FEE = 12;
    public static final int ERR_FLIGHT_REF = -1;

    private String name;
    private String flightReference;
    private String bookingReference;
    private boolean checkedIn;
    private Baggage baggage;

    public Passenger(String name, String flightReference, String bookingReference, Baggage baggage, boolean checkedIn) {
        this.name = name;
        this.flightReference = flightReference;
        this.bookingReference = bookingReference;
        this.baggage = baggage;
        this.checkedIn = checkedIn;
    }

    public Passenger(String name, String flightReference, String bookingReference, boolean checkedIn) {
        new Passenger(name,flightReference,bookingReference,null,checkedIn);
    }

    public synchronized CheckinResult checkIn(String flightReference, Baggage baggage) {
        if (checkedIn)
            // Check in is already done
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

    public void setBaggage(Baggage baggage) {
        this.baggage = baggage;
    }

    public String getLastName(String fullName)
    {
       int separator2 = fullName.lastIndexOf(' ');
       String lastName = fullName.substring(separator2 + 1);
       return lastName;
    }

    public void setCheckIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public enum CheckinResult {
        DONE,
        ERR_FLIGHT_REFERENCE,
        ERR_BAGGAGE_WEIGHT,
        ERR_BAGGAGE_DIMENSIONS,
        WARNING_ALREADY_DONE
    }



}
