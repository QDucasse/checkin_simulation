package test;

import junit.framework.TestCase;
import main.Airport;
import main.Baggage;
import main.Flight;
import main.Passenger;
import main.exceptions.FlightNotFoundException;
import org.junit.*;
import org.junit.rules.ExpectedException;

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

    @Before
    public void setUp() {
        dummyBaggage1 = new Baggage(10, 20, 30, 9);
        dummyPassenger1 = new Passenger("John Doe", "EH145", "1225", true);
        dummyPassenger1.setBaggage(dummyBaggage1);

        dummyBaggage2 = new Baggage(40, 40, 40, 30);
        dummyPassenger2 = new Passenger( "Jane Doe", "FR145", "1226", false);
        dummyPassenger2.setBaggage(dummyBaggage2);

        dummyBaggage3 = new Baggage(10, 20, 30, 80);
        dummyPassenger3 = new Passenger( "Bill Murray", "FR145", "1227", false);
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

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testGetFlightRefFound() throws FlightNotFoundException {
        assertEquals(dummyFlight2,dummyAirport.getFlightFromRef("FR145"));
    }

    @Test
    public void testGetFlightRefNotFound() throws FlightNotFoundException {
        try{
            dummyAirport.getFlightFromRef("AA000");
        } catch(FlightNotFoundException e){
            assertEquals("Flight reference not found",e.getMessage());
        }
    }

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
