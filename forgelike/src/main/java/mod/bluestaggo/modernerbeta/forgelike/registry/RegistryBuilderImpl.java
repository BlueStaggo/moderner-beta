package mod.bluestaggo.modernerbeta.forgelike.registry;

import mod.bluestaggo.modernerbeta.registry.IRegistryBuilder;
import net.minecraft.registry.Registry;
//? if neoforge {
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
//?} else {
/*import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;
*///?}

public class RegistryBuilderImpl<T> implements IRegistryBuilder<T> {
    private final NewRegistryEvent event;
    private final RegistryBuilder<T> registryBuilder;
    private boolean sync;

    public RegistryBuilderImpl(NewRegistryEvent event, RegistryBuilder<T> registryBuilder) {
        this.event = event;
        this.registryBuilder = registryBuilder;
    }

    @Override
    public IRegistryBuilder<T> synced() {
        this.sync = true;
        return this;
    }

    @Override
    public IRegistryBuilder<T> optional() {
        return this;
    }

    @Override
    public Registry<T> build() {
        //? if neoforge {
        
        this.registryBuilder.sync(this.sync);
        Registry<T> registry = this.registryBuilder.create();
        this.event.register(registry);
        return registry;
        //?} else {
        /*if (!this.sync) {
            this.registryBuilder.disableSync();
        }
        Supplier<IForgeRegistry<T>> forgeRegistrySupplier = this.event.create(this.registryBuilder);
        return new ForgeRegistryWrapper<>(forgeRegistrySupplier);
        *///?}
    }
}
