package main;

import java.util.ArrayList;

public class Flight {

    /* =======================
        INSTANCE VARIABLES
    ======================= */

    private String destination;
    private String carrier;
    private int maxPassengers;
    private int maxWeight;
    private int maxVolume;
    private String flightRef;
    private int baggageMaxWeight;
    private int baggageMaxVolume;
    private int excessFee;
    private ArrayList<Passenger> passengerList;

    /* =======================
           CONSTRUCTORS
    ======================= */

    /**
     * @param destination
     * @param carrier
     * @param maxPassengers
     * @param maxWeight
     * @param maxVolume
     * @param flightRef
     * @param baggageMaxWeight
     * @param baggageMaxVolume
     * @param excessFee
     */
    public Flight(String destination, String carrier, int maxPassengers, int maxWeight, int maxVolume, String flightRef,
                  int baggageMaxWeight, int baggageMaxVolume, int excessFee) {
        this.destination = destination;
        this.carrier = carrier;
        this.maxPassengers = maxPassengers;
        this.maxWeight = maxWeight;
        this.maxVolume = maxVolume;
        this.flightRef = flightRef;
        this.baggageMaxWeight = baggageMaxWeight;
        this.baggageMaxVolume = baggageMaxVolume;
        this.excessFee = excessFee;
        this.passengerList = new ArrayList<Passenger>();
    }

    /**
     * @param destination
     * @param carrier
     * @param maxPassengers
     * @param maxWeight
     * @param maxVolume
     * @param flightRef
     */
    public Flight(String destination, String carrier, int maxPassengers, int maxWeight, int maxVolume, String flightRef) {
        this(destination,carrier,maxPassengers,maxWeight,maxVolume,flightRef,30,23,30);
    }

    /* =======================
            ACCESSORS
    ======================= */

    /**
     * @return
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @return
     */
    public String getCarrier() {
        return carrier;
    }

    /**
     * @return
     */
    public int getMaxPassengers() {
        return maxPassengers;
    }

    /**
     * @return
     */
    public int getMaxWeight() {
        return maxWeight;
    }

    /**
     * @return
     */
    public int getMaxVolume() {
        return maxVolume;
    }

    /**
     * @return
     */
    public String getFlightRef() {
        return flightRef;
    }

    /**
     * @return
     */
    public int getBaggageMaxWeight() {
        return baggageMaxWeight;
    }

    /**
     * @return
     */
    public int getBaggageMaxVolume() {
        return baggageMaxVolume;
    }

    /**
     * @return
     */
    public int getExcessFee() { return excessFee; }

    /* =======================
             METHODS
    ======================= */

    /**
     * @param newPassenger
     */
    public void addPassenger(Passenger newPassenger){
        passengerList.add(newPassenger);
    }

    /**
     * @return
     */
    public int totalWeight(){
        int total = 0;
        for (Passenger passenger : passengerList){
            total = total + passenger.getBaggage().getWeight();
        }
        return total;
    }

    /**
     * @return
     */
    public int totalVolume(){
        int total = 0;
        for (Passenger passenger : passengerList){
            total = total + passenger.getBaggage().getVolume();
        }
        return total;
    }

    /**
     * @return
     */
    public int totalPassengers(){
        return (passengerList.size());
    }

    /**
     * @return
     */
    public boolean checkWeight(){
        return (totalWeight() <= maxWeight);
    }

    /**
     * @return
     */
    public boolean checkVolume(){
        return (totalVolume() <= maxVolume);
    }

    /**
     * @return
     */
    public boolean checkPassengers(){
        return (passengerList.size() <= maxPassengers);
    }

}
