package main;

import main.exceptions.EmptyPassengerListException;

import java.util.ArrayList;

public class ProducerConsumerDemo {
    public static void main(String[] args) {
        ArrayList<Passenger> passengerList = Serializer.defaultFileToPassengerList();
        PassengerQueue passengerQueue = null;
        try {
            passengerQueue = new PassengerQueue(passengerList);
        } catch (EmptyPassengerListException e) {
            e.printStackTrace();
        }
        // Producer/Consumer Creation
        Thread waitingLineThread = new Thread(new WaitingLine(passengerQueue));
        waitingLineThread.start();
        Thread deskThread = new Thread(new Desk(passengerQueue));
        deskThread.start();
    }
}
