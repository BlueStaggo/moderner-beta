package mod.bluestaggo.modernerbeta.client.registry;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.gui.screen.config.GraphicalConfigBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

@Environment(EnvType.CLIENT)
public class ModernBetaClientRegistryKeys {
    public static final RegistryKey<Registry<GraphicalConfigBuilder>> GRAPHICAL_CONFIG_BUILDER = of("graphical_config_builder");

    private static <T> RegistryKey<Registry<T>> of(String id) {
        return RegistryKey.ofRegistry(ModernerBeta.createId(id));
    }
}
