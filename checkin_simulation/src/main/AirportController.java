package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AirportController {

    /* =======================
        INSTANCE VARIABLES
    ======================= */

    private Airport airport;
    private AirportView view;


    /* =======================
          CONSTRUCTORS
    ======================= */

    public AirportController(Airport airport, AirportView view){
        this.airport = airport;
        this.view = view;

    }

    /* =======================
         ACTION LISTENERS
    ======================= */

    class ProcessPassengersController implements ActionListener {
        public void actionPerformed(ActionEvent e){
//            view.disableOpenDesksButton();
            Thread thread = new Thread(airport);
            thread.start();
        }
    }
}
