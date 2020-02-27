package test;

import junit.framework.TestCase;
import main.Airport;
import main.Baggage;
import main.Flight;
import main.Passenger;
import org.junit.*;

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
    private Flight dummyFlight1;
    private Flight dummyFlight2;

    /**
     *
     */
    @Before
    public void setUp() {
        // Case 1: Already checked in
        dummyBaggage1 = new Baggage(10, 20, 30, 9);
        dummyPassenger1 = new Passenger("John Doe", "EH145", "1225", true);
        dummyPassenger1.setBaggage(dummyBaggage1);
        // Case 2: Wrong flight reference
        dummyPassenger2 = new Passenger( "Jane Doe", "AA000", "1226", false);
        dummyPassenger2.setBaggage(dummyBaggage1);
        // Case 3: Warning weight
        dummyBaggage2 = new Baggage(10, 20, 30, 100);
        dummyPassenger3 = new Passenger( "Bill Murray", "EH145", "1227", false);
        dummyPassenger3.setBaggage(dummyBaggage2);
        // Case 4: Warning volume
        dummyBaggage3 = new Baggage(100, 100, 100, 9);
        dummyPassenger4 = new Passenger( "George Clooney", "EH145", "1228", false);
        dummyPassenger4.setBaggage(dummyBaggage3);
        // Case 5: Working fine

        dummyPassenger5 = new Passenger( "Brad Pitt", "EH145", "1229", false);
        dummyPassenger5.setBaggage(dummyBaggage1);

        dummyFlight1 = new Flight("Edinburgh", "RyanAir", 2, 100, 100, "EH145");
        dummyFlight2 = new Flight("Paris", "RyanAir", 2, 100, 100, "FR145");

        dummyAirport = new Airport();
        dummyAirport.addPassenger(dummyPassenger1);
        dummyAirport.addPassenger(dummyPassenger2);
        dummyAirport.addFlight(dummyFlight1);
        dummyAirport.addFlight(dummyFlight2);
    }


    /**
     * 
     */
    @Test
    public void testLastName() {
        assertEquals("Doe",dummyPassenger1.getLastName());
    }

    /**
     * 
     */
    @Test
    public void testCheckInAlreadyDone() {
        assertEquals(Passenger.CheckinResult.WARNING_ALREADY_DONE,dummyPassenger1.checkIn(dummyAirport));
    }

    /**
     * 
     */
    @Test
    public void testCheckInFlightReferenceError() {
        assertEquals(Passenger.CheckinResult.ERR_FLIGHT_REFERENCE,dummyPassenger2.checkIn(dummyAirport));
    }

    /**
     * 
     */
    @Test
    public void testCheckInBaggageWeightWarning() {
        assertEquals(Passenger.CheckinResult.WARNING_BAGGAGE_WEIGHT,dummyPassenger3.checkIn(dummyAirport));
    }

    /**
     * 
     */
    @Test
    public void testCheckInBaggageVolumeWarning() {
        assertEquals(Passenger.CheckinResult.WARNING_BAGGAGE_VOLUME,dummyPassenger4.checkIn(dummyAirport));
    }

    /**
     * 
     */
    @Test
    public void testCheckInDone() {
        assertEquals(Passenger.CheckinResult.DONE,dummyPassenger5.checkIn(dummyAirport));
    }
}
