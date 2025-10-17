package mod.bluestaggo.modernerbeta.forgelike.registry;

import mod.bluestaggo.modernerbeta.registry.IRegistryBuilder;
import mod.bluestaggo.modernerbeta.registry.IRegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
//? if neoforge {
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
//?} else {
/*import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
*///?}

public record RegistryHelperImpl(NewRegistryEvent event) implements IRegistryHelper {
    @Override
    public <T> IRegistryBuilder<T> createSimple(ResourceKey<Registry<T>> key) {
        RegistryBuilder<T> registryBuilder =
            //? if neoforge {
            new RegistryBuilder<>(key);
            //?} else {
            /*new RegistryBuilder<T>().setName(key.location());
            *///?}
        return new RegistryBuilderImpl<>(event, registryBuilder);
    }

    @Override
    public <T> IRegistryBuilder<T> createDefaulted(ResourceKey<Registry<T>> key, ResourceLocation defaultKey) {
        RegistryBuilder<T> registryBuilder =
            //? if neoforge {
            new RegistryBuilder<>(key).defaultKey(defaultKey);
             //?} else {
            /*new RegistryBuilder<T>().setName(key.location()).setDefaultKey(defaultKey);
            *///?}
        return new RegistryBuilderImpl<>(event, registryBuilder);
    }
}
