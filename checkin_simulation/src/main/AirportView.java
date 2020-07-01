package main;

import main.exceptions.NegativeDimensionException;
import main.exceptions.NullDimensionException;

import java.awt.*;
import java.util.Random;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AirportView extends JFrame implements ActionListener {

    /* =======================
        INSTANCE VARIABLES
	======================= */

    private JButton startSimulation;
    private JTextArea [] desks;
    private JTextArea [] flights;
    private JTextArea clients;

    private Airport airport;
    private ArrayList<Passenger> passengerList;
    private ArrayList<Flight> flightList;

    /* =======================
            CONSTRUCTORS
   ======================= */

    /**
     * GUI constructor
     * Creates and configures main panel and closing event.
     * @param passengersFileName
     *      Name of the json file containing the passengers information.
     * @param flightsFileName
     *      Name of the json file containing the flights information.
     */
    public AirportView(String passengersFileName, String flightsFileName) {
        // Title of the window
        setTitle("Airport view");
        // Deserializing the passengers and flights into a new airport
        this.airport = Serializer.fileToAirport(passengersFileName, flightsFileName);
        this.passengerList = airport.getPassengerList();
        this.flightList = airport.getFlightList();
        // Create the different panels
        Container contentPane = getContentPane();
        contentPane.add(setupClientPanel(), BorderLayout.NORTH);
        contentPane.add(setupDeskPanel(), BorderLayout.CENTER);
        contentPane.add(setupFlightPanel(), BorderLayout.SOUTH);
        setSize(500,600);
        setVisible(true);
        // Closing event
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public AirportView() {
        this("passengers.json","flights.json");
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
        JPanel clientPanel = new JPanel(new GridLayout(1,1));
        clients = new JTextArea(10,20);
        clients.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        clients.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.LIGHT_GRAY));
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
        JPanel flightPanel = new JPanel(new GridLayout(1,3));
        flights = new JTextArea[3];
        for (int i=0; i<3; i++)
        {
            flights[i]=new JTextArea(10,20);
            flights[i].setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            flights [i].setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.LIGHT_GRAY));
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
        desks = new JTextArea[3];
        for (int i=0; i<3; i++)
        {
            desks[i]=new JTextArea(10,20);
            desks[i].setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            desks [i].setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.LIGHT_GRAY));
            deskPanel.add(desks[i]);
        }
        return deskPanel;
    }

    /**
     * Method to associate random baggage dimension to every passenger in the passenger list
     * Dimensions are included between 1 and 200
     * @throws NegativeDimensionException
     *      Baggage dimensions should not be negative.
     * @throws NullDimensionException
     *      Baggage dimensions should not be null.
     */
    public void randomBaggageToPassenger() throws NegativeDimensionException, NullDimensionException {
        Random random = new Random();
        for (Passenger passenger: passengerList) {
            int bWeight = random.nextInt(200);
            bWeight += 1;
            int bLength = random.nextInt(200);
            bLength += 1;
            int bWidth = random.nextInt(200);
            bWidth += 1;
            int bHeight = random.nextInt(200);
            bHeight += 1;
            Baggage inputBaggage = new Baggage(bLength, bHeight, bWidth, bWeight);
            passenger.setBaggage(inputBaggage);
        }

    }

    /* =======================
            METHODS
   ======================= */

    @Override
    public void actionPerformed(ActionEvent e) {

    }



    /**
     * Method used for the checkIn button
     * Set the check-in value to true if the passenger last name and booking reference match.
     * Set a baggage for a passenger, display excess fee if applied.
     */
    /*
    private void checkIn() {
        try {

            // Baggage creation


            int bWeight= Integer.parseInt(baggageWeight.getText().trim());
            int bLength= Integer.parseInt(baggageLength.getText().trim());
            int bWidth= Integer.parseInt(baggageWidth.getText().trim());
            int bHeight= Integer.parseInt(baggageHeight.getText().trim());
            Baggage inputBaggage = new Baggage(bLength,bHeight,bWidth,bWeight);
            // Ref and name access
            String bRef=bookingRef.getText().trim();
            String pName=passengerName.getText().trim();
            Passenger targetPassenger;

            try {
                targetPassenger = airport.getPassengerFromBookingRefAndName(bRef,pName);
                targetPassenger.setBaggage(inputBaggage);
                Passenger.CheckinResult checkInResult = targetPassenger.checkIn(airport);
                String targetFlightRef = targetPassenger.getFlightReference();
                Flight targetFlight = airport.getFlightFromRef(targetFlightRef);
                String fee = Integer.toString(targetFlight.getExcessFee());
                System.out.println(fee);
                switch(checkInResult){
                    case DONE:
                        displayList.setText(displayList.getText() + "This passenger is now checked-in with his baggage." + "\n");
                        break;
                    case ERR_FLIGHT_REFERENCE:
                        displayList.setText(displayList.getText() + "This flight reference associated to this passenger does not exist." + "\n");
                        break;
                    case WARNING_ALREADY_DONE:
                        displayList.setText(displayList.getText() + "This passenger is already checked-in." + "\n");
                        break;
                    case WARNING_BAGGAGE_VOLUME:
                        displayList.setText(displayList.getText() + "The dimensions are exceeded, the passenger has to pay: " + fee + "\n");
                        break;
                    case WARNING_BAGGAGE_WEIGHT:
                        displayList.setText(displayList.getText() + "The weight is exceeded, the passenger has to pay: " + fee + "\n");
                        break;
                }
            } catch (BookingRefAndNameNoMatchException e) {
                displayList.setText(displayList.getText() + "This booking reference and name do not match" + "\n");;
            } catch (FlightNotFoundException e) {
                displayList.setText(displayList.getText() + "This flight reference associated to this passenger does not exist." + "\n");;
            }

        } catch(NumberFormatException | NullDimensionException | NegativeDimensionException e) {
            System.out.println(e.getMessage());
            displayList.setText(displayList.getText() + "Please insert valid values. "+ "\n");
        }
    }

    /**
     * Method for the button interaction.
     * Call the checkIn method if the checkIn button is pressed on.
     * @param e
     */


}


