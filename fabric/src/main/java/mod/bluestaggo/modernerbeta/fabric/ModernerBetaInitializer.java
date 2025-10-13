package mod.bluestaggo.modernerbeta.fabric;

import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.registry.VanillaRegistryHandler;
import net.minecraft.core.Registry;
import net.minecraft.util.Tuple;

import java.util.List;
import java.util.function.Consumer;

public interface ModernerBetaInitializer {
    static void setupRegistryHandlers(List<Tuple<Registry<?>, Consumer<IRegistryHandler<?>>>> registries) {
        for (Tuple<Registry<?>, Consumer<IRegistryHandler<?>>> handler : registries) {
            Registry<?> registry = handler.getA();
            IRegistryHandler<?> registryHandler = new VanillaRegistryHandler<>(registry);

            handler.getB().accept(registryHandler);
        }
    }
}
