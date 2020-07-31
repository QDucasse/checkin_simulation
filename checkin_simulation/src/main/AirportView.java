package main;

import main.exceptions.*;

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
        this.airport=airport;
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

    /* =======================
            METHODS
   ======================= */


    public void addListener(ActionListener al) {

        startSimulation.addActionListener(al);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }


    public void disableStartSimulationButton(){
        startSimulation.setEnabled(false);
    }

    public void start(ArrayList<Passenger> passengerList)
    {
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {

            @Override
            protected Void doInBackground() throws Exception {

                PassengerQueue passengerQueue = null;

                //Shuffle passengers in passengerList
                Collections.shuffle(passengerList, new Random());
                System.out.println(passengerList);
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
                Thread deskThread1 = new Thread(new Desk(airport, passengerQueue, 1));
                deskThread1.start();
                Thread deskThread2 = new Thread(new Desk(airport, passengerQueue, 2));
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
                for (String i: chunks)
                {
                    clients.setText(i);
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

}


