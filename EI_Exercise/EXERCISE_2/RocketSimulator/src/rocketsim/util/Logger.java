package rocketsim.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A Singleton logger class to handle writing mission events to a log file.
 * FINAL VERSION: Includes robust exception handling for file I/O operations.
 */
public class Logger {
    private static Logger instance;
    private static final String LOG_FILE = "mission_log.txt";
    private boolean isFirstLog = true;

    // Private constructor to prevent instantiation
    private Logger() {}

    /**
     * Provides the global access point to the Logger instance.
     * @return The singleton Logger instance.
     */
    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    /**
     * Logs a message to the mission_log.txt file with a timestamp.
     * Overwrites the file on the first call of a new run, then appends.
     * @param eventType The type of event (e.g., "STATUS", "COMMAND", "FAILURE").
     * @param message The details of the event.
     */
    public void log(String eventType, String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        String logEntry = String.format("[%s] [%s] %s", timestamp, eventType, message);

        // Use try-with-resources for safe file handling.
        // The 'isFirstLog' flag controls whether to append or overwrite.
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, !isFirstLog))) {
            writer.println(logEntry);
            if (isFirstLog) {
                isFirstLog = false; // Subsequent logs in this run will append.
            }
        } catch (IOException e) {
            // If logging fails, print an error to the console but do not crash the simulation.
            System.err.println("!!! Logger Error: Could not write to log file: " + e.getMessage());
        }
    }
}
