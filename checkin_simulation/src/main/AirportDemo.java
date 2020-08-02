package main;

import main.exceptions.EmptyPassengerListException;

import javax.swing.*;
import java.util.ArrayList;

public class AirportDemo {
    public static void main(String[] args) throws InterruptedException {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                Airport model = new Airport();                                      // Model
                model = Serializer.defaultFileToAirport();
                AirportView view  = new AirportView(model);
                AirportController controller = new AirportController(model, view);  // Controller
                view.setVisible(true);

            }

        });
    }
}
