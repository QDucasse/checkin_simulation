package main;

public class Main {

    public static void main(String[] args) {

        Airport airport = new Airport();
        Flight f1 = new Flight("destination", "carrier", 8, 100, 100, "EH145");
        Passenger passenger = new Passenger(airport, "name", "EH145", "1225", true);
        Passenger passenger2 = new Passenger(airport, "joan", "EH146", "1244", false);
        f1.addPassenger(passenger);
        f1.addPassenger(passenger2);
        f1.showGUI();
    }

}
