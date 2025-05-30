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

    public static Registry<GraphicalConfigBuilder> GRAPHICAL_CONFIG_BUILDER;

    private static <T> Registry<T> register(RegistryKey<Registry<T>> key) {
        return registryHelper.createSimple(key).build();
    }

    public static void makeRegistries(IRegistryHelper helper) {
        registryHelper = helper;

        GRAPHICAL_CONFIG_BUILDER = register(ModernBetaClientRegistryKeys.GRAPHICAL_CONFIG_BUILDER);
    }
}
