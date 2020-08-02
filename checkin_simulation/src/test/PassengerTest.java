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

public class PassengerTest extends TestCase {
    private Airport dummyAirport;
    private Baggage dummyBaggage1;
    private Baggage dummyBaggage2;
    private Baggage dummyBaggage3;
    private Passenger dummyPassenger1;
    private Passenger dummyPassenger2;
    private Passenger dummyPassenger3;
    private Passenger dummyPassenger4;
    private Passenger dummyPassenger5;
    private Passenger dummyPassenger6;
    private Flight dummyFlight1;
    private Flight dummyFlight2;

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

        dummyFlight1 = new Flight("Edinburgh", "RyanAir", 1, 100, 100, "EH145");
        dummyFlight2 = new Flight("Paris", "RyanAir", 1, 100, 100, "FR145");

        ArrayList<Passenger> passengerList = new ArrayList<Passenger>();
        passengerList.add(dummyPassenger1);
        passengerList.add(dummyPassenger2);
        passengerList.add(dummyPassenger3);
        passengerList.add(dummyPassenger4);
        passengerList.add(dummyPassenger5);

        ArrayList<Flight> flightList = new ArrayList<Flight>();
        flightList.add(dummyFlight1);
        flightList.add(dummyFlight2);

        dummyAirport = new Airport(passengerList, flightList);
    }


    /**
     * Checks the last name of a passenger.
     */
    @Test
    public void testLastName() {
        assertEquals("Doe",dummyPassenger1.getLastName());
    }

    /**
     * Checks the check-in result of an already checked-in passenger.
     */
    @Test
    public void testCheckInAlreadyDone() {
        assertEquals(Passenger.CheckinResult.WARNING_ALREADY_DONE,dummyPassenger1.checkIn(dummyAirport));
    }

    /**
     * Checks the check-in result of a passenger with an erroneous flight reference.
     */
    @Test
    public void testCheckInFlightReferenceError() {
        assertEquals(Passenger.CheckinResult.ERR_FLIGHT_REFERENCE,dummyPassenger2.checkIn(dummyAirport));
    }

    /**
     * Checks the check-in result of a passenger with a heavy baggage.
     */
    @Test
    public void testCheckInBaggageWeightWarning() {
        assertEquals(Passenger.CheckinResult.WARNING_BAGGAGE_WEIGHT,dummyPassenger3.checkIn(dummyAirport));
    }

    /**
     * Checks the check-in result of a passenger with a wide baggage.
     */
    @Test
    public void testCheckInBaggageVolumeWarning() {
        assertEquals(Passenger.CheckinResult.WARNING_BAGGAGE_VOLUME,dummyPassenger4.checkIn(dummyAirport));
    }

    /**
     * Checks the check-in result of a passenger in a full flight.
     */
    @Test
    public void testCheckInFlightFull() {
        dummyPassenger5.checkIn(dummyAirport);
        assertEquals(Passenger.CheckinResult.ERR_FLIGHT_IS_FULL,dummyPassenger6.checkIn(dummyAirport));
    }

    /**
     * Checks the check-in result of a passenger with no issues.
     */
    @Test
    public void testCheckInDone() {
        assertEquals(Passenger.CheckinResult.DONE,dummyPassenger5.checkIn(dummyAirport));
    }


}
