package main;

import main.exceptions.EmptyPassengerListException;
import main.exceptions.NullDimensionException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class PassengerQueue {
    /* =======================
        INSTANCE VARIABLES
	======================= */
    private Queue<Passenger> passengerQueue;
    private Passenger firstPassenger;
    private boolean desksFull;
    private boolean done;
    private boolean empty;


    /* =======================
           CONSTRUCTORS
    ======================= */

    public PassengerQueue(ArrayList<Passenger> passengerList, boolean desksFull, boolean done) throws EmptyPassengerListException {
        if ((passengerList == null) && (passengerList.isEmpty())){
            throw new EmptyPassengerListException("Passenger list cannot be empty");
        }
        this.passengerQueue = new LinkedList<Passenger>(passengerList);
        this.firstPassenger = this.passengerQueue.remove();
        this.desksFull = desksFull;
        this.done = done;
        this.empty = false;
    }

    public PassengerQueue(ArrayList<Passenger> passengerList) throws EmptyPassengerListException {
        this(passengerList, true, false);
    }

    /* =======================
            ACCESSORS
    ======================= */

     /* =======================
             METHODS
    ======================= */

    // Shared object "get" equivalent (for the consumer)
    public synchronized Passenger acceptNewPassenger() {
        while (desksFull) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Passenger accepted for check-in: " + firstPassenger.getFullName());
        desksFull = true;
        notifyAll();
        return firstPassenger;
    }

    // Shared object "put" equivalent (for the producer)
    public synchronized void proposeNewPassenger() {
        while (!desksFull) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Passenger passengerToCheckIn = passengerQueue.remove();
        System.out.println("Passenger waiting for check-in: " + passengerToCheckIn.getFullName());
        desksFull = false;
        notifyAll();
        this.firstPassenger = passengerToCheckIn;
    }

    public void setDone() {
        done = true;
    }

    public boolean getDone() {
        return done;
    }

}
