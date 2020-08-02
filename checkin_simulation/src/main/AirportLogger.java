package main;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Logger class
 */
public class AirportLogger {

    /* =======================
         CLASS VARIABLES
    ======================= */

    private static Logger logger = null;
    private final static String LOGFILE = "logs.txt";
    private final static boolean DRYRUN = false;

    /* =======================
          CONSTRUCTORS
    ======================= */

    /**
     * The initialization can never be done outside of this class. This class is never initialized and simply uses the
     * singleton mechanics of its class variable logger.
     */
    private AirportLogger() { }

    /* =======================
            METHODS
    ======================= */

    /**
     * Setup of the logger with a corresponding file handler and formatter (simple text here).
     * @param fileName
     *      The file where the logs will be written.
     * @param dryRun
     *      No console output if true.
     * @throws IOException
     *      Issues getting or creating the logfile.
     */
    private static void setUp(String fileName, boolean dryRun) throws IOException {
        // Lazy instantiation
        if (logger == null){
            logger = Logger.getLogger(AirportLogger.class.getName());
        }
        // Create and add the file handler with the linked formatter
        FileHandler fileHandler = new FileHandler(fileName);
        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);


        // If dryRun is selected, no console handler will be used
        if (dryRun) logger.setUseParentHandlers(false);

        // Sets the log level
        logger.setLevel(Level.INFO);
    }

    /**
     * Logs a message as an info.
     * @param message
     *      The message to log.
     * @throws IOException
     *      An issue was encountered when opening or writing in the log file.
     */
    public static void log(String message) throws IOException {
        if (logger == null){
            setUp(LOGFILE, DRYRUN);
        }
        logger.info(message);
    }

    /**
     * Log to display when a passenger has been accepted to check-in in a desk.
     * LOG MESSAGE EXAMPLE: "Desk nº3 accepted to check-in: John Doe".
     * @param desk
     *      The desk that accepted the check-in.
     * @param passenger
     *      The passenger that will go through the check-in process.
     * @throws IOException
     *      If the log file is not found.
     */
    public static void logDeskPassengerAccepted(Desk desk, Passenger passenger) throws IOException {
        String messageToLog = String.format("Desk nº%d accepted to check-in: %s", desk.getDeskNumber(), passenger.getFullName());
        log(messageToLog);
    }

    /**
     * Log to display the time taken by a desk to check-in a given passenger.
     * LOG MESSAGE EXAMPLE: "Desk nº3: Check-in completed in 5s".
     * @param desk
     *      The desk that accepted the check-in.
     * @throws IOException
     *      If the log file is not found.
     */
    public static void logDeskTimeTaken(Desk desk, int time) throws IOException {
        String messageToLog = String.format("Desk nº%d: Check-in completed in %ds", desk.getDeskNumber(), time / 1000);
        log(messageToLog);
    }

    /**
     * Log to present the new passenger that is the first one in the queue.
     * LOG MESSAGE EXAMPLE: "Passenger John Doe with reservation AB1CD2 is waiting to check-in for flight EH145"
     * @param passenger
     *      The passenger that is the first in the queue.
     * @throws IOException
     *      If the log file is not found.
     */
    public static void logFirstPassengerInQueue(Passenger passenger) throws IOException {
        String messageToLog = String.format("%s with reservation %s is waiting to check-in for flight %s",
                passenger.getFullName(), passenger.getBookingReference(), passenger.getFlightReference());

        log(messageToLog);
    }

    /**
     * Log to display the correct processing of the check-in.
     * LOG MESSAGE EXAMPLE: "Desk nº3: John Doe is now checked-in with their baggage!"
     * @param passenger
     *      The passenger that checked-in.
     * @throws IOException
     *      If the log file is not found.
     */
    public static void logCheckInComplete(Desk desk, Passenger passenger) throws IOException {
        String messageToLog = String.format("Desk nº%s: %s is now checked-in with their baggage!",
                                            desk.getDeskNumber(), passenger.getFullName());
        log(messageToLog);
    }

    /**
     * Log to display an error in the check-in with an erroneous flight reference.
     * LOG MESSAGE EXAMPLE: "Desk nº3: John Doe has an erroneous flight reference."
     * @param passenger
     *      The passenger that checked-in.
     * @throws IOException
     *      If the log file is not found.
     */
    public static void logCheckInErrorFlight(Desk desk, Passenger passenger) throws IOException {
        String messageToLog = String.format("Desk nº%s: %s has an erroneous flight reference.",
                                            desk.getDeskNumber(), passenger.getFullName());
        log(messageToLog);

    }

    /**
     * Log to display an error in the check-in if it is already done.
     * LOG MESSAGE EXAMPLE: "Desk nº3: John Doe is already checked-in."
     * @param passenger
     *      The passenger that checked-in.
     * @throws IOException
     *      If the log file is not found.
     */
    public static void logCheckInAlreadyDone(Desk desk, Passenger passenger) throws IOException {
        String messageToLog = String.format("Desk nº%s: %s is already checked-in.",
                                            desk.getDeskNumber(), passenger.getFullName());
        log(messageToLog);
    }

    /**
     * Log to display an error in the check-in with a baggage that is too wide.
     * LOG MESSAGE EXAMPLE: "Desk nº3: John Doe has a baggage too wide, the passenger has to pay: 30€."
     * @param passenger
     *      The passenger that checked-in.
     * @param fee
     *      The fee to pay if the volume is exceeded.
     * @throws IOException
     *      If the log file is not found.
     */
    public static void logCheckInBaggageVolume(Desk desk, Passenger passenger, String fee) throws IOException {
        String messageToLog = String.format("Desk nº%s: %s has a baggage too wide, the passenger has to pay: %s€.",
                                            desk.getDeskNumber(), passenger.getFullName(), fee);
        log(messageToLog);
    }

    /**
     * Log to display an error in the check-in with a baggage that is too heavy.
     * LOG MESSAGE EXAMPLE: "Desk nº3: John Doe has a baggage too heavy, the passenger has to pay: 30€."
     * @param passenger
     *      The passenger that checked-in.
     * @param fee
     *      The fee to pay if the weight is exceeded.
     * @throws IOException
     *      If the log file is not found.
     */
    public static void logCheckInBaggageWeight(Desk desk, Passenger passenger, String fee) throws IOException {
        String messageToLog = String.format("Desk nº%s: %s has a baggage too heavy, the passenger has to pay: %s€.",
                                            desk.getDeskNumber(), passenger.getFullName(), fee);
        log(messageToLog);
    }

    /**
     * Log to display an error in the check-in when the flight is full.
     * LOG MESSAGE EXAMPLE: "Desk nº3: John Doe could not check-in, this flight is full."
     * @param passenger
     *      The passenger that checked-in.
     * @throws IOException
     *      If the log file is not found.
     */
    public static void logCheckInFlightFull(Desk desk, Passenger passenger) throws IOException {
        String messageToLog = String.format("Desk nº%s: %s could not check-in, this flight is full.",
                                            desk.getDeskNumber(), passenger.getFullName());
        log(messageToLog);
    }
}
