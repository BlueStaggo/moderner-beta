package mod.bluestaggo.modernerbeta.fabric;

import com.mojang.datafixers.util.Pair;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.registry.VanillaRegistryHandler;
import net.minecraft.core.Registry;

import java.util.List;
import java.util.function.Consumer;

public interface ModernerBetaInitializer {
    static void setupRegistryHandlers(List<Pair<Registry<?>, Consumer<IRegistryHandler<?>>>> registries) {
        for (Pair<Registry<?>, Consumer<IRegistryHandler<?>>> handler : registries) {
            Registry<?> registry = handler.getFirst();
            IRegistryHandler<?> registryHandler = new VanillaRegistryHandler<>(registry);

            handler.getSecond().accept(registryHandler);
        }
    }
}
