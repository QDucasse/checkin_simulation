package test;

import junit.framework.TestCase;
import main.Airport;
import main.Baggage;
import main.Flight;
import main.Passenger;
import main.exceptions.BookingRefAndNameNoMatchException;
import main.exceptions.FlightNotFoundException;
import main.exceptions.NegativeDimensionException;
import main.exceptions.NullDimensionException;
import org.junit.Before;
import org.junit.Test;

public class AirportTest extends TestCase {
    private Airport dummyAirport;
    private Baggage dummyBaggage1;
    private Baggage dummyBaggage2;
    private Baggage dummyBaggage3;
    private Passenger dummyPassenger1;
    private Passenger dummyPassenger2;
    private Passenger dummyPassenger3;
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
    public void setUp() throws NullDimensionException {
        try {
            dummyBaggage1 = new Baggage(10, 20, 30, 9);
        } catch (NegativeDimensionException e) {
            e.printStackTrace();
        }
        dummyPassenger1 = new Passenger("John", "Doe", "EH145", "AB1CD2", true);
        dummyPassenger1.setBaggage(dummyBaggage1);

        try {
            dummyBaggage2 = new Baggage(40, 40, 40, 30);
        } catch (NegativeDimensionException e) {
            e.printStackTrace();
        }
        dummyPassenger2 = new Passenger( "Jane", "Doe", "FR145", "AA0BB0", false);
        dummyPassenger2.setBaggage(dummyBaggage2);

        try {
            dummyBaggage3 = new Baggage(10, 20, 30, 80);
        } catch (NegativeDimensionException e) {
            e.printStackTrace();
        }
        dummyPassenger3 = new Passenger( "Bill", "Murray", "FR145", "12345", false);
        dummyPassenger3.setBaggage(dummyBaggage3);

        dummyFlight1 = new Flight("Edinburgh", "RyanAir", 2, 100, 100, "EH145");
        dummyFlight2 = new Flight("Paris", "RyanAir", 2, 100, 100, "FR145");

        dummyFlight1.addPassenger(dummyPassenger1);
        dummyFlight2.addPassenger(dummyPassenger2);
        dummyFlight2.addPassenger(dummyPassenger3);

        dummyAirport = new Airport();
        dummyAirport.addPassenger(dummyPassenger1);
        dummyAirport.addPassenger(dummyPassenger2);
        dummyAirport.addFlight(dummyFlight1);
        dummyAirport.addFlight(dummyFlight2);
    }

    /**
     * Checks that a flight in the airport can be found using its reference.
     * @throws FlightNotFoundException
     */
    @Test
    public void testGetFlightRefFound() throws FlightNotFoundException {
        assertEquals(dummyFlight2,dummyAirport.getFlightFromRef("FR145"));
    }

    /**
     * Checks that a flight unknown from the airport cannot be found using its reference.
     * @throws FlightNotFoundException
     */
    @Test
    public void testGetFlightRefNotFound() throws FlightNotFoundException {
        try{
            dummyAirport.getFlightFromRef("AA000");
        } catch(FlightNotFoundException e){
            assertEquals("Flight reference not found",e.getMessage());
        }
    }

    /**
     * Checks that a passenger in the airport can be found using its reference.
     * @throws BookingRefAndNameNoMatchException
     */
    @Test
    public void testGetPassengerMatch() throws BookingRefAndNameNoMatchException {
        assertEquals(dummyPassenger1,dummyAirport.getPassengerFromBookingRefAndName("AB1CD2","Doe"));
    }

    /**
     * Checks that a flight unknown from the airport can be found using its reference.
     * @throws BookingRefAndNameNoMatchException
     */
    @Test
    public void testGetPassengerNoMatch() throws BookingRefAndNameNoMatchException {
        try{
            dummyAirport.getPassengerFromBookingRefAndName("AA0AA0","Doe");
        } catch(BookingRefAndNameNoMatchException e) {
            assertEquals("Booking reference and name do not match",e.getMessage());
        }
    }

    /**
     *
     */
    @Test
    public void testReport() {
        String expected = "========================\n" +
                "Report for flight EH145: \n" +
                "Number of passengers: 1/2\n" +
                "Weight: 9/100\n" +
                "Volume: 6/100\n" +
                "\n" +
                "========================\n" +
                "========================\n" +
                "Report for flight FR145: \n" +
                "Number of passengers: 2/2\n" +
                "Weight: 110/100\n" +
                "Volume: 70/100\n" +
                "\n" +
                "The weight is exceeded!\n" +
                "========================\n";
        assertEquals(expected,dummyAirport.outputReport());
    }
}
