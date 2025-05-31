package mod.bluestaggo.modernerbeta.client.registry;

import mod.bluestaggo.modernerbeta.client.gui.screen.config.GraphicalConfigBuilder;
import mod.bluestaggo.modernerbeta.registry.IRegistryHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

@Environment(EnvType.CLIENT)
public final class ModernBetaClientRegistries {
    private static IRegistryHelper registryHelper;

    public static Registry<GraphicalConfigBuilder> SETTINGS_COMPONENT_TYPE_GUI;

    private static <T> Registry<T> register(RegistryKey<Registry<T>> key) {
        return registryHelper.createSimple(key).build();
    }

    public static void makeRegistries(IRegistryHelper helper) {
        registryHelper = helper;

        SETTINGS_COMPONENT_TYPE_GUI = register(ModernBetaClientRegistryKeys.SETTINGS_COMPONENT_TYPE_GUI);
    }
}
