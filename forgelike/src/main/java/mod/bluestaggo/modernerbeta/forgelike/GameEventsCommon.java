package mod.bluestaggo.modernerbeta.forgelike;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.command.DebugProviderSettingsCommand;
import mod.bluestaggo.modernerbeta.level.ModernBetaLevelInitializer;
//? if neoforge {
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
//?} else {
/*import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
*///?}

//? if neoforge {
@EventBusSubscriber(
//?} else {
/*@Mod.EventBusSubscriber(
*///?}
        modid = ModernerBeta.MOD_ID
        //? if neoforge && <1.21.6 {
        /*, bus = EventBusSubscriber.Bus.GAME
        *///?} else if forge {
        /*, bus = Mod.EventBusSubscriber.Bus.FORGE
        *///?}
)
public class GameEventsCommon {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        if (FMLLoader/*? >=1.21.9 {*/.getCurrent()/*?}*/.isProduction()) return;

        DebugProviderSettingsCommand.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }

    @SubscribeEvent
    public static void serverStarting(ServerAboutToStartEvent event) {
        ModernBetaLevelInitializer.initStarting(event.getServer());
    }

    @SubscribeEvent
    public static void serverStarting(ServerStartedEvent event) {
        ModernBetaLevelInitializer.initStarted(event.getServer());
    }

    @SubscribeEvent
    public static void datapackSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() == null) {
            ModernBetaLevelInitializer.clearBiomeInjectionCaches(event.getPlayerList().getServer());
        }
    }
}
