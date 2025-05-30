package mod.bluestaggo.modernerbeta.client;

import mod.bluestaggo.modernerbeta.client.registry.ModernBetaClientRegistries;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import net.minecraft.registry.Registry;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.SequencedMap;
import java.util.function.Consumer;

public class ModernerBetaClient {
    public static SequencedMap<Registry<?>, Consumer<IRegistryHandler<?>>> CUSTOM_REGISTRY_HANDLERS;

    public static void setupCustomRegistryHandlers() {
        SequencedMap<Registry<?>, Consumer<IRegistryHandler<?>>> customRegistryHandlers = new LinkedHashMap<>();
        customRegistryHandlers.put(ModernBetaClientRegistries.GRAPHICAL_CONFIG_BUILDER, ModernBetaClientBuiltInProviders::registerGraphicalConfigBuilders);
        CUSTOM_REGISTRY_HANDLERS = Collections.unmodifiableSequencedMap(customRegistryHandlers);
    }
}
