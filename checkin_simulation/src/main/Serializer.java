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

    public Serializer(Airport airport){
        this.airport = airport;
    }

    public Serializer(String filename) {
        Airport airport = fileToAirport(filename);
        this.airport = airport;
    }

    /* =======================
           ACCESSORS
    ======================= */

    public Airport getAirport(){
        return airport;
    }

    /* =======================
            METHODS
    ======================= */

    public void airportToFile(String outputFilename) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileOutputStream stream = new FileOutputStream(new File(outputFilename));
        String toWrite = gson.toJson(airport);
        stream.write(toWrite.getBytes());
        stream.close();
    }

    public Airport fileToAirport(String filename){
        try {
            Airport airport = null;
            Gson gson = new Gson();

            String text = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
            airport = gson.fromJson(text, Airport.class);
            return airport;
        }
        catch(IOException e) {
            System.out.println(e);
        }
        return null;
    }

    public void checkLists(Airport airport){
        airport.checkLists();
    }
}
