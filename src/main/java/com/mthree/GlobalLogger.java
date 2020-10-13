package com.mthree;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GlobalLogger {

    static Logger logger;
    Handler fileHandler;

    private GlobalLogger() throws IOException {
        logger = Logger.getLogger(GlobalLogger.class.getName());
        logger.setLevel(Level.INFO);
        fileHandler = new FileHandler("M3Log.log");
        logger.addHandler(fileHandler);
    }

    
    /** 
     * @return Logger
     */
    private static Logger getLogger() {
        if (logger == null) {
            try {
                new GlobalLogger();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return logger;
    }

    
    /** 
     * @param level
     * @param msg
     */
    public static void log(Level level, String msg) {
        getLogger().log(level, msg);
    }
}