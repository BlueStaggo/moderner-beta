//? if >=1.21.9 {
/*package mod.bluestaggo.modernerbeta.client.debug;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.debug.hudentry.*;
import mod.bluestaggo.modernerbeta.mixin.client.AccessorDebugHudEntries;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.debug.DebugHudEntry;
import net.minecraft.util.Identifier;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class ModernBetaDebugHudEntries {
    public static final Identifier EXTENDED_BIOME = register("extended_biome", new ExtendedBiomeDebugHudEntry());
    public static final Identifier CLIMATE = register("climate", new ClimateDebugHudEntry());
    public static final Identifier HEIGHTMAP = register("heightmap", new HeightmapDebugHudEntry());
    public static final Identifier FORCED_HEIGHT = register("forced_height", new ForcedHeightDebugHudEntry());
    public static final Identifier INJECTED_BIOME = register("injected_biome", new InjectedBiomeDebugHudEntry());

    private static Identifier register(String id, DebugHudEntry text) {
        return AccessorDebugHudEntries.invokeRegister(ModernerBeta.createId(id), text);
    }

    public static void register() {}
}
*///?}