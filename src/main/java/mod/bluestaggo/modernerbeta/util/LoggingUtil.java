package mod.bluestaggo.modernerbeta.util;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class LoggingUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModernerBeta.MOD_NAME);

    public static void log(Level level, String message) {
        LOGGER.atLevel(level).log("[" + ModernerBeta.MOD_NAME + "] {}", message);
    }

    public static void log(String message) {
        log(Level.INFO, message);
    }
}
