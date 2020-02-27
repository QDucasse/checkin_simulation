package main;

import java.io.IOException;

public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        /* ====================
             FILE CREATION
        ==================== */
        Passenger dummyPassenger1 = new Passenger("John Doe", "EH145", "AB1CD2", true);
        Passenger dummyPassenger2 = new Passenger( "Jane Doe", "FR145", "AA0BB0", false);
        Passenger dummyPassenger3 = new Passenger( "Bill Murray", "FR145", "AA1BB1", false);

        Flight dummyFlight1 = new Flight("Edinburgh", "RyanAir", 2, 100, 100, "EH145");
        Flight dummyFlight2 = new Flight("Paris", "RyanAir", 2, 100, 100, "FR145");

        Airport dummyAirport = new Airport();
        dummyAirport.addPassenger(dummyPassenger1);
        dummyAirport.addPassenger(dummyPassenger2);
        dummyAirport.addPassenger(dummyPassenger3);
        dummyAirport.addFlight(dummyFlight1);
        dummyAirport.addFlight(dummyFlight2);

        try {
            Serializer serializer = new Serializer(dummyAirport);
            serializer.airportToFile("airport.json");
        } catch (IOException e) {
            System.err.println("An error occurred when savings details");
        }

        /* ====================
             GUI LAUNCH
        ==================== */
        GUI mainWindow = new GUI("airport.json");
        mainWindow.setVisible(true);
    }

}
