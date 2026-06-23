//? if <26.3 {
package mod.bluestaggo.modernerbeta.compat.levelgen;

import mod.bluestaggo.modernerbeta.util.LoggingUtil;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.slf4j.event.Level;

import java.lang.reflect.Method;
import java.util.List;

@SuppressWarnings("rawtypes")
public class TerraBlenderCompat implements SurfaceRuleCompatHelper {
    private final Class<Enum> ruleStage;

    private final Method getAdditions;
    private final Enum<?> overworld;

    @SuppressWarnings("unchecked")
    public TerraBlenderCompat() {
        try {
            Class<?> ruleManager = Class.forName("terrablender.api.SurfaceRuleManager");
            Class<Enum> ruleCategory = (Class<Enum>) Class.forName("terrablender.api.SurfaceRuleManager$RuleCategory");
            ruleStage = (Class<Enum>) Class.forName("terrablender.api.SurfaceRuleManager$RuleStage");

            getAdditions = ruleManager.getMethod("getDefaultSurfaceRuleAdditionsForStage",
                    ruleCategory, Class.forName("terrablender.api.SurfaceRuleManager$RuleStage"));
            overworld = Enum.valueOf(ruleCategory, "OVERWORLD");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialise TerraBlender compatibility!", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SurfaceRules.RuleSource> getPreBedrockCustomRules() {
        try {
            return (List<SurfaceRules.RuleSource>)
                    getAdditions.invoke(null, overworld, Enum.valueOf(ruleStage, "BEFORE_BEDROCK"));
        } catch (Exception e) {
            LoggingUtil.log(Level.ERROR, "Failed to get custom TerraBlender rules to add!", e);
        }

        return List.of();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SurfaceRules.RuleSource> getPostBedrockCustomRules() {
        try {
            return (List<SurfaceRules.RuleSource>)
                    getAdditions.invoke(null, overworld, Enum.valueOf(ruleStage, "AFTER_BEDROCK"));
        } catch (Exception e) {
            LoggingUtil.log(Level.ERROR, "Failed to get custom TerraBlender rules to add!", e);
        }

        return List.of();
    }
}
//? }