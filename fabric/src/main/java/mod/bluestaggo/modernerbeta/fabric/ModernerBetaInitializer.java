package mod.bluestaggo.modernerbeta.fabric;

import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.registry.VanillaRegistryHandler;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;

import java.util.List;
import java.util.function.Consumer;

public interface ModernerBetaInitializer {
    static void setupRegistryHandlers(List<Pair<Registry<?>, Consumer<IRegistryHandler<?>>>> registries) {
        for (Pair<Registry<?>, Consumer<IRegistryHandler<?>>> handler : registries) {
            Registry<?> registry = handler.getLeft();
            IRegistryHandler<?> registryHandler = new VanillaRegistryHandler<>(registry);

            handler.getRight().accept(registryHandler);
        }
    }
}
