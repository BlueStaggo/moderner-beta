package mod.bluestaggo.modernerbeta.util;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record BootstrapDataContextInfoLookup<C>(BootstrapContext<C> context) implements RegistryOps.RegistryInfoLookup {
    @Override
    //~ if >=26.3 'RegistryOps.RegistryInfo<T>' -> 'HolderGetter<T>'
    public <T> @NotNull Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> registryKey) {
        HolderGetter<T> lookup = context.lookup(registryKey);
        //? if >=26.3 {
        /*return Optional.of(lookup);
        *///? } else {
        return Optional.of(new RegistryOps.RegistryInfo<>(null, lookup, com.mojang.serialization.Lifecycle.stable()));
        //? }
    }
}
