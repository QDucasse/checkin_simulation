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

public class WaitingLineTest extends TestCase {

    private Airport dummyAirport;
    private Flight dummyFlight1;
    private Flight dummyFlight2;
    private Passenger dummyPassenger1;
    private Passenger dummyPassenger2;
    private Passenger dummyPassenger3;
    private Baggage dummyBaggage1;
    private Baggage dummyBaggage2;
    private Baggage dummyBaggage3;
    private PassengerQueue dummyPassengerQueue;
    private WaitingLine dummyWaitingLine;
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
        dummyWaitingLine = new WaitingLine(dummyPassengerQueue);

    }

    /**
     * Checks that the run method runs through a single passenger.
     */
    @Test
    public void testRun() {
        dummyPassengerQueue.deletePassengers();
        dummyWaitingLine.run();
    }
}
