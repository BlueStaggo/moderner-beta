package mod.bluestaggo.modernerbeta.fabric.registry;

import mod.bluestaggo.modernerbeta.registry.RegistryBuilder;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registry;

public class RegistryBuilderImpl<T> implements RegistryBuilder<T> {
    private final FabricRegistryBuilder<T, MutableRegistry<T>> registryBuilder;

    public RegistryBuilderImpl(MutableRegistry<T> registry) {
        registryBuilder = FabricRegistryBuilder.from(registry);
    }

    @Override
    public RegistryBuilder<T> synced() {
        registryBuilder.attribute(RegistryAttribute.SYNCED);
        return this;
    }

    @Override
    public RegistryBuilder<T> optional() {
        registryBuilder.attribute(RegistryAttribute.OPTIONAL);
        return this;
    }

    @Override
    public Registry<T> build() {
        return registryBuilder.buildAndRegister();
    }
}
