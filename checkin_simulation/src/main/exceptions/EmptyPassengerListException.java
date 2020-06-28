package main.exceptions;

public class EmptyPassengerListException extends Exception {
    /**
     * @param message
     */
    public EmptyPassengerListException(String message){
        super(message);
    }
}
