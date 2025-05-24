package mod.bluestaggo.modernerbeta.neoforge;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.command.DebugProviderSettingsCommand;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.registry.VanillaRegistryHandler;
import mod.bluestaggo.modernerbeta.world.ModernBetaWorldInitializer;
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
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.Consumer;

@EventBusSubscriber(modid = ModernerBeta.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModernerBetaNeoForge {
    //TODO: maybe move these to another class?
    static {
        NeoForge.EVENT_BUS.addListener(ModernerBetaNeoForge::registerCommands);
        NeoForge.EVENT_BUS.addListener(ModernerBetaNeoForge::serverStarting);
    }

    @SubscribeEvent
    public static void commonInit(FMLConstructModEvent event) {
        ModernerBeta.init();

        if (!FMLLoader.isProduction())
            ModernerBeta.DEV_ENV = true;
    }

    private static final Consumer<IRegistryHandler<?>> NONE = h -> {};

    @SubscribeEvent
    public static void registerToRegistries(RegisterEvent event) {
        VanillaRegistryHandler<?> registryHandler = new VanillaRegistryHandler<>(event.getRegistry());
        ModernerBeta.REGISTRY_HANDLERS.getOrDefault(event.getRegistry(), NONE).accept(registryHandler);
    }

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        for (Pair<RegistryKey<?>, Codec<?>> dynamicRegistry : ModernerBeta.DYNAMIC_REGISTRIES) {
            RegistryKey<Registry<Object>> registryKey = (RegistryKey<Registry<Object>>)dynamicRegistry.getLeft();
            Codec<Object> codec = (Codec<Object>)dynamicRegistry.getLeft();
            event.dataPackRegistry(registryKey, codec, codec);
        }
    }

    public static void registerCommands(RegisterCommandsEvent event) {
        if (FMLLoader.isProduction()) return;

        DebugProviderSettingsCommand.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }

    public static void serverStarting(ServerAboutToStartEvent event) {
        ModernBetaWorldInitializer.init(event.getServer());
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
