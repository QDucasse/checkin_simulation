package main;

import java.util.ArrayList;

public class Flight {
    private String destination;
    private String carrier;
    private int maxPassengers;
    private int maxWeight;
    private int maxVolume;
    private String flightRef;
    private int baggageMaxWeight;
    private ArrayList<Passenger> passengerList;

    public Flight(String destination, String carrier, int maxPassengers, int maxWeight, int maxVolume, String flightRef, int baggageMaxWeight) {
        this.destination = destination;
        this.carrier = carrier;
        this.maxPassengers = maxPassengers;
        this.maxWeight = maxWeight;
        this.maxVolume = maxVolume;
        this.flightRef = flightRef;
        this.baggageMaxWeight = baggageMaxWeight;
        this.passengerList = new ArrayList<Passenger>();
    }

    /* ACCESSORS */

    public String getDestination() {
        return destination;
    }

    public String getCarrier() {
        return carrier;
    }

    public int getMaxPassengers() {
        return maxPassengers;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public int getMaxVolume() {
        return maxVolume;
    }

    public String getFlightRef() {
        return flightRef;
    }

    public int getBaggageMaxWeight() {
        return baggageMaxWeight;
    }

    public void addPassenger(Passenger newPassenger){
        passengerList.add(newPassenger);
    }

    public int totalWeight(){
        int total = 0;
        for (Passenger passenger : passengerList){
            total = total + passenger.getBaggage().getWeight();
        }
        return total;
    }

    public int totalVolume(){
        int total = 0;
        for (Passenger passenger : passengerList){
            total = total + passenger.getBaggage().getVolume();
        }
        return total;
    }

    public int totalPassengers(){
        return (passengerList.size());
    }

    public boolean checkWeight(){
        return (totalWeight() <= maxWeight);
    }

    public boolean checkVolume(){
        return (totalVolume() <= maxVolume);
    }

    public boolean checkPassengers(){
        return (passengerList.size() <= maxPassengers);
    }

    public void showGUI(){
        GUI gui = new GUI(passengerList);
        gui.setVisible(true);
    }

}
