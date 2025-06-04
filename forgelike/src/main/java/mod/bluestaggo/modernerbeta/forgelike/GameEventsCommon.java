package mod.bluestaggo.modernerbeta.forgelike;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.command.DebugProviderSettingsCommand;
import mod.bluestaggo.modernerbeta.world.ModernBetaWorldInitializer;
//? if neoforge {
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
//?} else {
/*import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
*///?}

//? if neoforge {
@EventBusSubscriber(
//?} else {
/*@Mod.EventBusSubscriber(
*///?}
        modid = ModernerBeta.MOD_ID,
        //? if neoforge {
        bus = EventBusSubscriber.Bus.GAME
        //?} else {
        /*bus = Mod.EventBusSubscriber.Bus.FORGE
        *///?}
)
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
