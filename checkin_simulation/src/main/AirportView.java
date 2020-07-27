package main;

import main.exceptions.EmptyPassengerListException;
import main.exceptions.NegativeDimensionException;
import main.exceptions.NullDimensionException;
import java.util.Collections;

import java.util.logging.Logger;


import java.awt.*;
import java.util.List;
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
    private static Logger logger = null;
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
     * @param airport
     *
     */
    public AirportView(Airport airport) {

        setTitle("Airport view");
        this.passengerList = airport.getPassengerList();
        this.flightList = airport.getFlightList();



        Container contentPane = getContentPane();
        contentPane.add(setupClientPanel(), BorderLayout.NORTH);
        contentPane.add(setupDeskPanel(), BorderLayout.CENTER);
        contentPane.add(setupFlightPanel(), BorderLayout.SOUTH);


        pack();
        setVisible(true);

        // Closing event
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /*public AirportView() {
        this("passengers.json","flights.json");
    }*/

    /* =======================
            METHODS
   ======================= */


   /* private ArrayList<Passenger> shufflePassengerList(ArrayList<Passenger> passengerList){
        Collections.shuffle(passengerList);
        return passengerList;
    }*/

    /**
     * Method setting up the client panel (north panel) using Swing.
     * Part of the view dedicated to customers management (waiting clients, flight and baggage information).
     * @return clientPanel
     *      Panel containing the passenger waiting line
     */
    private JPanel setupClientPanel() {

        JPanel clientPanel = new JPanel(new GridLayout(2,1));
        clients = new JTextArea(10,20);
        clients.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        clients.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.LIGHT_GRAY));
        startSimulation = new JButton("Start simulation");
        startSimulation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //threads();
                start(passengerList);
            }
        });
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


    public void addListener(ActionListener al) {

        startSimulation.addActionListener(al);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    /*public synchronized void update(Observable o, Object args) {
        for (Passenger passenger : passengerList) {
            String report = passenger.getFirstName() ;
            clients.setText( report);
            clients.setForeground(Color.BLACK);


        }
    }*/

    public void disableStartSimulationButton(){
        startSimulation.setEnabled(false);
    }

    public void start(ArrayList<Passenger> passengerList)
    {
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {

            @Override
            protected Void doInBackground() throws Exception {



                //Airport dummyAirport = Serializer.defaultFileToAirport();
                //ArrayList<Passenger> passengerList = dummyAirport.getPassengerList();


                PassengerQueue passengerQueue = null;
                Collections.shuffle(passengerList, new Random());

                try {
                    passengerQueue = new PassengerQueue(passengerList);
                } catch (EmptyPassengerListException e) {
                    e.printStackTrace();
                }




                System.out.println(passengerQueue);
                // Producer/Consumer Creation
                Thread waitingLineThread = new Thread(new WaitingLine(passengerQueue));
                WaitingLine waitingLine = new WaitingLine(passengerQueue);
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






                return null;

            }

            @Override
            protected void process(List<String> chunks) {
                for (String line: chunks)
                {
                    clients.setText(line);
                }
            }

            @Override
            protected void done()
            {
                //clients.setText("Done");

            }
        };

        worker.execute();
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


