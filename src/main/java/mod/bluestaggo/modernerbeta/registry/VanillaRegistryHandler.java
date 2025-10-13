package mod.bluestaggo.modernerbeta.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record VanillaRegistryHandler<V>(Registry<V> registry) implements IRegistryHandler<V> {
    @Override
    public <T extends V> @NotNull T register(ResourceLocation id, T value) {
        return Registry.register(registry, id, value);
    }
}
