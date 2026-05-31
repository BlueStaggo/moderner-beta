package mod.bluestaggo.modernerbeta.compat.levelgen;

import com.google.common.collect.ImmutableList;
import mod.bluestaggo.modernerbeta.util.LoggingUtil;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.slf4j.event.Level;

import java.lang.reflect.Method;
import java.util.List;

public class TerraBlenderCompat implements SurfaceRuleCompatHelper {
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public List<SurfaceRules.RuleSource> getCustomRules() {
        ImmutableList.Builder<SurfaceRules.RuleSource> rules = ImmutableList.builder();

        try {
            Class<?> ruleManager = Class.forName("terrablender.api.SurfaceRuleManager");
            Class<Enum> ruleCategory = (Class<Enum>) Class.forName("terrablender.api.SurfaceRuleManager$RuleCategory");
            Class<Enum> ruleStage = (Class<Enum>) Class.forName("terrablender.api.SurfaceRuleManager$RuleStage");

            Method getAdditions = ruleManager.getMethod("getDefaultSurfaceRuleAdditionsForStage",
                    ruleCategory, Class.forName("terrablender.api.SurfaceRuleManager$RuleStage"));
            Enum<?> overworld = Enum.valueOf(ruleCategory, "OVERWORLD");

            rules.addAll((Iterable<SurfaceRules.RuleSource>)
                    getAdditions.invoke(null, overworld, Enum.valueOf(ruleStage, "BEFORE_BEDROCK")));
            rules.addAll((Iterable<SurfaceRules.RuleSource>)
                    getAdditions.invoke(null, overworld, Enum.valueOf(ruleStage, "AFTER_BEDROCK")));
        } catch (Exception e) {
            LoggingUtil.log(Level.ERROR, "Failed to get custom TerraBlender rules to add!", e);
        }

        return rules.build();
    }
}
