package main;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUI extends JFrame implements ActionListener{

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


    public GUI(ArrayList<Passenger> passengerList) {
        this.passengerList=passengerList;
        setTitle("Check-in GUI");
        setupNorthPanel();
        setupCenterPanel();
        setupSouthPanel();
        pack();
        setVisible(true);
    }

    private void setupNorthPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(2,3));
        searchPanel.add(new JLabel("Booking ref: "));
        bookingRef = new JTextField(5);
        searchPanel.add(bookingRef);
        searchPanel.add(new JLabel("Passenger name: "));
        passengerName = new JTextField(5);
        searchPanel.add(passengerName);
        JPanel northPanel=new JPanel();
        northPanel.setLayout(new GridLayout(2,1));
        northPanel.add(searchPanel);
        this.add(northPanel, BorderLayout.NORTH);
    }

    private void setupSouthPanel() {
        displayList = new JTextArea(10,10);
        displayList.setFont(new Font (Font.MONOSPACED, Font.PLAIN,14));
        displayList.setEditable(false);
        scrollList = new JScrollPane(displayList);
        this.add(scrollList,BorderLayout.SOUTH);
    }

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
                String bookingRef = passenger.getBookingReference();


                if (name.equals(pName) && bookingRef.equals(bRef)){
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

    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==checkIn) {
            checkIn();
        }
    }
}


