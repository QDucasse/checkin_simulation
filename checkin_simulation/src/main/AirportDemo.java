package main;

import main.exceptions.EmptyPassengerListException;
import main.exceptions.FlightNotFoundException;

import javax.swing.*;
import java.util.ArrayList;

public class AirportDemo {
    public static void main(String[] args) throws InterruptedException {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                Airport model = null;                                      // Model
                try {
                    model = new Airport();
                } catch (EmptyPassengerListException e) {
                    e.printStackTrace();
                } catch (FlightNotFoundException e) {
                    e.printStackTrace();
                }
                AirportView view  = new AirportView(model);
                AirportController controller = new AirportController(model, view);  // Controller
                view.setVisible(true);

            }

        });
    }
}
