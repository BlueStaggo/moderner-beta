package mod.bluestaggo.modernerbeta.registry;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface IRegistryHandler<V> {
    <T extends V> @NotNull T register(ResourceLocation id, T value);
}
