package main;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.*;

public class Log {
    private final static Logger logger = Logger.getLogger(Log.class.getName());

    public static void main(String[] args)
    {
        logger.info("INFO message");
    }

}
