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

public class PassengerQueueTest extends TestCase {

    private Flight dummyFlight1;
    private Flight dummyFlight2;
    private Passenger dummyPassenger1;
    private Passenger dummyPassenger2;
    private Passenger dummyPassenger3;
    private Baggage dummyBaggage1;
    private Baggage dummyBaggage2;
    private Baggage dummyBaggage3;
    private PassengerQueue dummyPassengerQueue;

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
        try {
            dummyBaggage1 = new Baggage(10, 20, 30, 9);
        } catch (NegativeDimensionException e) {
            e.printStackTrace();
        }
        dummyPassenger1 = new Passenger("John", "Doe", "EH145", "AB1CD2", true, "Economic");
        dummyPassenger1.setBaggage(dummyBaggage1);

        try {
            dummyBaggage2 = new Baggage(40, 40, 40, 30);
        } catch (NegativeDimensionException e) {
            e.printStackTrace();
        }
        dummyPassenger2 = new Passenger( "Jane", "Doe", "FR145", "AA0BB0", false, "Economic");
        dummyPassenger2.setBaggage(dummyBaggage2);

        try {
            dummyBaggage3 = new Baggage(10, 20, 30, 80);
        } catch (NegativeDimensionException e) {
            e.printStackTrace();
        }
        dummyPassenger3 = new Passenger( "Bill", "Murray", "FR145", "12345", false, "Economic");
        dummyPassenger3.setBaggage(dummyBaggage3);

        dummyFlight1 = new Flight("Edinburgh", "RyanAir", 2, 100, 100, "EH145");
        dummyFlight2 = new Flight("Paris", "RyanAir", 2, 100, 100, "FR145");

        dummyFlight1.addPassenger(dummyPassenger1);
        dummyFlight2.addPassenger(dummyPassenger2);
        dummyFlight2.addPassenger(dummyPassenger3);

        ArrayList<Passenger> passengerList = new ArrayList<Passenger>();
        passengerList.add(dummyPassenger1);
        passengerList.add(dummyPassenger2);
        passengerList.add(dummyPassenger3);

        dummyPassengerQueue = new PassengerQueue(passengerList);

    }

    /**
     * Checks initialization of the two different booleans.
     */
    @Test
    public void testInitializationComplete() {
        assertTrue(dummyPassengerQueue.areDesksFull());
        assertFalse(dummyPassengerQueue.getDone());
    }

    /**
     * Checks a failed initialization with an empty passengers list.
     */
    @Test
    public void testInitializationFailed() {
        try{
            ArrayList<Passenger> emptyPassengerList = new ArrayList<Passenger>();
            PassengerQueue passengerQueue = new PassengerQueue(emptyPassengerList);
        } catch (EmptyPassengerListException e) {
            assertEquals("Passenger list cannot be empty",e.getMessage());
        }
    }

    /**
     * Checks that the done getter performs correctly.
     */
    @Test
    public void testGetDone() {
        assertFalse(dummyPassengerQueue.getDone());
    }


    /**
     * Checks that the done setter (last method of the execution flow) correctly sets done to true.
     */
    @Test
    public void testSetDone() {
        dummyPassengerQueue.setDone();
        assertTrue(dummyPassengerQueue.getDone());
    }

    /**
     * Checks that when a desk is available (i.e. when all desks are not full), the first passenger in the queue is
     * accepted and the second is now on top of the queue.
     */
    @Test
    public void testAcceptNewPassenger() {
        dummyPassengerQueue.setDesksFull(false);
        assertEquals(dummyPassenger1, dummyPassengerQueue.acceptNewPassenger());
        assertEquals(dummyPassenger2, dummyPassengerQueue.peek());
    }

    /**
     * Checks that when all desks are full, the first passenger in the queue changes.
     */
    @Test
    public void testProposeNewPassenger() {
        assertTrue(dummyPassengerQueue.areDesksFull());
        dummyPassengerQueue.proposeNewPassenger();
        assertFalse(dummyPassengerQueue.areDesksFull());
    }

    /**
     * Checks that deleting all passengers empties the queue.
     */
    @Test
    public void testDeletePassengers() {
        dummyPassengerQueue.deletePassengers();
        assertTrue(dummyPassengerQueue.isEmpty());
    }

    /**
     * Checks that when all passengers have been processed, the queue is empty.
     */
    @Test
    public void testIsEmpty() {
        // Free a virtual desk
        dummyPassengerQueue.setDesksFull(false);
        // Remove passenger 1
        dummyPassengerQueue.acceptNewPassenger();
        dummyPassengerQueue.setDesksFull(false);
        // Remove passenger 2
        dummyPassengerQueue.acceptNewPassenger();
        dummyPassengerQueue.setDesksFull(false);
        // Remove passenger 3
        dummyPassengerQueue.acceptNewPassenger();
        dummyPassengerQueue.setDesksFull(false);
        assertTrue(dummyPassengerQueue.isEmpty());
    }

    /**
     * Checks that the peek method returns the first passenger in the queue.
     */
    @Test
    public void testPeek() {
        assertEquals(dummyPassenger1, dummyPassengerQueue.peek());
    }
}