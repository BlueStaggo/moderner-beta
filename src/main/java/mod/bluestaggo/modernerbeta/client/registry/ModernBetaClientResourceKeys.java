package mod.bluestaggo.modernerbeta.client.registry;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.gui.screen.config.GraphicalConfigBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

@Environment(EnvType.CLIENT)
public class ModernBetaClientResourceKeys {
    public static final ResourceKey<Registry<GraphicalConfigBuilder>> SETTINGS_COMPONENT_TYPE_GUI = of("settings_component_type_gui");

    private static <T> ResourceKey<Registry<T>> of(String id) {
        return ResourceKey.createRegistryKey(ModernerBeta.createId(id));
    }
}
