package main;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
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

   private ArrayList<Passenger> passengerList;

    JTextField bookingRef;
    JTextField passengerName;
    JTextField baggageWeight;
    JTextField baggageWidth;
    JTextField baggageHeight;
    JTextField baggageLength;
    JButton checkIn;
    JTextArea displayList;
    JScrollPane scrollList;
    boolean match = false;


    /**
     * GUI constructor
     * Call methods to create the interface.
     * @param passengerList
     */

    public GUI(ArrayList<Passenger> passengerList) {
        this.passengerList=passengerList;
        setTitle("Check-in GUI");
        setupNorthPanel();
        setupCenterPanel();
        setupSouthPanel();
        pack();
        setVisible(true);

        /**
         * Closing Event
         */
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Exit the program
                System.exit(0);
            }
        });
    }

    /* =======================
      INTERFACE CONSTRUCTION
    ======================= */

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
            int bWeight= Integer.parseInt(baggageWeight.getText().trim());
            int bLength= Integer.parseInt(baggageLength.getText().trim());
            int bWidth= Integer.parseInt(baggageWidth.getText().trim());
            int bHeight= Integer.parseInt(baggageHeight.getText().trim());
            String bRef=bookingRef.getText().trim();
            String pName=passengerName.getText().trim();

            for (Passenger passenger: passengerList) {
                String name = passenger.getName();
                String lastName = passenger.getLastName();
                String bookingRef = passenger.getBookingReference();

                if (lastName.equals(pName) && bookingRef.equals(bRef)){
                    boolean checkIn = passenger.isCheckedIn();
                    match = true;
                    if (checkIn == true) {
                        displayList.setText(displayList.getText() + "Check-In status: " + checkIn + "\n");
                        displayList.setText(displayList.getText() + "This passenger is already checked-in." + "\n");
                    }else {
                        Baggage baggage = new Baggage(bLength, bHeight, bWidth, bWeight);
                        passenger.setBaggage(baggage);
                        passenger.setCheckIn(true);
                        boolean checkIn2 = passenger.isCheckedIn();
                        displayList.setText(displayList.getText() + "Check-In status:" + checkIn2 + "\n");
                        displayList.setText(displayList.getText() + "This passenger is now checked-in with his baggage." + "\n");
                        if (bWeight>30 && (bLength>23 || bHeight>23 || bWidth>23) )
                        {
                            displayList.setText(displayList.getText() + "Dimension & weight excess : +60£" + "\n");
                        }
                        else if (bWeight>20)
                        {
                            displayList.setText(displayList.getText() + "Weight excess : +30£" + "\n");
                        }
                        else if (bLength>20 || bHeight>20 || bWidth>20)
                        {
                            displayList.setText(displayList.getText() + "Dimension excess : +30£" + "\n");
                        }
                    }
                }
            }

            if (match == false)
            {
                displayList.setText(displayList.getText() + "Booking reference and passenger name don't match." + "\n");
            }

        }catch(NumberFormatException e) {
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
}


