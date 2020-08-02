package main;

import main.exceptions.BookingRefAndNameNoMatchException;
import main.exceptions.EmptyPassengerListException;
import main.exceptions.FlightNotFoundException;

import java.util.ArrayList;
import java.util.Objects;
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
    private ArrayList<Desk> deskList = new ArrayList<Desk>();
    private Thread[] deskThreadList;
    private WaitingLine waitingLine;
    private PassengerQueue passengerQueue;
  
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
    public Airport(ArrayList<Passenger> passengerList, ArrayList<Flight> flightList) throws FlightNotFoundException, EmptyPassengerListException {
        // Load lists
        this.passengerList = passengerList;
        this.flightList = flightList;
        this.checkLists();
        this.passengerQueue = new PassengerQueue(this.passengerList);
        // Setup Desks
        Desk desk1 = new Desk(this, this.passengerQueue, 1);
        Desk desk2 = new Desk(this, this.passengerQueue, 2);
        Desk desk3 = new Desk(this, this.passengerQueue, 3);
        this.deskList.add(desk1);
        this.deskList.add(desk2);
        this.deskList.add(desk3);
        // Setup waiting line
        this.waitingLine = new WaitingLine(this.passengerQueue);
    }

    public Airport() throws EmptyPassengerListException, FlightNotFoundException {
        // Load both lists from files
        ArrayList<Passenger> passengerList = Serializer.defaultFileToPassengerList();
        ArrayList<Flight> flightList = Serializer.defaultFileToFlightList();
        // Load lists
        this.passengerList = passengerList;
        this.flightList = flightList;
        this.checkLists();
        this.passengerQueue = new PassengerQueue(this.passengerList);
        // Setup Desks
        Desk desk1 = new Desk(this, this.passengerQueue, 1);
        Desk desk2 = new Desk(this, this.passengerQueue, 2);
        Desk desk3 = new Desk(this, this.passengerQueue, 3);
        this.deskList.add(desk1);
        this.deskList.add(desk2);
        this.deskList.add(desk3);
        // Setup waiting line
        this.waitingLine = new WaitingLine(this.passengerQueue);
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
     * @return The instance variable PassengerList
     */
    public ArrayList<Passenger> getPassengerList() {
        return passengerList;
    }

    /**
     * Getter of the DeskList instance variable
     * @return The instance variable DeskList
     */
    public ArrayList<Desk> getDeskList() {
        return deskList;
    }


    /**
     * Getter of the waitingLine instance variable
     * @return The instance variable waitingLine
     */
    public WaitingLine getWaitingLine() {
        return waitingLine;
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
     * @throws FlightNotFoundException
     *      Happens if an already checked-in passenger is checked-in on an unknown flight.
     */
    public void checkLists() throws FlightNotFoundException {
        // Remove passengers with wrong booking references and flights with wrong references
        flightList.removeIf(flight -> !flight.getFlightRef().matches(PATTERN_FLIGHT));
        passengerList.removeIf(passenger -> !passenger.getBookingReference().matches(PATTERN_BOOKING));
        // Add the already checked-in passengers to their correct flights
        checkAlreadyCheckedIn();
    }

    /**
     * Runs through the list of passengers to check if some are already checked-in. If a passenger is already checked-in,
     * they are added to the corresponding flight and removed from the list.
     * @throws FlightNotFoundException
     *      The flight a passenger is checked-in is unknown from the airport.
     */
    public void checkAlreadyCheckedIn() throws FlightNotFoundException {
        ArrayList<Passenger> newPassengerList = new ArrayList<Passenger>();
        for (Passenger passenger : passengerList){
            // If passenger already checked-in, add them to the flight and remove them from the list
            if (passenger.getCheckedIn()) {
                Flight correspondingFlight = getFlightFromRef(passenger.getFlightReference());
                correspondingFlight.addPassenger(passenger);
            }
            // Else (passenger not checked-in) keep it in the list
            else {
                newPassengerList.add(passenger);
            }
        }
        this.passengerList = newPassengerList;
    }

    public void run(){
        // Desk thread creation
        deskThreadList = new Thread[deskList.size()];
        for (int i = 0; i < deskList.size(); i++){
            deskThreadList[i] = new Thread(deskList.get(i));
            deskThreadList[i].start();
        }
        // Waiting line thread creation
        Thread waitingLineThread = new Thread(waitingLine);
        waitingLineThread.start();

        // Joining the different threads
        try {
            for (int i = 0; i < deskList.size(); i++){
                deskThreadList[i].join();
            }
            waitingLineThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /* =======================
       OVERRIDDEN METHODS
   ======================= */

    /**
     * Textual representation of an airport.
     * @return output
     *      The textual representation of the airport.
     */
    @Override
    public String toString() {
        return "Airport{" +
                "passengerList=" + passengerList +
                ", flightList=" + flightList +
                '}';
    }

    /**
     * Test equality between an airport and another object by comparing their attributes.
     * @param o
     *      The object to compare to the instance
     * @return
     *      True if they are the same, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return Objects.equals(passengerList, airport.passengerList) &&
                Objects.equals(flightList, airport.flightList);
    }

    /**
     * Generates a correct hashcode with the object attributes.
     * @return hash
     *      The hashcode of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(passengerList, flightList);
    }
}
