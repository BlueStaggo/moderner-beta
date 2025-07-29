//? if >=1.21.9 {
/*package mod.bluestaggo.modernerbeta.client.debug;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.debug.text.*;
import mod.bluestaggo.modernerbeta.mixin.client.Accessorclass_11631;
import net.minecraft.class_11632;
import net.minecraft.util.Identifier;

@SuppressWarnings("unused")
public class ModernBetaDebugTexts {
    public static final Identifier EXTENDED_BIOME = register("extended_biome", new ExtendedBiomeDebugText());
    public static final Identifier CLIMATE = register("climate", new ClimateDebugText());
    public static final Identifier HEIGHTMAP = register("heightmap", new HeightmapDebugText());
    public static final Identifier FORCED_HEIGHT = register("forced_height", new ForcedHeightDebugText());
    public static final Identifier INJECTED_BIOME = register("injected_biome", new InjectedBiomeDebugText());

    private static Identifier register(String id, class_11632 text) {
        return Accessorclass_11631.invokeRegister(ModernerBeta.createId(id), text);
    }

    public static void register() {}
}
*///?}