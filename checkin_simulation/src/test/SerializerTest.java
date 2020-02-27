package test;

import main.Airport;
import main.Baggage;
import main.Flight;
import main.Passenger;
import org.junit.*;

import java.io.File;

public class SerializerTest {

    private static final String FILE_NAME = "test.json";


    /**
     * 
     */
    @BeforeClass
    public static void setupBeforeClass() {
        File f = new File(FILE_NAME);
        if (f.exists())
            f.delete();
    }

    /**
     * 
     */
    @AfterClass
    public void tearDownAfterClass() {

    }

    /**
     * 
     */
    @Before
    public void setUp() {
    }

    /**
     * 
     */
    @After
    public void tearDown() {
        File f = new File(FILE_NAME);
        if (f.exists())
            f.delete();
    }

    /**
     * 
     */
    @Test
    void testAirportToFile() {

    }

    /**
     * 
     */
    @Test
    void testFileToAirport() {


    }

    /**
     * @return
     */
    private Airport createAirport() {
        Airport airport = new Airport();
        Flight f1 = new Flight("destinatio", "carrier", 8, 100, 100, "EH145");
        Passenger passenger1 = new Passenger("James", "EH145", "1225", true);
        Passenger passenger2 = new Passenger("Lucas", "EH146", "1244", false);
        Baggage baggage1 = new Baggage(10, 20, 30, 9);
        Baggage baggage2 = new Baggage(20, 10, 30, 11);
        passenger1.setBaggage(baggage1);
        passenger2.setBaggage(baggage2);
        f1.addPassenger(passenger1);
        f1.addPassenger(passenger2);
        airport.addFlight(f1);
        return airport;
    }
}
