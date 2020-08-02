package test;

import junit.framework.TestCase;
import main.*;
import main.exceptions.EmptyPassengerListException;
import main.exceptions.NegativeDimensionException;
import main.exceptions.NullDimensionException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AirportLoggerTest extends TestCase {

    private static final String LOG_FILE = "logs.txt";
    private Airport dummyAirport;
    private Flight dummyFlight1;
    private Flight dummyFlight2;
    private Passenger dummyPassenger1;
    private Passenger dummyPassenger2;
    private Passenger dummyPassenger3;
    private Passenger dummyPassenger4;
    private Passenger dummyPassenger5;
    private Baggage dummyBaggage1;
    private Baggage dummyBaggage2;
    private Baggage dummyBaggage3;
    private PassengerQueue dummyPassengerQueue;
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
    public void setUp() throws NullDimensionException, EmptyPassengerListException {
        // Case 1: Already checked in
        try {
            dummyBaggage1 = new Baggage(10, 20, 30, 9);
        } catch (NegativeDimensionException e) {
            e.printStackTrace();
        }
        dummyPassenger1 = new Passenger("John", "Doe", "EH145", "1225", true);
        dummyPassenger1.setBaggage(dummyBaggage1);
        // Case 2: Wrong flight reference
        dummyPassenger2 = new Passenger( "Jane", "Doe", "AA000", "1226", false);
        dummyPassenger2.setBaggage(dummyBaggage1);
        // Case 3: Warning weight
        try {
            dummyBaggage2 = new Baggage(10, 20, 30, 100);
        } catch (NegativeDimensionException e) {
            e.printStackTrace();
        }
        dummyPassenger3 = new Passenger( "Bill", "Murray", "EH145", "1227", false);
        dummyPassenger3.setBaggage(dummyBaggage2);
        // Case 4: Warning volume
        try {
            dummyBaggage3 = new Baggage(100, 100, 100, 9);
        } catch (NegativeDimensionException e) {
            e.printStackTrace();
        }
        dummyPassenger4 = new Passenger( "George", "Clooney", "EH145", "1228", false);
        dummyPassenger4.setBaggage(dummyBaggage3);
        // Case 5: Working fine

        dummyPassenger5 = new Passenger( "Brad", "Pitt", "EH145", "1229", false);
        dummyPassenger5.setBaggage(dummyBaggage1);

        dummyFlight1 = new Flight("Edinburgh", "RyanAir", 2, 100, 100, "EH145");
        dummyFlight2 = new Flight("Paris", "RyanAir", 2, 100, 100, "FR145");

        ArrayList<Passenger> passengerList = new ArrayList<Passenger>();
        passengerList.add(dummyPassenger1);
        passengerList.add(dummyPassenger2);
        passengerList.add(dummyPassenger3);
        passengerList.add(dummyPassenger4);
        passengerList.add(dummyPassenger5);

        dummyPassengerQueue = new PassengerQueue(passengerList);

        ArrayList<Flight> flightList = new ArrayList<Flight>();
        flightList.add(dummyFlight1);
        flightList.add(dummyFlight2);

        dummyAirport = new Airport(passengerList, flightList);

        dummyDesk = new Desk(dummyAirport, dummyPassengerQueue, 3);

    }

    /**
     * After any test has been run, the file is deleted.
     */
    @After
    public void tearDown() {
        File logs = new File(LOG_FILE);
        if (logs.exists())
            logs.delete();
    }

    /**
     * Compares a list of logs with the corresponding log file. In order to do so, it removes the date and time stamps
     * in order to only compare the messages.
     * @param toCompare
     *      String list to compare
     * @return identical
     *      True if the elements are the same, false otherwise.
     * @throws IOException
     *      If the log file is not found.
     */
    public boolean compareWithLog(String[] toCompare) throws IOException {
        // Load lines
        List<String> lines = Files.readAllLines(Paths.get(LOG_FILE));
        // Remove 1 line out of 2 (date, hour, ...)
        for (int i = 0; i < lines.size(); i+= 2) {
            lines.remove(i);
        }

        boolean identical = true;
        for (int i = 0; i < lines.size(); i++) {
            identical = identical & (lines.get(i).equals(Arrays.asList(toCompare).get(i)));
        }
        return identical;
    }

    /**
     * Checks that the basic log method is working.
     * @throws IOException
     *      If the log file is not found.
     */
    @Test
    public void testLog() throws IOException {
        AirportLogger.log("testLog");
        String[] toCompare = new String[]{"INFO: testLog"};
        assertTrue(compareWithLog(toCompare));
    }

    /**
     * Checks that the basic log method is working.
     * @throws IOException
     *      If the log file is not found.
     */
    @Test
    public void testLogDeskPassengerAccepted() throws IOException {
        AirportLogger.logDeskPassengerAccepted(dummyDesk, dummyPassenger1);
        String[] toCompare = new String[]{"INFO: Desk nº3 accepted to check-in: John Doe"};
        assertTrue(compareWithLog(toCompare));
    }

    @Test
    public void testlogDeskTimeTaken() throws IOException {
        AirportLogger.logDeskTimeTaken(dummyDesk, 5000);
        String[] toCompare = new String[]{"INFO: Desk nº3: Check-in completed in 5s"};
        assertTrue(compareWithLog(toCompare));
    }

    @Test
    public void testLogFirstPassengerInQueue() throws IOException {
        AirportLogger.logFirstPassengerInQueue(dummyPassenger1);
        String[] toCompare = new String[]{"INFO: John Doe with reservation 1225 is waiting to check-in for flight EH145"};
        assertTrue(compareWithLog(toCompare));
    }

    @Test
    public void testLogCheckInComplete() throws IOException {
        AirportLogger.logCheckInComplete(dummyDesk, dummyPassenger5);
        String[] toCompare = new String[]{"INFO: Desk nº3: Brad Pitt is now checked-in with their baggage!"};
        assertTrue(compareWithLog(toCompare));
    }

    @Test
    public void testLogCheckInErrorFlight() throws IOException {
        AirportLogger.logCheckInErrorFlight(dummyDesk, dummyPassenger2);
        String[] toCompare = new String[]{"INFO: Desk nº3: Jane Doe has an erroneous flight reference."};
        assertTrue(compareWithLog(toCompare));
    }

    @Test
    public void testLogCheckInAlreadyDone() throws IOException {
        AirportLogger.logCheckInAlreadyDone(dummyDesk, dummyPassenger1);
        String[] toCompare = new String[]{"INFO: Desk nº3: John Doe is already checked-in."};
        assertTrue(compareWithLog(toCompare));
    }

    @Test
    public void testLogCheckInBaggageVolume() throws IOException {
        AirportLogger.logCheckInBaggageVolume(dummyDesk, dummyPassenger4, "30");
        String[] toCompare = new String[]{"INFO: Desk nº3: George Clooney has a baggage too wide, the passenger has to pay: 30€."};
        assertTrue(compareWithLog(toCompare));
    }

    @Test
    public void testLogCheckInBaggageWeight() throws IOException {
        AirportLogger.logCheckInBaggageWeight(dummyDesk, dummyPassenger3, "30");
        String[] toCompare = new String[]{"INFO: Desk nº3: Bill Murray has a baggage too heavy, the passenger has to pay: 30€."};
        assertTrue(compareWithLog(toCompare));
    }

    @Test
    public void testLogCheckInFlightFull() throws IOException {
        AirportLogger.logCheckInFlightFull(dummyDesk, dummyPassenger5);
        String[] toCompare = new String[]{"INFO: Desk nº3: Brad Pitt could not check-in, this flight is full."};
        assertTrue(compareWithLog(toCompare));
    }

}