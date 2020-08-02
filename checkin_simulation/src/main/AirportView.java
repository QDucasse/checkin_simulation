package main;

import main.exceptions.EmptyPassengerListException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class AirportView extends JFrame implements ActionListener, Observer {

    /* =======================
        INSTANCE VARIABLES
	======================= */

    private JButton startSimulation;
    private JTextArea clients;
    private JTextArea[] desks = new JTextArea[3];
    private Airport airport;
    private ArrayList<Passenger> passengerList;
    private ArrayList<Passenger> economicPassengerList;
    private ArrayList<Passenger> businessPassengerList;
    private ArrayList<Passenger> firstClassPassengerList;


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
        this.passengerList = airport.getPassengerList();

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
        startSimulation.addActionListener(e -> {
            //threads();
            start(passengerList);
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
        JTextArea[] flights = new JTextArea[3];
        for (int i=0; i<3; i++)
        {
            flights[i]=new JTextArea(10,20);
            flights[i].setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
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
        for (int i=0; i<3; i++)
        {
            desks[i]=new JTextArea(10,20);
            desks[i].setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            desks[i].setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.LIGHT_GRAY));
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

    /**
     * Method to sort passengers into different lists depending on their priority.
     * @param passengerList
     *      List of passengers
     */
    public void getPriorityLists(ArrayList<Passenger> passengerList)
    {
        for (Passenger passenger: passengerList)
        {
            String priority = passenger.getPriority();
            switch (priority){
                case "Economic":
                    economicPassengerList.add(passenger);
                    break;
                case "Business":
                    businessPassengerList.add(passenger);
                    break;
                case "First class":
                    firstClassPassengerList.add(passenger);
                    break;
                default:
                    System.out.println("No priority found for " + passenger.getFullName() + ": automatically set to economic class.");
                    economicPassengerList.add(passenger);
                    break;
            }
        }
    }


    public void disableStartSimulationButton(){
        startSimulation.setEnabled(false);
    }

    public void start(ArrayList<Passenger> passengerList)
    {
        SwingWorker<Void, String> worker = new SwingWorker<>() {

            @Override
            protected Void doInBackground() {

                economicPassengerList = new ArrayList<>();
                businessPassengerList = new ArrayList<>();
                firstClassPassengerList = new ArrayList<>();

                PassengerQueue passengerQueueEconomic = null;
                PassengerQueue passengerQueueBusiness = null;
                PassengerQueue passengerQueueFirstClass = null;

                //Shuffle passengers in passengerList
                Collections.shuffle(passengerList, new Random());
                System.out.println(passengerList);

                try {
                    getPriorityLists(passengerList);

                    passengerQueueEconomic = new PassengerQueue(economicPassengerList);
                    passengerQueueBusiness = new PassengerQueue(businessPassengerList);
                    passengerQueueFirstClass = new PassengerQueue(firstClassPassengerList);

                } catch (EmptyPassengerListException e) {
                    e.printStackTrace();
                }

                System.out.println("Economic queue: " + passengerQueueEconomic);
                System.out.println("Business queue: " + passengerQueueBusiness);
                System.out.println("First class queue: " + passengerQueueFirstClass);

                // Producer/Consumer Creation
                WaitingLine waitingLine = new WaitingLine(passengerQueueEconomic);
                Thread waitingLineThread = new Thread(waitingLine);
                waitingLineThread.start();

                WaitingLine waitingLine2 = new WaitingLine(passengerQueueBusiness);
                Thread waitingLineThread2 = new Thread(waitingLine2);
                waitingLineThread2.start();

                WaitingLine waitingLine3 = new WaitingLine(passengerQueueFirstClass);
                Thread waitingLineThread3 = new Thread(waitingLine3);
                waitingLineThread3.start();

                Thread deskThreadEconomic = new Thread(new Desk(airport, passengerQueueEconomic, 1));
                deskThreadEconomic.setPriority(Thread.MIN_PRIORITY);
                deskThreadEconomic.start();

                Thread deskThreadBusiness = new Thread(new Desk(airport, passengerQueueBusiness, 2));
                deskThreadBusiness.setPriority(Thread.NORM_PRIORITY);
                deskThreadBusiness.start();

                Thread deskThreadFirstClass = new Thread(new Desk(airport, passengerQueueFirstClass, 3));
                deskThreadFirstClass.setPriority(Thread.MAX_PRIORITY);
                deskThreadFirstClass.start();


                try {
                    waitingLineThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    waitingLineThread2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    waitingLineThread3.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    deskThreadEconomic.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    deskThreadBusiness.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    deskThreadFirstClass.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                return null;

            }

            @Override
            protected void process(List<String> chunks) {
                for (String i : chunks) {
                    clients.setText(i);
                }
            }

            @Override
            protected void done() {
                //clients.setText("Done");

            }
        };

        worker.execute();
    }

    @Override
    public void update(Observable o, Object arg) {
        for (int i = 0; i < 3; i++) {
            desks[i].setText("Bloub");
        }
    }
}


