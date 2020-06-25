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
    /*
    private JTextField bookingRef;
    private JTextField passengerName;
    private JTextField baggageWeight;
    private JTextField baggageWidth;
    private JTextField baggageHeight;
    private JTextField baggageLength;
    private JButton checkIn;
    private JTextArea displayList;
    private JScrollPane scrollList;

     */

    private JButton startSimulation;
    private JTextArea [] desks;
    private JTextArea [] flights;
    private JTextArea clients;



    private Airport airport;
    private ArrayList<Passenger> passengerList;
    private ArrayList<Flight> flightList;


    /**
     * GUI constructor
     * Creates and configures main panel and closing event.
     * @param filename
     */
    public AirportView(String filename) {

        setTitle("Airport view");

        this.airport = Serializer.defaultFileToAirport();
        this.passengerList = airport.getPassengerList();
        this.flightList = airport.getFlightList();

        Container contentPane = getContentPane();
        contentPane.add(setupClientPanel(), BorderLayout.NORTH);
        contentPane.add(setupDeskPanel(), BorderLayout.CENTER);
        contentPane.add(setupFlightPanel(), BorderLayout.SOUTH);
        //pack();
        setSize(500,600);
        setVisible(true);

        /**
         * Closing Event
         */

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
//        this.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//            	//Print file to logs
//				System.out.println(getAirport().outputReport());
//                // Exit the program
//                e.getWindow().dispose();
//                System.exit(0);
//            }
//        });
    }

    /**
     * Method setting up the client panel (north panel) using Swing.
     * Part of the view dedicated to customers management (waiting clients, flight and baggage information).
     * @return clientPanel
     */
    private JPanel setupClientPanel() {
        /*
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(2,3));
        searchPanel.add(new JLabel("Booking ref: "));
        bookingRef = new JTextField(5);
        searchPanel.add(bookingRef);
        searchPanel.add(new JLabel("Passenger  last name: "));
        passengerName = new JTextField(5);
        searchPanel.add(passengerName);
        JPanel northPanel=new JPanel();
        northPanel.setLayout(new GridLayout(2,1));
        northPanel.add(searchPanel);
        this.add(northPanel, BorderLayout.NORTH);
         */

        /*startSimulation = new JButton("Start Simulation");
        JPanel northPanel = new JPanel();
        northPanel.add(startSimulation);
        return northPanel;*/

        JPanel clientPanel = new JPanel(new GridLayout(1,1));

        clients = new JTextArea();

            clients =new JTextArea(10,20);
            clients.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            clients.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.LIGHT_GRAY));
            clientPanel.add(clients);


        return clientPanel;


    }
    
    /**
     * Method setting up the flight panel (south panel) using Swing
     * Part of the view dedicated to flight check-in management.
     * @return flightPanel
     */
    private JPanel setupFlightPanel() {

        /*
        displayList = new JTextArea(10,10);
        displayList.setFont(new Font (Font.MONOSPACED, Font.PLAIN,14));
        displayList.setEditable(false);
        scrollList = new JScrollPane(displayList);
        this.add(scrollList,BorderLayout.SOUTH);
         */

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
     */
    private JPanel setupDeskPanel() {

        /*

        JPanel bagagePanel = new JPanel();
        bagagePanel.setLayout(new GridLayout(1,2));
        bagagePanel.add(new JLabel("Baggage height: "));
        baggageHeight = new JTextField(5);
        bagagePanel.add(baggageHeight);
        bagagePanel.add(new JLabel("Baggage length: "));
        baggageLength = new JTextField(5);
        bagagePanel.add(baggageLength);
        bagagePanel.add(new JLabel("Baggage width: "));
        baggageWidth = new JTextField(5);
        bagagePanel.add(baggageWidth);
        bagagePanel.add(new JLabel("Baggage weight: "));
        baggageWeight = new JTextField(5);
        bagagePanel.add(baggageWeight);
        checkIn = new JButton("Check-in");
        bagagePanel.add(checkIn);
        checkIn.addActionListener(this);
        JPanel centerPanel=new JPanel();
        centerPanel.setLayout(new GridLayout(5,7));
        centerPanel.add(bagagePanel);
        this.add(centerPanel,BorderLayout.CENTER);

         */

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


    public void actionPerformed(ActionEvent e) {
        /*if (e.getSource()==checkIn) {
            checkIn();
        }*/
    }








    /**
     * Method to associate random baggage dimension to every passenger in the passenger list
     * Dimensions are included between 1 and 200
     * @throws NegativeDimensionException
     * @throws NullDimensionException
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

}


