package test;

import main.Airport;
import main.Baggage;
import main.Flight;
import main.Passenger;
import main.exceptions.NullDimensionException;
import org.junit.*;

import java.io.File;

public class SerializerTest {

    private static final String FILE_NAME = "test.json";
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
    public void setUp() throws NullDimensionException {
        dummyBaggage1 = new Baggage(10, 20, 30, 9);
        dummyPassenger1 = new Passenger("John Doe", "EH145", "AB1CD2", true);
        dummyPassenger1.setBaggage(dummyBaggage1);

        dummyBaggage2 = new Baggage(40, 40, 40, 30);
        dummyPassenger2 = new Passenger( "Jane Doe", "FR145", "AA0BB0", false);
        dummyPassenger2.setBaggage(dummyBaggage2);

        dummyBaggage3 = new Baggage(10, 20, 30, 80);
        dummyPassenger3 = new Passenger( "Bill Murray", "FR145", "12345", false);
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
}
