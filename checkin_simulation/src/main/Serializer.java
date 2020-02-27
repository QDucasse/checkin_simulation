package main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Serializer {

    /* =======================
        INSTANCE VARIABLES
    ======================= */

    private Airport airport;

    /* =======================
           CONSTRUCTORS
    ======================= */

    /**
     * @param airport
     */
    public Serializer(Airport airport){
        this.airport = airport;
    }

    /**
     * @param filename
     */
    public Serializer(String filename) {
        Airport airport = fileToAirport(filename);
        this.airport = airport;
    }

    /* =======================
           ACCESSORS
    ======================= */

    /**
     * @return
     */
    public Airport getAirport(){
        return airport;
    }

    /* =======================
            METHODS
    ======================= */

    /**
     * @param outputFilename
     * @throws IOException
     */
    public void airportToFile(String outputFilename) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileOutputStream stream = new FileOutputStream(new File(outputFilename));
        String toWrite = gson.toJson(airport);
        stream.write(toWrite.getBytes());
        stream.close();
    }

    /**
     * @param filename
     * @return
     */
    public Airport fileToAirport(String filename){
        try {
            Airport airport = null;
            Gson gson = new Gson();

            String text = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
            airport = gson.fromJson(text, Airport.class);
            return airport;
        }
        catch(IOException e) {
        	System.err.println("Something went wrong while reading the file");
        }
        return null;
    }

    /**
     * @param airport
     */
    public void checkLists(Airport airport){
        airport.checkLists();
    }
}
