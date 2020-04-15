package main;

import main.exceptions.BookingRefAndNameNoMatchException;
import main.exceptions.FlightNotFoundException;

import java.util.ArrayList;
import java.util.Observable;

public class Airport extends Observable implements Runnable {

    /**
     * The patterns below are used when checking if the loaded flights and bookings are meaningful.
     * In order to be accepted, a flight number has to respect the format:
     *     <Capitalised Letter> <Capitalised Letter> <Digit> <Digit> <Digit>
     * In order to be accepted, a booking reference has to respect the format:
     *     <Capitalised Letter> <Capitalised Letter> <Digit> <Capitalised Letter> <Capitalised Letter> <Digit>
     *
     * The regular expressions translate these rules.
     */
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

    /**
     * Airport class holding a list of all passengers, checked-in or not, all flights and managing the check-in
     * operation.
     * @param passengerList
     *    List of passengers composing the airport.
     * @param flightList
     *    List of flights composing the airport.
     */
    public Airport(ArrayList<Passenger> passengerList, ArrayList<Flight> flightList){
        this.passengerList = passengerList;
        this.flightList = flightList;
    }

    public Airport(){
        this.passengerList = new ArrayList<Passenger>();
        this.flightList = new ArrayList<Flight>();
    }

    /* =======================
            ACCESSORS
    ======================= */

    /**
     * Getter of the FlightList instance variable
     * @return The instance variable FlightList
     */
    public ArrayList<Flight> getFlightList() {
        return flightList;
    }

    /**
     * Getter of the PassengerList instance variable
     * @return The instance variable FlightList
     */
    public ArrayList<Passenger> getPassengerList() {
        return passengerList;
    }

    
    /**
     * Look for a flight that has the given flight reference.
     * @param flightReference
     *    The flight reference to look for.
     * @return flight
     *    The flight with the given flight reference.
     * @throws FlightNotFoundException
     *    In case no flight holds the flight reference.
     */
    public Flight getFlightFromRef(String flightReference) throws FlightNotFoundException {
        for (Flight flight : flightList){
            if (flight.getFlightRef().equals(flightReference)) {
                return flight;
            }
        }
        throw new FlightNotFoundException("Flight reference not found");
    }

    /**
     * Look for a passenger that has a given name and booking reference.
     * @param bookingReference
     *    The booking reference to look for.
     * @param lastName
     *    The name to look for.
     * @return passenger
     *    The passenger with the given booking reference and name.
     * @throws BookingRefAndNameNoMatchException
     *    In case no passenger holds the given attributes.
     */
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

    /**
     * Add a passenger to the passenger list.
     * @param passenger
     *    The passenger to add.
     */
    public void addPassenger(Passenger passenger){
        passengerList.add(passenger);
    }

    /**
     * Add a flight to the flight list.
     * @param flight
     *    The flight to add.
     */
    public void addFlight(Flight flight){
        flightList.add(flight);
    }

    /**
     * Present a textual report consisting of all the different flights and their attributes (number of passengers,
     * weight, volume) as well as the completion to each of them (e.g. weight: 300/500).
     * @return report
     *    String representation of the report.
     */
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

    /**
     * Once the files are loaded with the serializer, the airport runs a check in order to delete all flights which
     * reference does not follow the rule defined at the beginning and all passengers which booking reference does not
     * follow the rule defined at the beginning.
     */
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

    public void run(){

    }
}
