package main.exceptions;

public class BookingRefAndNameNoMatchException extends Exception {
    /**
     * @param message
     */
    public BookingRefAndNameNoMatchException(String message){
        super(message);
    }
}
