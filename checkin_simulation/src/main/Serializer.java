package main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import main.exceptions.EmptyPassengerListException;
import main.exceptions.FlightNotFoundException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Serializer {

    /* ========================
       SERIALIZATION METHODS
    ======================== */

    /**
     * Serialize the given airport (passenger list and flight list).
     * @param airport
     *    The airport to serialize.
     * @param flightOutputFilename
     *    The name of the output file the flights will be stored upon.
     * @param passengerOutputFilename
     *    The name of the output file the passengers will be stored upon.
     * @throws IOException
     *    In case there is an issue when opening the file.
     */
    public static void airportToFile(Airport airport, String flightOutputFilename, String passengerOutputFilename) throws IOException {
        Serializer.flightsToFile(airport.getFlightList(),flightOutputFilename);
        Serializer.passengersToFile(airport.getPassengerList(),passengerOutputFilename);
    }

    /**
     * Above method with the default flights and passenger files.
     * @param airport
     *      Airport to serialize.
     * @throws IOException
     *      In case there is an issue when opening the file.
     */
    public static void defaultAirportToFile(Airport airport) throws IOException {
        Serializer.defaultFlightsToFile(airport.getFlightList());
        Serializer.defaultPassengersToFile(airport.getPassengerList());
    }

    /**
     * Serialize the given list of flights in the given filename.
     * @param flightList
     *    The list of flights to serialize.
     * @param outputFilename
     *    The file name of the output file.
     * @throws IOException
     *    In case there is an issue when opening the file.
     */
    public static void flightsToFile(ArrayList<Flight> flightList, String outputFilename) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileOutputStream stream = new FileOutputStream(new File(outputFilename));
        String toWrite = gson.toJson(flightList);
        stream.write(toWrite.getBytes());
        stream.close();
    }

    /**
     * Above method with a default output file name "flights.json".
     * @param flightList
     *    The list of flights to serialize.
     * @throws IOException
     *    In case there is an issue when opening the file.
     */
    public static void defaultFlightsToFile(ArrayList<Flight> flightList) throws IOException{
        Serializer.flightsToFile(flightList,"flights.json");
    }

    /**
     * Serialize the given list of passengers in the given filename.
     * @param passengerList
     *    The list of passengers to serialize.
     * @param outputFilename
     *    The file name of the output file.
     * @throws IOException
     *    In case there is an issue when opening the file.
     */
    public static void passengersToFile(ArrayList<Passenger> passengerList, String outputFilename) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileOutputStream stream = new FileOutputStream(new File(outputFilename));
        String toWrite = gson.toJson(passengerList);
        stream.write(toWrite.getBytes());
        stream.close();
    }

    /**
     * Above method with a default output file name "passengers.json".
     * @param passengerList
     *    The list of passengers to serialize.
     * @throws IOException
     *    In case there is an issue when opening the file.
     */
    public static void defaultPassengersToFile(ArrayList<Passenger> passengerList) throws IOException{
        Serializer.passengersToFile(passengerList,"passengers.json");
    }


    /* ==========================
       DESERIALIZATION METHODS
    ========================== */


    /**
     * Create an airport from the files storing the passengers and flights.
     * @param passengersFileName
     *    Passengers file name.
     * @param flightsFileName
     *    Flights file name.
     * @return airport
     *    Airport with the passengers and flights loaded.
     */
    public static Airport fileToAirport(String passengersFileName, String flightsFileName) throws EmptyPassengerListException, FlightNotFoundException {
        ArrayList<Passenger> passengerList = fileToPassengerList(passengersFileName);
        ArrayList<Flight> flightList = fileToFlightList(flightsFileName);
        Airport airport = new Airport(passengerList,flightList);
        return airport;
    }

    /**
     * Default behaviour of the above method with default filenames "passengers.json" and "flights.json".
     * @return airport
     *    Airport with the passengers and flights loaded.
     */
    public static Airport defaultFileToAirport() throws EmptyPassengerListException, FlightNotFoundException {
        Airport airport = new Airport(defaultFileToPassengerList(), defaultFileToFlightList());
        return airport;
    }

    /**
     * Create a passengers list from a given file name.
     * @param filename
     *    Name of the file containing the serialized passengers.
     * @return passengerList
     *    Passenger list with the loaded passengers.
     */
    public static ArrayList<Passenger> fileToPassengerList(String filename){
    	try {
            ArrayList<Passenger> passengerList = new ArrayList<>();
            Gson gson = new Gson();

            String text = Files.readString(Paths.get(filename));
            JsonArray array = JsonParser.parseString(text).getAsJsonArray();
            for (int i = 0; i < array.size(); i=i+1){
                Passenger passenger = gson.fromJson(array.get(i), Passenger.class);
                passengerList.add(passenger);
            }
            return passengerList;
        }
        catch(IOException | JsonSyntaxException e) {
            System.err.println("Something went wrong while reading the file : " + e);
        }
        return null;
    }

    /**
     * Default behaviour of the above method (from "passengers.json").
     * @return passengerList
     *    Passenger list with the loaded passengers
     */
    public static ArrayList<Passenger> defaultFileToPassengerList(){
        return Serializer.fileToPassengerList("passengers.json");
    }

    /**
     * Create a flight list from a given file name.
     * @param filename
     *    Name of the file containing the serialized flights.
     * @return passengerList
     *    Flight list with the loaded flights.
     */
    public static ArrayList<Flight> fileToFlightList(String filename){
    	try {
            ArrayList<Flight> flightList = new ArrayList<Flight>();
            Gson gson = new Gson();

            String text = Files.readString(Paths.get(filename));
            JsonArray array = JsonParser.parseString(text).getAsJsonArray();
            for (int i = 0; i < array.size(); i=i+1){
                Flight flight = gson.fromJson(array.get(i), Flight.class);
                flightList.add(flight);
            }
            return flightList;
        }
        catch(IOException | JsonSyntaxException e) {
            System.err.println("Something went wrong while reading the file : " + e);
        }
        return null;
    }

    /**
     * Default behaviour of the above method (from "flights.json").
     * @return flightList
     *    Flight list with the loaded flights
     */
    public static ArrayList<Flight> defaultFileToFlightList(){
        return Serializer.fileToFlightList("flights.json");
    }
}
