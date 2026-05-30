package mod.bluestaggo.modernerbeta.client;

import com.mojang.datafixers.util.Pair;
import mod.bluestaggo.modernerbeta.client.registry.ModernBetaClientRegistries;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import net.minecraft.core.Registry;
import java.util.List;
import java.util.function.Consumer;

public class ModernerBetaClient {
    public static List<Pair<Registry<?>, Consumer<IRegistryHandler<?>>>> CUSTOM_REGISTRY_HANDLERS;

    public static void init() {
        //? if >=1.21.9
        //mod.bluestaggo.modernerbeta.client.debug.ModernBetaDebugScreenEntries.register();
    }

    public static void setupCustomRegistryHandlers() {
        CUSTOM_REGISTRY_HANDLERS = List.of(
            new Pair<>(ModernBetaClientRegistries.SETTINGS_COMPONENT_TYPE_GUI, ModernBetaClientBuiltInProviders::registerSettingsComponentTypeGuis)
        );
    }
}
