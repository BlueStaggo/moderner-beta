package mod.bluestaggo.modernerbeta.fabric.registry;

import com.mojang.serialization.Lifecycle;
import mod.bluestaggo.modernerbeta.registry.IRegistryBuilder;
import mod.bluestaggo.modernerbeta.registry.IRegistryHelper;
import net.minecraft.core.DefaultedMappedRegistry;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.registry.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class RegistryHelperImpl implements IRegistryHelper {
    private <T> IRegistryBuilder<T> from(WritableRegistry<T> registry) {
        return new RegistryBuilderImpl<>(registry);
    }

    @Override
    public <T> IRegistryBuilder<T> createSimple(ResourceKey<Registry<T>> key) {
        return from(new MappedRegistry<>(key, Lifecycle.stable(), false));
    }

    @Override
    public <T> IRegistryBuilder<T> createDefaulted(ResourceKey<Registry<T>> key, ResourceLocation defaultKey) {
        return from(new DefaultedMappedRegistry<>(defaultKey.toString(), key, Lifecycle.stable(), false));
    }
}
