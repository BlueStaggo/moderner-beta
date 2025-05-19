package mod.bluestaggo.modernerbeta.registry;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public interface IRegistryHandler<V> {
    <T extends V> @NotNull T register(Identifier id, T value);
}
