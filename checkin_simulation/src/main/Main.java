package main;

public class Main {

    public static void main(String[] args) {
        Airport airport = new Airport();
        Flight f1 = new Flight("destination", "carrier", 8, 100, 100, "EH145");
        Passenger passenger1 = new Passenger("name", "EH145", "1225", true);
        Passenger passenger2 = new Passenger("joan", "EH145", "1244", true);
        Baggage baggage1 = new Baggage(10, 20, 30, 9);
        Baggage baggage2 = new Baggage(20, 10, 30, 11);
        passenger1.setBaggage(baggage1);
        passenger2.setBaggage(baggage2);
        f1.addPassenger(passenger1);
        f1.addPassenger(passenger2);
        f1.showGUI();
    }

}
