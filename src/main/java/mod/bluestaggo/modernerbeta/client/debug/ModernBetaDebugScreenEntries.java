//? if >=1.21.9 {
/*package mod.bluestaggo.modernerbeta.client.debug;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.debug.entries.*;
import mod.bluestaggo.modernerbeta.mixin.client.DebugScreenEntriesAccessor;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
public class ModernBetaDebugScreenEntries {
    public static final ResourceLocation EXTENDED_BIOME = register("extended_biome", new DebugEntryExtendedBiome());
    public static final ResourceLocation CLIMATE = register("climate", new DebugEntryClimate());
    public static final ResourceLocation HEIGHTMAP = register("heightmap", new DebugEntryHeightmap());
    public static final ResourceLocation FORCED_HEIGHT = register("forced_height", new DebugEntryForcedHeight());
    public static final ResourceLocation INJECTED_BIOME = register("injected_biome", new DebugEntryInjectedBiome());

    private static ResourceLocation register(String id, DebugScreenEntry text) {
        return DebugScreenEntriesAccessor.invokeRegister(ModernerBeta.createId(id), text);
    }

    public static void register() {}
}
*///?}
