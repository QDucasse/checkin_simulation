package main;

import main.exceptions.EmptyPassengerListException;
import main.exceptions.FlightNotFoundException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class AirportView extends JFrame implements Observer {

    /* =======================
        INSTANCE VARIABLES
	======================= */

    private JButton startSimulation;
    private JTextArea clients;
    private JTextArea[] desks = new JTextArea[3];
    private Airport airport;

    private static final int MAX_CHECKIN_DESKS = 3;

    public static final int UPDATE_DESKS = 0;
    public static final int UPDATE_FLIGHT = 1;
    private JTextArea[] flights;


    /* =======================
            CONSTRUCTORS
   ======================= */

    /**
     * GUI constructor
     * Creates and configures main panel and closing event.
     * @param airport
     *    Airport object
     */
    public AirportView(Airport airport) {

        setTitle("Airport view");
        this.airport=airport;
        for (Desk desk : this.airport.getDeskList()){
            desk.addObserver(this);
        }
        Container contentPane = getContentPane();
        contentPane.add(setupClientPanel(), BorderLayout.NORTH);
        contentPane.add(setupDeskPanel(), BorderLayout.CENTER);
        contentPane.add(setupFlightPanel(), BorderLayout.SOUTH);

        pack();
        setVisible(true);

        // Closing event
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    /* =======================
            METHODS
   ======================= */

    /**
     * Method setting up the client panel (north panel) using Swing.
     * Part of the view dedicated to customers management (waiting clients, flight and baggage information).
     * @return clientPanel
     *      Panel containing the passenger waiting line
     */
    private JPanel setupClientPanel() {

        JPanel clientPanel = new JPanel(new GridLayout(2,1));
        clients = new JTextArea(10,20);
        clients.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        clients.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.LIGHT_GRAY));
        startSimulation = new JButton("Start simulation");
        clientPanel.add(startSimulation);

        clientPanel.add(clients);
        return clientPanel;
    }

    /**
     * Method setting up the flight panel (south panel) using Swing
     * Part of the view dedicated to flight check-in management.
     * @return flightPanel
     *      Flight panel containing the different flight information (here 3)
     */
    private JPanel setupFlightPanel() {
        int flightSize = airport.getFlightList().size();

        JPanel flightPanel = new JPanel(new GridLayout(1, flightSize));
        this.flights = new JTextArea[flightSize];

        for (int i=0; i< flightSize; i++)
        {
            flights[i]=new JTextArea(10,20);
            flights[i].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
            flights[i].setLineWrap(true);
            flights[i].setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.LIGHT_GRAY));
            flightPanel.add(flights[i]);
        }
        return flightPanel;
    }

    /**
     * Method setting up the desk panel (center panel) using Swing
     * Part of the view dedicated to passenger check-in management.
     * @return deskPanel
     *      The panel containing the different desks (here 3)
     */
    private JPanel setupDeskPanel() {
        JPanel deskPanel = new JPanel(new GridLayout(1,3));


        for (int i=0; i < MAX_CHECKIN_DESKS; i++)
        {
            desks[i]=new JTextArea(10,20);
            desks[i].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
            desks[i].setLineWrap(true);
            desks[i].setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.LIGHT_GRAY));
            deskPanel.add(desks[i]);
        }
        return deskPanel;
    }

    /* =======================
            METHODS
   ======================= */

    /**
     * Adds a listener for the "Start Simulation" button.
     * @param al
     *      Action listener to add to the button.
     */
    public void addListener(ActionListener al) {
        startSimulation.addActionListener(al);
    }

    /**
     * Disables the "Start Simulation" button.
     */
    public void disableStartSimulationButton(){
        startSimulation.setEnabled(false);
    }

    /**
     * Update method called when an observable sends the "notifyObservers" methods.
     * @param o
     *      Observable object (Desks here).
     * @param arg
     *      Argument of the notification.
     */
    @Override
    public void update(Observable o, Object arg) {
        updateFlights();
        updateDesks();
    }

    private void updateFlights() {
        for (int i = 0; i < airport.getFlightList().size(); i++) {
            Flight f = airport.getFlightList().get(i);

            StringBuilder builder = new StringBuilder();
            builder.append(f.getFlightRef()).append(" ").append(f.getDestination()).append("\n");
            builder.append(f.totalPassengers()).append(" checked in of ").append(f.getMaxPassengers()).append("\n");
            builder.append("Volume is ").append(f.totalVolume()).append("\n");
            builder.append("Hold is ").append(f.getHold()).append("%\n");
            this.flights[i].setText(builder.toString());
        }
    }

    private void updateDesks() {
        for (int i = 0; i < MAX_CHECKIN_DESKS; i++) {
            WaitingLine currentLine = airport.getWaitingLine();
            PassengerQueue currentQueue = currentLine.getPassengerQueue();
            clients.setText("There are currently " + currentQueue.getQueueSize() + " people waiting in the queue:\n" +
                    currentQueue.toString());

            Desk currentDesk = airport.getDeskList().get(i);
            if (currentDesk.getCurrentPassenger() != null) {
                Passenger passenger = currentDesk.getCurrentPassenger();
                Baggage baggage = passenger.getBaggage();

                StringBuilder builder = new StringBuilder();
                builder.append("Desk n°").append(currentDesk.getDeskNumber()).append("\n");
                builder.append(passenger.getFullName()).append(" ");
                Flight f = null;

                try {
                    f = airport.getFlightFromRef(passenger.getFlightReference());
                } catch (FlightNotFoundException e) {
                    e.printStackTrace();
                }


                switch (passenger.getResult()) {
                    case WARNING_BAGGAGE_WEIGHT:
                        builder.append("has checked in with a baggage of " + baggage.getWeight() + "kg.\n A fee of " + f.getExcessFee() +" has been paid due to the weight");
                        break;
                    case WARNING_BAGGAGE_VOLUME:
                        builder.append("has checked in with a baggage of " + baggage.getWeight() + "kg.\n A fee of " + f.getExcessFee() +" has been paid due to the volume");
                        break;
                    case WARNING_ALREADY_DONE:
                        builder.append("is already checked in, he moved to his flight");
                        break;
                    case ERR_HOLD_FULL:
                        builder.append("cannot enter flight with his bag of ").append(baggage.getWeight()).append("since the hold is full");
                        break;
                    case ERR_FLIGHT_IS_FULL:
                        builder.append("cannot enter flight, it is full");
                        break;
                    case DONE:
                        builder.append("has checked in with a baggage of ").append(baggage.getWeight()).append("kg.");
                        break;

                    case ERR_FLIGHT_REFERENCE:
                        builder.append("has a wrong flight reference");
                        break;
                }

               desks[i].setText(builder.toString());

            }
            else {
                desks[i].setText("Desk n°" + currentDesk.getDeskNumber() + " empty");
            }

        }
    }
}


