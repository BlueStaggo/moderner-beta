package mod.bluestaggo.modernerbeta.client;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.client.gui.screen.config.GraphicalConfigBuilder;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@SuppressWarnings("unchecked")
public class ModernBetaClientBuiltInProviders {
    public static void registerGraphicalConfigBuilders(IRegistryHandler<?> handler) {
        IRegistryHandler<GraphicalConfigBuilder> registryHandler = (IRegistryHandler<GraphicalConfigBuilder>) handler;

        registryHandler.register(
            ModernBetaBuiltInTypes.SettingsComponentType.DEEPSLATE_GENERATION.id,
            (screen, options) -> {
                int minY = screen.worldMinY;
                int maxY = screen.worldMaxY;

                options.addAll(
                    screen.booleanOption("enabled"),
                    screen.blockOption("block"),
                    screen.intRangeOption("minY", minY, maxY),
                    screen.intRangeOption("maxY", minY, maxY)
                );
            }
        );
    }
}
