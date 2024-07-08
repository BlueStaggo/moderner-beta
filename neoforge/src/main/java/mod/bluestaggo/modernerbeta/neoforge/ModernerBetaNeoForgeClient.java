package mod.bluestaggo.modernerbeta.neoforge;

import me.shedaniel.autoconfig.AutoConfig;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.config.ModernBetaConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(modid = ModernerBeta.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModernerBetaNeoForgeClient {
    @SubscribeEvent
    public static void clientInit(FMLConstructModEvent event) {
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class, () -> (mc, screen) -> AutoConfig.getConfigScreen(ModernBetaConfig.class, screen).get());
    }
}
