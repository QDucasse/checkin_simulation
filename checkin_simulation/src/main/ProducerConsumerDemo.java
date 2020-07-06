package main;

import main.exceptions.EmptyPassengerListException;

import java.util.ArrayList;

public class ProducerConsumerDemo {
    public static void main(String[] args) throws InterruptedException {

        Airport dummyAirport = Serializer.defaultFileToAirport();
        ArrayList<Passenger> passengerList = dummyAirport.getPassengerList();
        PassengerQueue passengerQueue = null;
        try {
            passengerQueue = new PassengerQueue(passengerList);
        } catch (EmptyPassengerListException e) {
            e.printStackTrace();
        }

        System.out.println(passengerQueue);
        // Producer/Consumer Creation
        Thread waitingLineThread = new Thread(new WaitingLine(passengerQueue));
        waitingLineThread.start();
        Thread deskThread1 = new Thread(new Desk(passengerQueue,1));
        deskThread1.start();
        Thread deskThread2 = new Thread(new Desk(passengerQueue,2));
        deskThread2.start();

        waitingLineThread.join();
        deskThread1.join();
        deskThread2.join();
    }
}
