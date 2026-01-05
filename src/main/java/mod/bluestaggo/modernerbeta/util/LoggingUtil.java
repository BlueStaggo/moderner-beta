package mod.bluestaggo.modernerbeta.util;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class LoggingUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModernerBeta.MOD_NAME);

    public static void log(Level level, String message) {
        String template = "[" + ModernerBeta.MOD_NAME + "] {}";

        switch (level) {
            case TRACE:
                LOGGER.trace(template, message);
                break;
            case DEBUG:
                LOGGER.debug(template, message);
                break;
            case INFO:
                LOGGER.info(template, message);
                break;
            case WARN:
                LOGGER.warn(template, message);
                break;
            case ERROR:
                LOGGER.error(template, message);
                break;
            default:
                throw new IllegalArgumentException("Unknown logging level: " + level);
        }
    }

    public static void log(String message) {
        log(Level.INFO, message);
    }
}
