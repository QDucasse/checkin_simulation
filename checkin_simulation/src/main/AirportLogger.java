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
    private static void log(String message) throws IOException {
        if (logger == null){
            setUp(LOGFILE, DRYRUN);
        }
        logger.info(message);
    }

    public static void logDeskPassengerAccepted(Desk desk, Passenger passenger) throws IOException {
        String messageToLog = "Desk nº" + desk.getDeskNumber() + " accepted to check-in: " + passenger.getFullName();
        log(messageToLog);
    }

    public static void logDeskTimeTaken(Desk desk, int time) throws IOException {
        String messageToLog = "Desk nº" + desk.getDeskNumber() + ": Check-in completed in " + time/1000 + "s";
        log(messageToLog);
    }

    public static void logPassenger(Passenger passenger) throws IOException {
        String messageToLog = "Passenger " + passenger.getFullName() + " with reservation " + passenger.getBookingReference()
                                                    + " is waiting to check-in for flight " + passenger.getFlightReference();
        log(messageToLog);
    }
}
