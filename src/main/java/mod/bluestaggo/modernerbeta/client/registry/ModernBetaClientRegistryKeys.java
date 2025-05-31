package mod.bluestaggo.modernerbeta.client.registry;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.gui.screen.config.GraphicalConfigBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

@Environment(EnvType.CLIENT)
public class ModernBetaClientRegistryKeys {
    public static final RegistryKey<Registry<GraphicalConfigBuilder>> SETTINGS_COMPONENT_TYPE_GUI = of("settings_component_type_gui");

    private static <T> RegistryKey<Registry<T>> of(String id) {
        return RegistryKey.ofRegistry(ModernerBeta.createId(id));
    }
}
