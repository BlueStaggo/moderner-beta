package mod.bluestaggo.modernerbeta.registry;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ConfiguredLayers;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class ModernBetaRegistryKeys {
    public static final RegistryKey<Registry<ConfiguredLayers>> CONFIGURED_LAYERS_KEY = RegistryKey.ofRegistry(ModernerBeta.createId("configured_layers"));
}
