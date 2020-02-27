package main;

import main.exceptions.BookingRefAndNameNoMatchException;
import main.exceptions.FlightNotFoundException;

import java.util.ArrayList;

public class Airport {

    private static final String PATTERN_FLIGHT = "(?<![A-Z])[A-Z]{2}\\d{3}(?!\\d)";
    private static final String PATTERN_BOOKING = "(?<![A-Z])[A-Z]{2}\\d{1}[A-Z]{2}\\d{1}(?!\\d)";

    /* =======================
        INSTANCE VARIABLES
    ======================= */

    private ArrayList<Passenger> passengerList;
    private ArrayList<Flight> flightList;

    /* =======================
           CONSTRUCTORS
    ======================= */

    public Airport(){
        this.passengerList = new ArrayList<Passenger>();
        this.flightList = new ArrayList<Flight>();
    }

    /* =======================
            ACCESSORS
    ======================= */

    public ArrayList<Flight> getFlightList() {
        return flightList;
    }

    public ArrayList<Passenger> getPassengerList() {
        return passengerList;
    }

    public Flight getFlightFromRef(String flightReference) throws FlightNotFoundException {
        for (Flight flight : flightList){
            if (flight.getFlightRef().equals(flightReference)) {
                return flight;
            }
        }
        throw new FlightNotFoundException("Flight reference not found");
    }

    public Passenger getPassengerFromBookingRefAndName(String bookingReference, String lastName) throws BookingRefAndNameNoMatchException {
        for (Passenger passenger : passengerList){
            if (passenger.getBookingReference().equals(bookingReference) && passenger.getLastName().equals(lastName)) {
                return passenger;
            }
        }
        throw new BookingRefAndNameNoMatchException("Booking reference and name do not match");
    }

    /* =======================
             METHODS
    ======================= */

    public void addPassenger(Passenger passenger){
        passengerList.add(passenger);
    }

    public void addFlight(Flight flight){
        flightList.add(flight);
    }

    public String outputReport(){
        StringBuilder globalReport = new StringBuilder();
        for (Flight flight : flightList){
            String flightReport;
            flightReport = String.format("========================\n" +
                                         "Report for flight %s: \n" +
                                         "Number of passengers: %d/%d\n" +
                                         "Weight: %d/%d\n" +
                                         "Volume: %d/%d\n\n",
                                         flight.getFlightRef(),flight.totalPassengers(),
                                         flight.getMaxPassengers(),flight.totalWeight(),
                                         flight.getMaxWeight(), flight.totalVolume(),
                                         flight.getMaxVolume());
            if (!flight.checkPassengers()) flightReport += "The number of passengers is exceeded!\n";
            if (!flight.checkWeight()) flightReport += "The weight is exceeded!\n";
            if (!flight.checkVolume()) flightReport += "The volume is exceeded!\n";
            flightReport = flightReport + "========================\n";
            globalReport.append(flightReport);
        }
        return globalReport.toString();
    }

    public void checkLists(){
        for (Flight flight : flightList){
            if (!flight.getFlightRef().matches(PATTERN_FLIGHT)){
                flightList.remove(flight);
            }
        }
        for (Passenger passenger : passengerList){
            if (!passenger.getBookingReference().matches(PATTERN_BOOKING)){
                passengerList.remove(passenger);
            }
        }
    }
}
