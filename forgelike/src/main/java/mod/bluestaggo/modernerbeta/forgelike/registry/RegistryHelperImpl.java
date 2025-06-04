package mod.bluestaggo.modernerbeta.forgelike.registry;

import mod.bluestaggo.modernerbeta.registry.IRegistryBuilder;
import mod.bluestaggo.modernerbeta.registry.IRegistryHelper;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
//? if neoforge {
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
//?} else {
/*import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
*///?}

public record RegistryHelperImpl(NewRegistryEvent event) implements IRegistryHelper {
    @Override
    public <T> IRegistryBuilder<T> createSimple(RegistryKey<Registry<T>> key) {
        RegistryBuilder<T> registryBuilder = new RegistryBuilder<>(key);
        return new RegistryBuilderImpl<>(event, registryBuilder);
    }

    @Override
    public <T> IRegistryBuilder<T> createDefaulted(RegistryKey<Registry<T>> key, Identifier defaultKey) {
        RegistryBuilder<T> registryBuilder = new RegistryBuilder<>(key).defaultKey(defaultKey);
        return new RegistryBuilderImpl<>(event, registryBuilder);
    }
}
