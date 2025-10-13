package mod.bluestaggo.modernerbeta.fabric.registry;

import mod.bluestaggo.modernerbeta.registry.IRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;

public class RegistryBuilderImpl<T> implements IRegistryBuilder<T> {
    private final FabricRegistryBuilder<T, WritableRegistry<T>> registryBuilder;

    public RegistryBuilderImpl(WritableRegistry<T> registry) {
        registryBuilder = FabricRegistryBuilder.from(registry);
    }

    @Override
    public IRegistryBuilder<T> synced() {
        registryBuilder.attribute(RegistryAttribute.SYNCED);
        return this;
    }

    @Override
    public IRegistryBuilder<T> optional() {
        //? if >=1.21.4 {
        registryBuilder.attribute(RegistryAttribute.OPTIONAL);
        //?} else {
        /*mod.bluestaggo.modernerbeta.ModernerBeta.log(org.slf4j.event.Level.WARN,
            "Creation of optional registry has been attempted." +
            "This is not supported by Fabric API for this version." +
            "Unexpected behavior may arise.");
        *///?}
        return this;
    }

    @Override
    public Registry<T> build() {
        return registryBuilder.buildAndRegister();
    }
}
