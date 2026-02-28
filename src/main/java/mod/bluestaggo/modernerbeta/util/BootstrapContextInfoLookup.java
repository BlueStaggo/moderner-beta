package mod.bluestaggo.modernerbeta.util;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record BootstrapContextInfoLookup<C>(BootstrapContext<C> context) implements RegistryOps.RegistryInfoLookup {
    @Override
    public <T> @NotNull Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> registryKey) {
        return Optional.of(new RegistryOps.RegistryInfo<>(null, context.lookup(registryKey), Lifecycle.stable()));
    }
}
