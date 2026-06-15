package mod.bluestaggo.modernerbeta.mixin;

import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(targets = "net.minecraft.world.level.levelgen.SurfaceRules$SequenceRuleSource")
public interface SequenceRuleSourceAccessor {
    @Accessor("sequence")
    List<SurfaceRules.RuleSource> sequence();
}
