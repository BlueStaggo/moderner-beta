package mod.bluestaggo.modernerbeta.util;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class LoggingUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModernerBeta.MOD_NAME);

    public static void log(Level level, String message, Object... args) {
        String template = "[" + ModernerBeta.MOD_NAME + "] " + message;

        switch (level) {
            case TRACE:
                LOGGER.trace(template, args);
                break;
            case DEBUG:
                LOGGER.debug(template, args);
                break;
            case INFO:
                LOGGER.info(template, args);
                break;
            case WARN:
                LOGGER.warn(template, args);
                break;
            case ERROR:
                LOGGER.error(template, args);
                break;
            default:
                throw new IllegalArgumentException("Unknown logging level: " + level);
        }
    }

    public static void log(String message) {
        log(Level.INFO, message);
    }
}
