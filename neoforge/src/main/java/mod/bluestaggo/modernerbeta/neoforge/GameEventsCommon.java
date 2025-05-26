package mod.bluestaggo.modernerbeta.neoforge;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.command.DebugProviderSettingsCommand;
import mod.bluestaggo.modernerbeta.world.ModernBetaWorldInitializer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

@EventBusSubscriber(modid = ModernerBeta.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class GameEventsCommon {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        if (FMLLoader.isProduction()) return;

        DebugProviderSettingsCommand.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }

    @SubscribeEvent
    public static void serverStarting(ServerAboutToStartEvent event) {
        ModernBetaWorldInitializer.init(event.getServer());
    }
}
