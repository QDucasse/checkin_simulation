package test;

import junit.framework.TestCase;
import main.Baggage;
import main.Flight;
import main.Passenger;
import main.exceptions.NegativeDimensionException;
import main.exceptions.NullDimensionException;
import org.junit.Before;
import org.junit.Test;

public class FlightTest extends TestCase {

    private Flight dummyFlight;
    private Passenger dummyPassenger1;
    private Passenger dummyPassenger2;
    private Baggage dummyBaggage1;
    private Baggage dummyBaggage2;

    /**
     * Sets up an environment before every test. Here, one flight is created with two passengers with one baggage each.
     */
    @Before
    public void setUp() throws NullDimensionException {
        dummyFlight = new Flight("Edinburgh", "RyanAir", 2, 100, 100, "EH145");
        try {
            dummyBaggage1 = new Baggage(10, 20, 30, 9);
        } catch (NegativeDimensionException e) {
            e.printStackTrace();
        }
        try {
            dummyBaggage2 = new Baggage(20, 10, 30, 11);
        } catch (NegativeDimensionException e) {
            e.printStackTrace();
        }
        dummyPassenger1 = new Passenger( "John", "Doe", "EH145", "1225", true);
        dummyPassenger2 = new Passenger( "Jane", "Doe", "EH146", "1244", true);
        dummyPassenger1.setBaggage(dummyBaggage1);
        dummyPassenger2.setBaggage(dummyBaggage2);
        dummyFlight.addPassenger(dummyPassenger1);
        dummyFlight.addPassenger(dummyPassenger2);
    }

    /**
     * Checks the total weight of the baggages.
     */
    @Test
    public void testTotalWeight() {
        assertEquals(dummyFlight.totalWeight(),20);
    }

    /**
     * Checks the total volume of the baggages.
     */
    @Test
    public void testTotalVolume() {
        assertEquals(dummyFlight.totalVolume(),12);
    }

    /**
     * Checks the total number of passengers
     */
    @Test
    public void testTotalPassengers() {
        assertEquals(dummyFlight.totalPassengers(),2);
    }

    /**
     * Checks that the total weight of the baggages does not exceed the maximum (max 100 here).
     */
    @Test
    public void testCheckWeightTrue() {
        assertTrue(dummyFlight.checkWeight());
    }

    /**
     * Adds a new passenger with a super heavy baggage and checks that the total weight exceeds the maximum allowed.
     */
    @Test
    public void testCheckWeightFalse() throws NullDimensionException, NegativeDimensionException {
        Baggage dummyBaggage3 = new Baggage(1000, 1000, 1000, 300);
        Passenger dummyPassenger3 = new Passenger( "Bill", "Murray", "EH146", "1233", true);
        dummyPassenger3.setBaggage(dummyBaggage3);
        dummyFlight.addPassenger(dummyPassenger3);
        assertFalse(dummyFlight.checkWeight());
    }

    /**
     * Checks that the total volume of the baggages does not exceed the maximum (max 100 here).
     */
    @Test
    public void testCheckVolumeTrue() {
        assertTrue(dummyFlight.checkVolume());
    }

    /**
     * Adds a new passenger with a super wide baggage and checks that the total volume exceeds the maximum allowed.
     */
    @Test
    public void testCheckVolumeFalse() throws NullDimensionException, NegativeDimensionException {
        Baggage dummyBaggage3 = new Baggage(1000, 1000, 1000, 300);
        Passenger dummyPassenger3 = new Passenger( "Bill" ,"Murray", "EH146", "1233", true);
        dummyPassenger3.setBaggage(dummyBaggage3);
        dummyFlight.addPassenger(dummyPassenger3);
        assertFalse(dummyFlight.checkVolume());
    }

    /**
     * Checks that the total number of passengers does not exceed the maximum (max 2 here).
     */
    @Test
    public void testCheckPassengersTrue() {
        assertTrue(dummyFlight.checkPassengers());
    }

    /**
     * Adds a new passenger and checks that the maximum number of passengers is exceeded.
     */
    @Test
    public void testCheckPassengerseFalse() throws NullDimensionException, NegativeDimensionException {
        Baggage dummyBaggage3 = new Baggage(10, 20, 30, 9);
        Passenger dummyPassenger3 = new Passenger( "Bill" ,"Murray", "EH146", "1233", true);
        dummyPassenger3.setBaggage(dummyBaggage3);
        dummyFlight.addPassenger(dummyPassenger3);
        assertFalse(dummyFlight.checkPassengers());
    }

}