package mod.bluestaggo.modernerbeta.client.registry;

import mod.bluestaggo.modernerbeta.client.gui.screen.config.graphical.GraphicalConfigBuilder;
import mod.bluestaggo.modernerbeta.registry.IRegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class ModernBetaClientRegistries {
    private static IRegistryHelper registryHelper;

    public static Registry<GraphicalConfigBuilder> SETTINGS_COMPONENT_TYPE_GUI;

    private static <T> Registry<T> register(ResourceKey<Registry<T>> key) {
        return registryHelper.createSimple(key).build();
    }

    public static void makeRegistries(IRegistryHelper helper) {
        registryHelper = helper;

        SETTINGS_COMPONENT_TYPE_GUI = register(ModernBetaClientResourceKeys.SETTINGS_COMPONENT_TYPE_GUI);
    }
}
