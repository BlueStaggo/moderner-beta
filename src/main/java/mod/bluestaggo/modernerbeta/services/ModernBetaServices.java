package mod.bluestaggo.modernerbeta.services;

import mod.bluestaggo.modernerbeta.util.LoggingUtil;
import org.slf4j.event.Level;

import java.util.ServiceLoader;

public class ModernBetaServices {
    public static IPlatformHelper PLATFORM = loadService(IPlatformHelper.class);


    private static <T> T loadService(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        LoggingUtil.log(Level.DEBUG, String.format("Loaded %s for service %s", loadedService, clazz));
        return loadedService;
    }
}
