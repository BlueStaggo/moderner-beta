package mod.bluestaggo.modernerbeta.registry;

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public record VanillaRegistryHandler<V>(Registry<V> registry) implements IRegistryHandler<V> {
    @Override
    public <T extends V> @NotNull T register(Identifier id, T value) {
        return Registry.register(registry, id, value);
    }
}
