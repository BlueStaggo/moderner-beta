package mod.bluestaggo.modernerbeta.neoforge;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.neoforge.network.NetworkHelperImpl;
import mod.bluestaggo.modernerbeta.neoforge.registry.RegistryHelperImpl;
import mod.bluestaggo.modernerbeta.network.BiomeProviderInfoPayload;
import mod.bluestaggo.modernerbeta.network.S2CPacketHandlers;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.registry.IRegistryHelper;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.registry.VanillaRegistryHandler;
import mod.bluestaggo.modernerbeta.world.ModernBetaWorldInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.Consumer;

@EventBusSubscriber(modid = ModernerBeta.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventsCommon {
    @SubscribeEvent
    public static void commonInit(FMLConstructModEvent event) {
        ModernerBeta.init();

        if (!FMLLoader.isProduction())
            ModernerBeta.DEV_ENV = true;
    }

    private static final Consumer<IRegistryHandler<?>> NONE = h -> {};

    @SubscribeEvent
    public static void registerToRegistries(RegisterEvent event) {
        Registry<?> registry = event.getRegistry();

        VanillaRegistryHandler<?> registryHandler = new VanillaRegistryHandler<>(registry);
        ModernerBeta.REGISTRY_HANDLERS.getOrDefault(registry, NONE).accept(registryHandler);
        ModernerBeta.CUSTOM_REGISTRY_HANDLERS.getOrDefault(registry, NONE).accept(registryHandler);
    }

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        IRegistryHelper registryHelper = new RegistryHelperImpl(event);
        ModernBetaRegistries.makeRegistries(registryHelper);
        ModernerBeta.setupCustomRegistryHandlers();
    }

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        for (Pair<RegistryKey<?>, Codec<?>> dynamicRegistry : ModernerBeta.DYNAMIC_REGISTRIES) {
            RegistryKey<Registry<Object>> registryKey = (RegistryKey<Registry<Object>>)dynamicRegistry.getLeft();
            Codec<Object> codec = (Codec<Object>)dynamicRegistry.getRight();
            event.dataPackRegistry(registryKey, codec, codec);
        }
    }

    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        ModernerBeta.networkHelper = new NetworkHelperImpl();

        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                BiomeProviderInfoPayload.ID,
                BiomeProviderInfoPayload.CODEC,
                (payload, context) -> {
                    S2CPacketHandlers.onBiomeProviderInfo(context.player().getWorld(), payload);
                }
        );
    }

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == ResourceType.SERVER_DATA)
            event.addPackFinders(
                    ModernerBeta.createId("resourcepacks/reduced_height"),
                    ResourceType.SERVER_DATA,
                    Text.of("Reduced Height"), //TODO: i18n perhaps?
                    new ResourcePackSource() {
                        @Override
                        public Text decorate(Text packDisplayName) {
                            return Text.translatable("pack.nameAndSource", packDisplayName,
                                    Text.translatable("pack.source.builtin")).formatted(Formatting.GRAY);
                        }

                        @Override
                        public boolean canBeEnabledLater() {
                            return false;
                        }
                    },
                    false,
                    ResourcePackProfile.InsertionPosition.TOP
            );
    }
}
