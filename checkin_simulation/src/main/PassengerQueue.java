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

    /**
     * Structure of the passengers waiting in the airport with an order. This object's first passenger is a shared object
     * between the Desk (consumer) and WaitingLine (producer).
     * @param passengerList
     *      The list of the passengers present in the airport
     * @param desksFull
     *      True if all the desks are occupied, false otherwise
     * @param done
     *      True if the resource is not being used by a thread, false otherwise
     * @throws EmptyPassengerListException
     *      The list given to the constructor cannot be empty
     */
    public PassengerQueue(ArrayList<Passenger> passengerList, boolean desksFull, boolean done) throws EmptyPassengerListException {
        if ((passengerList == null) && (passengerList.isEmpty())){
            throw new EmptyPassengerListException("Passenger list cannot be empty");
        }
        this.passengerQueue = new LinkedList<>(passengerList);
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

    /**
     * Sets the status of the queue to true. This means there are no more passengers in the queue.
     */
    public void setDone() {
        done = true;
    }

    /**
     * Returns the state of the queue. done is set to true when there are no more passengers in the queue, false
     * otherwise.
     * @return done
     *      State of the queue
     */
    public boolean getDone() {
        return done;
    }

     /* =======================
             METHODS
    ======================= */

    /**
     * While the desks are occupied, waits. When one desk is free, the first passenger is accepted to check-in.
     * This method works as the shared object "get" equivalent (for the consumer).
     * @return firstPassenger
     *      The first passenger in the line (the shared object between Desk and WaitingLine)
     */
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

    /**
     * While one desk is free, waits. When all the desks are occupied, a new first passenger is proposed to check-in.
     * This is done by putting the first passenger in the queue as the shared object.
     * This method works as the shared object "put" equivalent (for the consumer).
     */
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

    /**
     * Checks if the queue is empty
     * @return isEmpty
     *      True if the queue is empty, false otherwise.
     */
    public boolean isEmpty() {
        return passengerQueue.isEmpty();
    }

    public String toString(){
        return passengerQueue.toString();
    }
}
