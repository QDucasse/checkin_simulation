package test;

import main.*;
import main.exceptions.NegativeDimensionException;
import main.exceptions.NullDimensionException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class SerializerTest {

    private static final String FILE_NAME_PASSENGERS = "test_passengers.json";
    private static final String FILE_NAME_FLIGHTS = "test_flights.json";
    private Airport dummyAirport;
    private Baggage dummyBaggage1;
    private Baggage dummyBaggage2;
    private Baggage dummyBaggage3;
    private Passenger dummyPassenger1;
    private Passenger dummyPassenger2;
    private Passenger dummyPassenger3;
    private Flight dummyFlight1;
    private Flight dummyFlight2;
    private static final String expectedPassengers = "[\n" +
            "  {\n" +
            "    \"firstName\": \"John\",\n" +
            "    \"lastName\": \"Doe\",\n" +
            "    \"flightReference\": \"EH145\",\n" +
            "    \"bookingReference\": \"AB1CD2\",\n" +
            "    \"baggage\": {\n" +
            "      \"length\": 10,\n" +
            "      \"height\": 20,\n" +
            "      \"width\": 30,\n" +
            "      \"weight\": 9\n" +
            "    },\n" +
            "    \"checkedIn\": true\n" +
            "  },\n" +
            "  {\n" +
            "    \"firstName\": \"Jane\",\n" +
            "    \"lastName\": \"Doe\",\n" +
            "    \"flightReference\": \"FR145\",\n" +
            "    \"bookingReference\": \"AA0BB0\",\n" +
            "    \"baggage\": {\n" +
            "      \"length\": 40,\n" +
            "      \"height\": 40,\n" +
            "      \"width\": 40,\n" +
            "      \"weight\": 30\n" +
            "    },\n" +
            "    \"checkedIn\": false\n" +
            "  },\n" +
            "  {\n" +
            "    \"firstName\": \"Bill\",\n" +
            "    \"lastName\": \"Murray\",\n" +
            "    \"flightReference\": \"FR145\",\n" +
            "    \"bookingReference\": \"12345\",\n" +
            "    \"baggage\": {\n" +
            "      \"length\": 10,\n" +
            "      \"height\": 20,\n" +
            "      \"width\": 30,\n" +
            "      \"weight\": 80\n" +
            "    },\n" +
            "    \"checkedIn\": false\n" +
            "  }\n" +
            "]";
    private static final String expectedFlights = "[\n" +
            "  {\n" +
            "    \"destination\": \"Edinburgh\",\n" +
            "    \"carrier\": \"RyanAir\",\n" +
            "    \"maxPassengers\": 2,\n" +
            "    \"maxWeight\": 100,\n" +
            "    \"maxVolume\": 100,\n" +
            "    \"flightRef\": \"EH145\",\n" +
            "    \"baggageMaxWeight\": 30,\n" +
            "    \"baggageMaxVolume\": 23,\n" +
            "    \"excessFee\": 30,\n" +
            "    \"passengerList\": [\n" +
            "      {\n" +
            "        \"firstName\": \"John\",\n" +
            "        \"lastName\": \"Doe\",\n" +
            "        \"flightReference\": \"EH145\",\n" +
            "        \"bookingReference\": \"AB1CD2\",\n" +
            "        \"baggage\": {\n" +
            "          \"length\": 10,\n" +
            "          \"height\": 20,\n" +
            "          \"width\": 30,\n" +
            "          \"weight\": 9\n" +
            "        },\n" +
            "        \"checkedIn\": true\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"destination\": \"Paris\",\n" +
            "    \"carrier\": \"RyanAir\",\n" +
            "    \"maxPassengers\": 2,\n" +
            "    \"maxWeight\": 100,\n" +
            "    \"maxVolume\": 100,\n" +
            "    \"flightRef\": \"FR145\",\n" +
            "    \"baggageMaxWeight\": 30,\n" +
            "    \"baggageMaxVolume\": 23,\n" +
            "    \"excessFee\": 30,\n" +
            "    \"passengerList\": [\n" +
            "      {\n" +
            "        \"firstName\": \"Jane\",\n" +
            "        \"lastName\": \"Doe\",\n" +
            "        \"flightReference\": \"FR145\",\n" +
            "        \"bookingReference\": \"AA0BB0\",\n" +
            "        \"baggage\": {\n" +
            "          \"length\": 40,\n" +
            "          \"height\": 40,\n" +
            "          \"width\": 40,\n" +
            "          \"weight\": 30\n" +
            "        },\n" +
            "        \"checkedIn\": false\n" +
            "      },\n" +
            "      {\n" +
            "        \"firstName\": \"Bill\",\n" +
            "        \"lastName\": \"Murray\",\n" +
            "        \"flightReference\": \"FR145\",\n" +
            "        \"bookingReference\": \"12345\",\n" +
            "        \"baggage\": {\n" +
            "          \"length\": 10,\n" +
            "          \"height\": 20,\n" +
            "          \"width\": 30,\n" +
            "          \"weight\": 80\n" +
            "        },\n" +
            "        \"checkedIn\": false\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]";

    public void writeFile(String fileName, String text) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        writer.write(text);
        writer.close();
    }

    /**
     * The first setup to be run before the test suite is to look for the file that will hold the different serializations
     * and delete it.
     */
    @BeforeClass
    public static void setupBeforeClass() {
        File flights = new File(FILE_NAME_FLIGHTS);
        if (flights.exists())
            flights.delete();
        File passengers = new File(FILE_NAME_PASSENGERS);
        if (passengers.exists())
            passengers.delete();
    }

    /**
     * The setup of this test class corresponds to three passengers and two flights contained in an airport.
     */
    @Before
    public void setUp() throws NullDimensionException, NegativeDimensionException {
        dummyBaggage1 = new Baggage(10, 20, 30, 9);
        dummyPassenger1 = new Passenger("John", "Doe", "EH145", "AB1CD2", true);
        dummyPassenger1.setBaggage(dummyBaggage1);

        dummyBaggage2 = new Baggage(40, 40, 40, 30);
        dummyPassenger2 = new Passenger( "Jane", "Doe", "FR145", "AA0BB0", false);
        dummyPassenger2.setBaggage(dummyBaggage2);

        dummyBaggage3 = new Baggage(10, 20, 30, 80);
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
        dummyAirport.addPassenger(dummyPassenger3);
        dummyAirport.addFlight(dummyFlight1);
        dummyAirport.addFlight(dummyFlight2);
    }

    /**
     * After any test has been run, the file is deleted.
     */
    @After
    public void tearDown() {
        File flights = new File(FILE_NAME_FLIGHTS);
        if (flights.exists())
            flights.delete();
        File passengers = new File(FILE_NAME_PASSENGERS);
        if (passengers.exists())
            passengers.delete();
    }

    /* ========================
       SERIALIZATION METHODS
    ======================== */

    /**
     * Serializes the whole airport and checks if the created passengers and flights files are correct.
     */
    @Test
    public void testAirportToFile() throws IOException {
        // Serialization of the airport
        Serializer.airportToFile(dummyAirport, FILE_NAME_FLIGHTS, FILE_NAME_PASSENGERS);
        // Reading the passengers file content
        String passengersFileContent = Files.readString(Paths.get(FILE_NAME_PASSENGERS));
        // Reading the passengers file content
        String flightsFileContent = Files.readString(Paths.get(FILE_NAME_FLIGHTS));
        assertEquals(expectedPassengers, passengersFileContent);
        assertEquals(expectedFlights, flightsFileContent);
    }

    /**
     * Serializes the flights and checks if the flight file is correct.
     */
    @Test
    public void testFlightsToFile() throws IOException {
        // Serialization of the flights list
        ArrayList<Flight> flightList = dummyAirport.getFlightList();
        Serializer.flightsToFile(flightList, FILE_NAME_FLIGHTS);
        // Reading the file content
        String fileContent = Files.readString(Paths.get(FILE_NAME_FLIGHTS));
        assertEquals(expectedFlights, fileContent);
    }

    /**
     * Serializes the passengers and checks if the passenger file is correct.
     */
    @Test
    public void testPassengersToFile() throws IOException {
        // Serialization of the passengers list
        ArrayList<Passenger> passengerList = dummyAirport.getPassengerList();
        Serializer.passengersToFile(passengerList, FILE_NAME_PASSENGERS);
        // Reading the file content
        String fileContent = Files.readString(Paths.get(FILE_NAME_PASSENGERS));
        assertEquals(expectedPassengers, fileContent);
    }

    /* ==========================
       DESERIALIZATION METHODS
    ========================== */

    /**
     *
     */
    @Test
    public void testFileToAirport() throws IOException {
        // Write the dummy json files
        writeFile(FILE_NAME_FLIGHTS, expectedFlights);
        writeFile(FILE_NAME_PASSENGERS, expectedPassengers);
        // Deserialize the airport
        Airport airport = Serializer.fileToAirport(FILE_NAME_PASSENGERS,FILE_NAME_FLIGHTS);
        assertEquals(airport, dummyAirport);
    }

    /**
     *
     */
    @Test
    public void testfileToPassengerList() throws IOException {
        // Write the dummy json file
        writeFile(FILE_NAME_PASSENGERS, expectedPassengers);
        // Deserialize the passengers
        ArrayList<Passenger> passengerList = Serializer.fileToPassengerList(FILE_NAME_PASSENGERS);
        assertEquals(passengerList, dummyAirport.getPassengerList());
    }

    /**
     *
     */
    @Test
    public void testfileToFlightsList() throws IOException {
        // Write the dummy json file
        writeFile(FILE_NAME_FLIGHTS, expectedFlights);
        // Deserialize the passengers
        ArrayList<Flight> flightList = Serializer.fileToFlightList(FILE_NAME_FLIGHTS);
        assertEquals(flightList, dummyAirport.getFlightList());
    }
}
