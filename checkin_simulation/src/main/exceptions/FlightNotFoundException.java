package main.exceptions;

public class FlightNotFoundException extends Exception {
    /**
     * @param message
     */
    public FlightNotFoundException(String message){
        super(message);
    }
}
