package test;

import junit.framework.TestCase;
import main.Baggage;
import main.Flight;
import main.Passenger;
import org.junit.*;

public class FlightTest extends TestCase {

    private Flight dummyFlight;
    private Passenger dummyPassenger1;
    private Passenger dummyPassenger2;
    private Baggage dummyBaggage1;
    private Baggage dummyBaggage2;

    @Before
    public void setUp() {
        dummyFlight = new Flight("Edinburgh", "RyanAir", 2, 100, 100, "EH145");
        dummyBaggage1 = new Baggage(10, 20, 30, 9);
        dummyBaggage2 = new Baggage(20, 10, 30, 11);
        dummyPassenger1 = new Passenger( "John Doe", "EH145", "1225", true);
        dummyPassenger2 = new Passenger( "Jane Doe", "EH146", "1244", true);
        dummyPassenger1.setBaggage(dummyBaggage1);
        dummyPassenger2.setBaggage(dummyBaggage2);
        dummyFlight.addPassenger(dummyPassenger1);
        dummyFlight.addPassenger(dummyPassenger2);
    }

    @Test
    public void testTotalWeight() {
        assertEquals(dummyFlight.totalWeight(),20);
    }

    @Test
    public void testTotalVolume() {
        assertEquals(dummyFlight.totalVolume(),12);
    }

    @Test
    public void testTotalPassengers() {
        assertEquals(dummyFlight.totalPassengers(),2);
    }

    @Test
    public void testCheckWeightTrue() {
        assertTrue(dummyFlight.checkWeight());
    }

    @Test
    public void testCheckWeightFalse() {
        Baggage dummyBaggage3 = new Baggage(1000, 1000, 1000, 300);
        Passenger dummyPassenger3 = new Passenger( "Bill Murray", "EH146", "1233", true);
        dummyPassenger3.setBaggage(dummyBaggage3);
        dummyFlight.addPassenger(dummyPassenger3);
        assertFalse(dummyFlight.checkWeight());
    }

    @Test
    public void testCheckVolumeTrue() {
        assertTrue(dummyFlight.checkVolume());
    }

    @Test
    public void testCheckVolumeFalse() {
        Baggage dummyBaggage3 = new Baggage(1000, 1000, 1000, 300);
        Passenger dummyPassenger3 = new Passenger( "Bill Murray", "EH146", "1233", true);
        dummyPassenger3.setBaggage(dummyBaggage3);
        dummyFlight.addPassenger(dummyPassenger3);
        assertFalse(dummyFlight.checkVolume());
    }

    @Test
    public void testCheckPassengersTrue() {
        assertTrue(dummyFlight.checkPassengers());
    }

    @Test
    public void testCheckPassengerseFalse() {
        Baggage dummyBaggage3 = new Baggage(1000, 1000, 1000, 300);
        Passenger dummyPassenger3 = new Passenger( "Bill Murray", "EH146", "1233", true);
        dummyPassenger3.setBaggage(dummyBaggage3);
        dummyFlight.addPassenger(dummyPassenger3);
        assertFalse(dummyFlight.checkPassengers());
    }

}