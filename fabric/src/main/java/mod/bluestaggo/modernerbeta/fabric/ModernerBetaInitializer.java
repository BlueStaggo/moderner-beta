package mod.bluestaggo.modernerbeta.fabric;

import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.registry.VanillaRegistryHandler;
import net.minecraft.registry.Registry;

import java.util.Map;
import java.util.function.Consumer;

public interface ModernerBetaInitializer {
    static void setupRegistryHandlers(Map<Registry<?>, Consumer<IRegistryHandler<?>>> map) {
        for (Map.Entry<Registry<?>, Consumer<IRegistryHandler<?>>> handler : map.entrySet()) {
            Registry<?> registry = handler.getKey();
            IRegistryHandler<?> registryHandler = new VanillaRegistryHandler<>(registry);

            handler.getValue().accept(registryHandler);
        }
    }
}
