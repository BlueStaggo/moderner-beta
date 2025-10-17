//? if forge {
/*package mod.bluestaggo.modernerbeta.forgelike.registry;

import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

public record ForgeRegistryHandler<V>(RegisterEvent event) implements IRegistryHandler<V> {
    @Override
    public <T extends V> @NotNull T register(ResourceLocation id, T value) {
        IForgeRegistry<T> forgeRegistry = event.getForgeRegistry();
        Registry<T> vanillaRegistry = event.getVanillaRegistry();

        if (forgeRegistry != null) {
            forgeRegistry.register(id, value);
            return value;
        } else if (vanillaRegistry != null) {
            return Registry.register(vanillaRegistry, id, value);
        }

        throw new RuntimeException("Event does not have a valid Registry or ForgeRegistry!");
    }
}
*///?}
