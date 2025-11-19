package mod.bluestaggo.modernerbeta.compat.client;

import mod.bluestaggo.modernerbeta.compat.client.world.ColorModifierHelper;
import mod.bluestaggo.modernerbeta.compat.client.world.SereneSeasonsCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class ModCompatClient {
    public static Set<ColorModifierHelper> colorModifierHelpers = new HashSet<>();
    private static boolean compatChecked;

    public static void initialise(Function<String, Boolean> modPresenceFunction) {
        if (compatChecked)
            throw new IllegalStateException("Tried to initialise client-side mod compatibility workarounds twice!");

        if (modPresenceFunction.apply("sereneseasons"))
            colorModifierHelpers.add(new SereneSeasonsCompat());

        compatChecked = true;
    }

    public static int modifyGrassColor(int original, Holder<Biome> biome, BlockPos pos) {
        if (colorModifierHelpers.isEmpty())
            return original;

        int modified = original;
        for (ColorModifierHelper helper : colorModifierHelpers) {
            modified = helper.modifyGrass(modified, biome, pos);
        }

        return modified;
    }

    public static int modifyFoliageColor(int original, Holder<Biome> biome, BlockPos pos) {
        if (colorModifierHelpers.isEmpty())
            return original;

        int modified = original;
        for (ColorModifierHelper helper : colorModifierHelpers) {
            modified = helper.modifyFoliage(modified, biome, pos);
        }

        return modified;
    }
}
