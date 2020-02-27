
package main;

import main.exceptions.FlightNotFoundException;

import java.io.Serializable;

public class Passenger {

    public enum CheckinResult {
        DONE,
        ERR_FLIGHT_REFERENCE,
        WARNING_BAGGAGE_WEIGHT,
        WARNING_BAGGAGE_VOLUME,
        WARNING_ALREADY_DONE
    }

    /* =======================
        INSTANCE VARIABLES
    ======================= */

    private String name;
    private String flightReference;
    private String bookingReference;
    private Baggage baggage;
    private boolean checkedIn;

    /* =======================
           CONSTRUCTORS
    ======================= */

    public Passenger(String name, String flightReference, String bookingReference, Baggage baggage, boolean checkedIn) {
        this.name = name;
        this.flightReference = flightReference;
        this.bookingReference = bookingReference;
        this.baggage = baggage;
        this.checkedIn = checkedIn;
    }

    public Passenger(String name, String flightReference, String bookingReference, boolean checkedIn) {
        this(name,flightReference,bookingReference,null,checkedIn);
    }

    /* =======================
            ACCESSORS
    ======================= */

    public String getName() {
        return name;
    }

    public String getFlightReference() {
        return flightReference;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public Baggage getBaggage() {
        return baggage;
    }

    public void setBaggage(Baggage baggage) {
        this.baggage = baggage;
    }

    public boolean getCheckedIn() {
        return checkedIn;
    }
    
    public void setCheckIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public String getLastName() {
        int separator2 = name.lastIndexOf(' ');
        String lastName = name.substring(separator2 + 1);
        return lastName;
    }

    /* =======================
             METHODS
    ======================= */

    public synchronized CheckinResult checkIn(Airport airport) {
        Flight targetFlight;

        if (checkedIn) {
            // Check in is already done -> Abort
            return CheckinResult.WARNING_ALREADY_DONE;
        }

        try {
            targetFlight = airport.getFlightFromRef(flightReference);
            // Baggage weight is exceeded -> Fee
            if (baggage.getWeight() > targetFlight.getBaggageMaxWeight()){
                targetFlight.addPassenger(this);
                checkedIn = true;
                return CheckinResult.WARNING_BAGGAGE_WEIGHT;
            }
            // Baggage volume is exceeded -> Fee
            if (baggage.getVolume() > targetFlight.getBaggageMaxVolume()){
                targetFlight.addPassenger(this);
                checkedIn = true;
                return CheckinResult.WARNING_BAGGAGE_VOLUME;
            }
        }
        catch (FlightNotFoundException e) {
            // Flight reference is wrong -> Abort
            return CheckinResult.ERR_FLIGHT_REFERENCE;
        }

        targetFlight.addPassenger(this);
        checkedIn = true;
        return CheckinResult.DONE;
    }
}
