package main;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Logger class
 */
public class Log {
    private final static Logger log = Logger.getLogger(Log.class.getName());

    /**
     * Main method + write log to file using FileHandler
     *
     * @param args
     * @throws IOException
     * @throws SecurityException
     */
    public static void main(String[] args) throws IOException {
        FileHandler fh;
        try {
            fh = new FileHandler("logFile.log");
            log.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            log.info("INFO log");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }

        log.info("INFO message");


        /**To remove console handler */
        //log.setUseParentHandlers(false);
    }



}
