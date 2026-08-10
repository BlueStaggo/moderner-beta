package mod.bluestaggo.modernerbeta.compat;

import com.google.common.collect.ImmutableList;
import mod.bluestaggo.modernerbeta.compat.levelgen.SurfaceRuleCompatHelper;
import mod.bluestaggo.modernerbeta.compat.levelgen.TerraBlenderCompat;
import mod.bluestaggo.modernerbeta.compat.levelgen.ValkyrienSkiesCompat;
import mod.bluestaggo.modernerbeta.compat.levelgen.LevelGenCompatHelper;
//? if >=26.2 {
/*import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;
*///? }
import net.minecraft.world.level.levelgen.SurfaceRules;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class ModCompat {
    public static Set<LevelGenCompatHelper> worldGenHelpers = new HashSet<>();
    public static Set<SurfaceRuleCompatHelper> surfaceRuleHelpers = new HashSet<>();
    private static boolean compatChecked;

    public static boolean useModernBetaSurfaceRules;

    public static void initialise(Function<String, Boolean> modPresenceFunction) {
        if (compatChecked)
            throw new IllegalStateException("Tried to initialise mod compatibility workarounds twice!");

        if (modPresenceFunction.apply("valkyrienskies"))
            worldGenHelpers.add(new ValkyrienSkiesCompat());

        if (modPresenceFunction.apply("terrablender"))
            surfaceRuleHelpers.add(new TerraBlenderCompat());

        compatChecked = true;
    }

    public static boolean skipGeneratingChunk(int x, int z) {
        if (worldGenHelpers.isEmpty())
            return false;

        for (LevelGenCompatHelper helper : worldGenHelpers) {
            if (helper.skipGeneratingChunk(x, z))
                return true;
        }

        return false;
    }

    public static List<SurfaceRules.RuleSource> getPreBedrockCustomRules(/*? >=26.2 {*/ /*HolderGetter<Biome> biomes *//*? }*/) {
        if (surfaceRuleHelpers.isEmpty())
            return List.of();

        ImmutableList.Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();
        for (SurfaceRuleCompatHelper helper : surfaceRuleHelpers) {
            builder.addAll(helper.getPreBedrockCustomRules(/*? >=26.2 {*/ /*biomes *//*? }*/));
        }

        return builder.build();
    }

    public static List<SurfaceRules.RuleSource> getPostBedrockCustomRules(/*? >=26.2 {*/ /*HolderGetter<Biome> biomes *//*? }*/) {
        if (surfaceRuleHelpers.isEmpty())
            return List.of();

        ImmutableList.Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();
        for (SurfaceRuleCompatHelper helper : surfaceRuleHelpers) {
            builder.addAll(helper.getPostBedrockCustomRules(/*? >=26.2 {*/ /*biomes *//*? }*/));
        }

        return builder.build();
    }
}
