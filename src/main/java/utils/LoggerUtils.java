package utils;

public class LoggerUtils {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LoggerUtils.class);

    public static void info(String message) {
        logger.info("Info: " + message);
    }

    public static void error(String message) {
        logger.error("Error: " + message);
    }

    public static void debug(String message) {
        logger.debug("Debug: " + message);
    }

    public static void warn(String message) {
        logger.warn("Warning: " + message);
    }

    public static void success(String message) {
        logger.info("Success: " + message);
    }

    public static void loading(String message) {
        logger.info("Info: " + message);
    }
}
