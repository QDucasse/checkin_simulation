package main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Serializer {

    /* =======================
            METHODS
    ======================= */

    public static void airportToFile(Airport airport, String outputFilename) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileOutputStream stream = new FileOutputStream(new File(outputFilename));
        String toWrite = gson.toJson(airport);
        stream.write(toWrite.getBytes());
        stream.close();
    }

    public static Airport fileToAirport(String filename){
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

    public void checkObjects(Airport airport){
        airport.checkObjects();
    }
}
