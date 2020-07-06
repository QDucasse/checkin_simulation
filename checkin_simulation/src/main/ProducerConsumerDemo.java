package main;

import main.exceptions.EmptyPassengerListException;

import javax.swing.*;
import java.util.ArrayList;

public class ProducerConsumerDemo {
    public static void main(String[] args) throws InterruptedException {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

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
                Thread deskThread1 = new Thread(new Desk(passengerQueue, 1));
                deskThread1.start();
                Thread deskThread2 = new Thread(new Desk(passengerQueue, 2));
                deskThread2.start();

                try {
                    waitingLineThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    deskThread1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    deskThread2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        });
    }
}
