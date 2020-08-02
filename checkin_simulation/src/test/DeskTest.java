package test;

import junit.framework.TestCase;
import main.*;
import main.exceptions.EmptyPassengerListException;
import main.exceptions.FlightNotFoundException;
import main.exceptions.NegativeDimensionException;
import main.exceptions.NullDimensionException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class DeskTest extends TestCase {

    private Airport dummyAirport;
    private Flight dummyFlight1;
    private Flight dummyFlight2;
    private Passenger dummyPassenger1;
    private Passenger dummyPassenger2;
    private Passenger dummyPassenger3;
    private Passenger dummyPassenger4;
    private Passenger dummyPassenger5;
    private Passenger dummyPassenger6;
    private Baggage dummyBaggage1;
    private Baggage dummyBaggage2;
    private Baggage dummyBaggage3;
    private PassengerQueue dummyPassengerQueue;
    private Desk dummyDesk;

    /**
     * Sets up an environment before every test. Here, five passengers are going in two different flights within an
     * airport. They all have their own baggage. The baggages are designed as follows:
     * - baggage1: normal baggage
     * - baggage2: super heavy baggage
     * - baggage3: super wide baggage
     * Passengers 1 and 2 have the baggage 1. Passengers 3 and 4 the baggage 2. Passenger 5 has the baggage 3.
     */
    @Before
    public void setUp() throws NullDimensionException, EmptyPassengerListException, FlightNotFoundException {
        // Case 1: Already checked in
        try {
            dummyBaggage1 = new Baggage(10, 20, 30, 9);
        } catch (NegativeDimensionException e) {
            e.printStackTrace();
        }
        dummyPassenger1 = new Passenger("John", "Doe", "EH145", "1225", true, "Economic");
        dummyPassenger1.setBaggage(dummyBaggage1);
        // Case 2: Wrong flight reference
        dummyPassenger2 = new Passenger( "Jane", "Doe", "AA000", "1226", false, "Business");
        dummyPassenger2.setBaggage(dummyBaggage1);
        // Case 3: Warning weight
        try {
            dummyBaggage2 = new Baggage(10, 20, 30, 100);
        } catch (NegativeDimensionException e) {
            e.printStackTrace();
        }
        dummyPassenger3 = new Passenger( "Bill", "Murray", "EH145", "1227", false, "First class");
        dummyPassenger3.setBaggage(dummyBaggage2);
        // Case 4: Warning volume
        try {
            dummyBaggage3 = new Baggage(100, 100, 100, 9);
        } catch (NegativeDimensionException e) {
            e.printStackTrace();
        }
        dummyPassenger4 = new Passenger( "George", "Clooney", "EH145", "1228", false, "Economic");
        dummyPassenger4.setBaggage(dummyBaggage3);
        // Case 5: Working fine
        dummyPassenger5 = new Passenger( "Brad", "Pitt", "EH145", "1229", false, "Business");
        dummyPassenger5.setBaggage(dummyBaggage1);

        // Case 6: Flight Full
        dummyPassenger6 = new Passenger( "Brad", "Pitt", "EH145", "1229", false, "First class");
        dummyPassenger6.setBaggage(dummyBaggage1);

        dummyFlight1 = new Flight("Edinburgh", "RyanAir", 2, 100, 100, "EH145");
        dummyFlight2 = new Flight("Paris", "RyanAir", 2, 100, 100, "FR145");

        dummyFlight1.addPassenger(dummyPassenger1);
        dummyFlight2.addPassenger(dummyPassenger2);
        dummyFlight2.addPassenger(dummyPassenger3);

        ArrayList<Passenger> passengerList = new ArrayList<Passenger>();
        passengerList.add(dummyPassenger1);

        dummyPassengerQueue = new PassengerQueue(passengerList);

        ArrayList<Flight> flightList = new ArrayList<Flight>();
        flightList.add(dummyFlight1);
        flightList.add(dummyFlight2);

        dummyAirport = new Airport(passengerList, flightList);

        dummyDesk = new Desk(dummyAirport, dummyPassengerQueue, 3);

    }

    /**
     * Checks that the getter of the desk number performs correctly.
     */
    @Test
    public void testGetDeskNumber() {
        assertEquals(3, dummyDesk.getDeskNumber());
    }

    /**
     * Checks that the random baggage corresponds to the given dimensions
     * @throws NegativeDimensionException
     * @throws NullDimensionException
     */
    @Test
    public void testSetRandomBaggageToPassenger() throws NegativeDimensionException, NullDimensionException {
        Passenger noBaggagePassenger = new Passenger("John", "Doe", "EH145", "AB1CD2", true, "Economic");
        dummyDesk.setRandomBaggageToPassenger(noBaggagePassenger);
        int bHeight = noBaggagePassenger.getBaggage().getHeight();
        int bWidth  = noBaggagePassenger.getBaggage().getWidth();
        int bLength = noBaggagePassenger.getBaggage().getLength();
        int bWeight = noBaggagePassenger.getBaggage().getWeight();
        assertTrue((bHeight >= 20) & (bHeight <= 200));
        assertTrue((bWidth >= 20) & (bWidth <= 200));
        assertTrue((bLength >= 20) & (bLength <= 200));
        assertTrue((bWeight >= 20) & (bWeight <= 200));
    }

    /**
     * Checks that the run method runs through a single passenger.
     */
    @Test
    public void testRun() {
        dummyPassengerQueue.setDesksFull(false);
        dummyDesk.run();
    }

    /**
     * Checks the different check-in options.
     */
    @Test
    public void testCheckIn() {
        dummyDesk.checkIn(dummyPassenger1);
        dummyDesk.checkIn(dummyPassenger2);
        dummyDesk.checkIn(dummyPassenger3);
        dummyDesk.checkIn(dummyPassenger4);
        dummyDesk.checkIn(dummyPassenger5);
        dummyDesk.checkIn(dummyPassenger6);
    }
}