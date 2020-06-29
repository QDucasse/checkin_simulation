package main;

import java.util.ArrayList;
import java.util.Objects;

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
     * The Flight class holds the different attributes of a flight (current list of passengers and their baggages,
     * flight reference, destination and carrier) as well as max values for weight, number of passengers or volume.
     * It also knows the limitations on baggage's dimensions and the associated fee if not respected.
     * @param destination
     *    The destination of the flight.
     * @param carrier
     *    The carrier of the flight.
     * @param maxPassengers
     *    The maximum number of passengers allowed in the flight.
     * @param maxWeight
     *    The maximum total baggage weight allowed in the flight.
     * @param maxVolume
     *    The maximum total baggage volume allowed in the flight.
     * @param flightRef
     *    The reference of the flight (<Capitalised Letter> <Capitalised Letter> <Digit> <Digit> <Digit>).
     * @param baggageMaxWeight
     *    The maximum weight allowed for a single baggage.
     * @param baggageMaxVolume
     *    The maximum volume allowed for a single baggage.
     * @param excessFee
     *    The fee if the maximum weight or volume are not respected for a single baggage.
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
     * Default values on baggage restrictions and fees.
     * @param destination
     *    The destination of the flight.
     * @param carrier
     *    The carrier of the flight.
     * @param maxPassengers
     *    The maximum number of passengers allowed in the flight.
     * @param maxWeight
     *    The maximum total baggage weight allowed in the flight.
     * @param maxVolume
     *    The maximum total baggage volume allowed in the flight.
     * @param flightRef
     *    The reference of the flight (<Capitalised Letter> <Capitalised Letter> <Digit> <Digit> <Digit>).
     */
    public Flight(String destination, String carrier, int maxPassengers, int maxWeight, int maxVolume, String flightRef) {
        this(destination,carrier,maxPassengers,maxWeight,maxVolume,flightRef,30,23,30);
    }

    /* =======================
            ACCESSORS
    ======================= */

    /**
     * Getter of the destination.
     * @return destination
     *    The destination of the flight.
     */
    public String getDestination() { return destination; }

    /**
     * Getter of the carrier.
     * @return carrier
     *    The carrier of the flight.
     */
    public String getCarrier() { return carrier; }

    /**
     * Getter of the maximum number of passengers.
     * @return maxPassengers
     *    The maximum total number of passengers allowed in the flight.
     */
    public int getMaxPassengers() { return maxPassengers; }

    /**
     * Getter of the maximum total weight.
     * @return maxWeight
     *    The maximum total weight allowed in the flight.
     */
    public int getMaxWeight() { return maxWeight; }

    /**
     * Getter of the maximum total volume.
     * @return maxVolume
     *    The maximum total volume allowed in the flight.
     */
    public int getMaxVolume() { return maxVolume; }

    /**
     * Getter of the flight reference.
     * @return flightRef
     *    The flight reference of the flight.
     */
    public String getFlightRef() { return flightRef; }

    /**
     * Getter of the maximum weight of a baggage.
     * @return baggageMaxWeight
     *    The maximum weight of a single baggage allowed in the flight.
     */
    public int getBaggageMaxWeight() { return baggageMaxWeight; }

    /**
     * Getter of the maximum volume of a baggage.
     * @return baggageMaxVolume
     *    The maximum volume of a single baggage allowed in the flight.
     */
    public int getBaggageMaxVolume() { return baggageMaxVolume; }

    /**
     * Getter of the excess fee.
     * @return excessFee
     *    The fee applied if the weight or dimensions of a single baggage are not respected.
     */
    public int getExcessFee() { return excessFee; }

    /* =======================
             METHODS
    ======================= */

    /**
     * Add a passenger to the flight list.
     * @param newPassenger
     *    The passenger to add.
     */
    public void addPassenger(Passenger newPassenger){
        passengerList.add(newPassenger);
    }

    /**
     * Compute the total baggage weight boarding the flight.
     * @return total
     *    The sum of the weight of all baggage.
     */
    public int totalWeight(){
        int total = 0;
        for (Passenger passenger : passengerList){
            total = total + passenger.getBaggage().getWeight();
        }
        return total;
    }

    /**
     * Compute the total baggage volume boarding the flight.
     * @return total
     *    The sum of the volume of all baggage.
     */
    public int totalVolume(){
        int total = 0;
        for (Passenger passenger : passengerList){
            total = total + passenger.getBaggage().getVolume();
        }
        return total;
    }

    /**
     * Compute the total number of passengers boarding the flight.
     * @return total
     *    The total number of passengers.
     */
    public int totalPassengers(){
        return (passengerList.size());
    }

    /**
     * Run the total boarded weight against the maximum allowed.
     * @return weightRespected
     *    A boolean stating if the maximum weight is respected or not.
     */
    public boolean checkWeight(){
        return (totalWeight() <= maxWeight);
    }

    /**
     * Run the total boarded volume against the maximum allowed.
     * @return volumeRespected
     *    A boolean stating if the maximum volume is respected or not.
     */
    public boolean checkVolume(){
        return (totalVolume() <= maxVolume);
    }

    /**
     * Run the total number of passengers against the maximum allowed.
     * @return nbPassengersRespected
     *    A boolean stating if the maximum number of passengers is respected or not.
     */
    public boolean checkPassengers(){
        return (passengerList.size() <= maxPassengers);
    }

    /* =======================
       OVERRIDDEN METHODS
   ======================= */

    /**
     * Returns the textual representation of a flight.
     * @return string
     *      Textual representation of a flight.
     */
    @Override
    public String toString(){
        String output = "Flight number " + flightRef;
        output += " going to " + destination + " with carrier " + carrier;
        return output;
    }

    /**
     * Test equality between a flight and another object by comparing their attributes.
     * @param o
     *      The object to compare to the instance
     * @return
     *      True if they are the same, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return maxPassengers == flight.maxPassengers &&
                maxWeight == flight.maxWeight &&
                maxVolume == flight.maxVolume &&
                baggageMaxWeight == flight.baggageMaxWeight &&
                baggageMaxVolume == flight.baggageMaxVolume &&
                excessFee == flight.excessFee &&
                Objects.equals(destination, flight.destination) &&
                Objects.equals(carrier, flight.carrier) &&
                Objects.equals(flightRef, flight.flightRef) &&
                Objects.equals(passengerList, flight.passengerList);
    }

    /**
     * Generates a correct hashcode with the object attributes.
     * @return hash
     *      The hashcode of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(destination, carrier, maxPassengers, maxWeight, maxVolume, flightRef, baggageMaxWeight, baggageMaxVolume, excessFee, passengerList);
    }
}
