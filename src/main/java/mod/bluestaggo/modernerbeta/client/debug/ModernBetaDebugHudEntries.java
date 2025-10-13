//? if >=1.21.9 {
/*package mod.bluestaggo.modernerbeta.client.debug;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.debug.hudentry.*;
import mod.bluestaggo.modernerbeta.mixin.client.AccessorDebugHudEntries;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class ModernBetaDebugHudEntries {
    public static final ResourceLocation EXTENDED_BIOME = register("extended_biome", new ExtendedBiomeDebugHudEntry());
    public static final ResourceLocation CLIMATE = register("climate", new ClimateDebugHudEntry());
    public static final ResourceLocation HEIGHTMAP = register("heightmap", new HeightmapDebugHudEntry());
    public static final ResourceLocation FORCED_HEIGHT = register("forced_height", new ForcedHeightDebugHudEntry());
    public static final ResourceLocation INJECTED_BIOME = register("injected_biome", new InjectedBiomeDebugHudEntry());

    private static ResourceLocation register(String id, DebugScreenEntry text) {
        return AccessorDebugHudEntries.invokeRegister(ModernerBeta.createId(id), text);
    }

    public static void register() {}
}
*///?}
