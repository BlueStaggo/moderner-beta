package mod.bluestaggo.modernerbeta.compat.levelgen;

//? if >=26.2 {
/*import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;
*///? }
import net.minecraft.world.level.levelgen.SurfaceRules;

import java.util.List;

public interface SurfaceRuleCompatHelper {
    List<SurfaceRules.RuleSource> getPreBedrockCustomRules(/*? >=26.2 {*/ /*HolderGetter<Biome> biomes *//*? }*/);
    List<SurfaceRules.RuleSource> getPostBedrockCustomRules(/*? >=26.2 {*/ /*HolderGetter<Biome> biomes *//*? }*/);
}
