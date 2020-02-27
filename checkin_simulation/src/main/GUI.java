package main;
import main.exceptions.BookingRefAndNameNoMatchException;
import main.exceptions.FlightNotFoundException;
import main.exceptions.NullDimensionException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUI extends JFrame implements ActionListener{

	/**
     * Instance variables
     */

    private Serializer serializer;

    private JTextField bookingRef;
    private JTextField passengerName;
    private JTextField baggageWeight;
    private JTextField baggageWidth;
    private JTextField baggageHeight;
    private JTextField baggageLength;
    private JButton checkIn;
    private JTextArea displayList;
    private JScrollPane scrollList;
    private boolean match = false;

    /**
     * GUI constructor
     * Call methods to create the interface.
     * @param serializer
     * @param filename
     */
    public GUI(Serializer serializer, String filename) {
        this.serializer = serializer;
        setTitle("Check-in GUI");
        setupNorthPanel();
        setupCenterPanel();
        setupSouthPanel();
        pack();
        setVisible(true);

        this.serializer.fileToAirport(filename);

        /**
         * Closing Event
         */
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	//Print file to logs
				System.out.println(getSerializer().getAirport().outputReport());
                // Exit the program
                e.getWindow().dispose();
                System.exit(0);
            }
        });
    }

    /**
     * @param filename
     */
    public GUI(String filename) {
        this(new Serializer(filename), filename);
    }

    /**
     * Method to set up the interface using Swing
     * North part of the interface asking passenger information.
     */
    private void setupNorthPanel() {
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
    }
    
    /**
     * Method to set up the GUI using Swing
     * South part of the panel to display information about the check-in and baggage fees.
     */
    private void setupSouthPanel() {
        displayList = new JTextArea(10,10);
        displayList.setFont(new Font (Font.MONOSPACED, Font.PLAIN,14));
        displayList.setEditable(false);
        scrollList = new JScrollPane(displayList);
        this.add(scrollList,BorderLayout.SOUTH);
    }

    /**
     * Method to set up the GUI using Swing
     * Middle part of the GUI asking for baggage information.
     * Check-in button
     */
    private void setupCenterPanel() {
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
    }

    /**
     * Method used for the checkIn button
     * Set the check-in value to true if the passenger last name and booking reference match.
     * Set a baggage for a passenger, display excess fee if applied.
     */
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
            Airport airport = serializer.getAirport();

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

        } catch(NumberFormatException | NullDimensionException e) {
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
        if (e.getSource()==checkIn) {
            checkIn();
        }
    }
    
    /**
     * @return
     */
    public Serializer getSerializer() {
    	return serializer;
    }
}


