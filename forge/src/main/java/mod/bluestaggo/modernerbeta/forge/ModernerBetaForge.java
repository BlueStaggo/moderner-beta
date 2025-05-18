package mod.bluestaggo.modernerbeta.forge;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.command.DebugProviderSettingsCommand;
import mod.bluestaggo.modernerbeta.world.ModernBetaWorldInitializer;
import net.minecraft.resource.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.RegisterEvent;

import java.nio.file.Path;

@Mod(ModernerBeta.MOD_ID)
@Mod.EventBusSubscriber(modid = ModernerBeta.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModernerBetaForge {
    //TODO: maybe move these to another class?
    static {
        MinecraftForge.EVENT_BUS.addListener(ModernerBetaForge::registerCommands);
        MinecraftForge.EVENT_BUS.addListener(ModernerBetaForge::serverStarting);
    }

    @SubscribeEvent
    public static void commonInit(FMLConstructModEvent event) {
        ModernerBeta.init();

        if (!FMLLoader.isProduction())
            ModernerBeta.DEV_ENV = true;
    }

    private static final Runnable NONE = () -> {};
    @SubscribeEvent
    public static void registerToRegistries(RegisterEvent event) {
        ModernerBeta.REGISTRY_HANDLERS.getOrDefault(event.getVanillaRegistry(), NONE).run();
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
        if (event.getPackType() == ResourceType.SERVER_DATA) {
            Path resourcePath = ModList.get().getModFileById(ModernerBeta.MOD_ID).getFile().findResource("resourcepacks/reduced_height");
            ResourcePackProfile pack = ResourcePackProfile.create(
                    "moderner_beta/reduced_height",
                    Text.of("Reduced Height"), //TODO: i18n perhaps?
                    false,
                    path -> new DirectoryResourcePack(path, resourcePath, false),
                    ResourceType.SERVER_DATA,
                    ResourcePackProfile.InsertionPosition.TOP,
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
                    }
            );
            event.addRepositorySource(consumer -> consumer.accept(pack));
        }
    }
}
