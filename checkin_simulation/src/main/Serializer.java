package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Serializer {

    /* =======================
        INSTANCE VARIABLES
    ======================= */

    private Airport airport;

    public String airportToReport(){
        return airport.outputReport();
    }

    public void fileToAirport(String filename){
        try
        {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);
            airport = (Airport)in.readObject();
            in.close();
            file.close();
        }
        catch(IOException | ClassNotFoundException e)
        {
            System.out.println(e);
        }
    }
}
