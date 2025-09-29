package utils;

import java.time.LocalDateTime;

public class JavaUtils {

    /**
     * @return The current date and time as a formatted string
     */
    public static String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        return now.toString().replace(":", "-").replace(".", "-");  
    }
}
