package mod.bluestaggo.modernerbeta.forgelike.registry;

import mod.bluestaggo.modernerbeta.registry.IRegistryBuilder;
import net.minecraft.registry.Registry;
//? if neoforge {
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
//?} else {
/*import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
*///?}

public class RegistryBuilderImpl<T> implements IRegistryBuilder<T> {
    private final NewRegistryEvent event;
    private final RegistryBuilder<T> registryBuilder;

    public RegistryBuilderImpl(NewRegistryEvent event, RegistryBuilder<T> registryBuilder) {
        this.event = event;
        this.registryBuilder = registryBuilder;
    }

    @Override
    public IRegistryBuilder<T> synced() {
        registryBuilder.sync(true);
        return this;
    }

    @Override
    public IRegistryBuilder<T> optional() {
        return this;
    }

    @Override
    public Registry<T> build() {
        Registry<T> registry = registryBuilder.create();
        event.register(registry);
        return registry;
    }
}
