package mod.bluestaggo.modernerbeta.compat.levelgen;

import net.minecraft.world.level.levelgen.SurfaceRules;

import java.util.List;

public interface SurfaceRuleCompatHelper {
    List<SurfaceRules.RuleSource> getCustomRules();
}
