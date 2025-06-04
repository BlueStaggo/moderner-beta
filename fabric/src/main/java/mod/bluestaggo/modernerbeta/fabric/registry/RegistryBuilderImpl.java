package mod.bluestaggo.modernerbeta.fabric.registry;

import mod.bluestaggo.modernerbeta.registry.IRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registry;

public class RegistryBuilderImpl<T> implements IRegistryBuilder<T> {
    private final FabricRegistryBuilder<T, MutableRegistry<T>> registryBuilder;

    public RegistryBuilderImpl(MutableRegistry<T> registry) {
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
        /*ModernerBeta.log(Level.WARN,
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
