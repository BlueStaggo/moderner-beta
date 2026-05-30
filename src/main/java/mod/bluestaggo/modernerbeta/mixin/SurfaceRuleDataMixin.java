package mod.bluestaggo.modernerbeta.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomes;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static mod.bluestaggo.modernerbeta.level.chunk.ModernBetaNoiseGeneratorSettings.useModernBetaSurfaceRules;

@Mixin(SurfaceRuleData.class)
public class SurfaceRuleDataMixin {
    @ModifyExpressionValue(
        method = "overworldLike",
        at = @At(
            value = "CONSTANT",
            args = "intValue=97"
        )
    )
    private static int modifyWoodedBadlandsHeight(int constant) {
        if (useModernBetaSurfaceRules()) {
            return 86;
        }
        return constant;
    }

    @ModifyExpressionValue(
        method = "overworldLike",
        at = @At(
            value = "CONSTANT",
            args = "intValue=74"
        )
    )
    private static int modifyTerracottaStripesHeight(int constant) {
        if (useModernBetaSurfaceRules()) {
            return 66;
        }
        return constant;
    }

    @SuppressWarnings("rawtypes")
    @WrapOperation(method = "overworldLike",
        at = {
            @At(
                value = "INVOKE",
                target = "Lcom/google/common/collect/ImmutableList$Builder;add(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList$Builder;",
                ordinal = 0,
                remap = false
            ),
            @At(
                value = "INVOKE",
                target = "Lcom/google/common/collect/ImmutableList$Builder;add(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList$Builder;",
                ordinal = 1,
                remap = false
            ),
            @At(
                value = "INVOKE",
                target = "Lcom/google/common/collect/ImmutableList$Builder;add(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList$Builder;",
                ordinal = /*? >=26.2 {*/ /*4 *//*?} else {*/ 3 /*?}*/,
                remap = false
            )
        }
    )
    private static ImmutableList.Builder removeWorldBottomRulesIfModernBeta(ImmutableList.Builder instance, Object element, Operation<ImmutableList.Builder> original) {
        if (useModernBetaSurfaceRules()) {
            return null;
        }

        return original.call(instance, element);
    }

    @WrapOperation(
        method = "overworldLike",
        at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/level/levelgen/SurfaceRules;isBiome("
                        //? >=26.2
                        //+ "Lnet/minecraft/core/HolderGetter;"
                        + "[Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/world/level/levelgen/SurfaceRules$ConditionSource;"
        )
    )
    private static SurfaceRules.ConditionSource addModernBetaVariantsToRulePredicates(
        //? if >=26.2
        //net.minecraft.core.HolderGetter<Biome> biomes,
        ResourceKey<Biome>[] target,
        Operation<SurfaceRules.ConditionSource> original
    ) {
        if (!useModernBetaSurfaceRules())
            return original.call(/*? >=26.2 {*//*biomes, *//*?} else {*/(Object)/*?}*/ target);

        List<ResourceKey<Biome>> targets = new ArrayList<>(Arrays.asList(target));

        if (targets.contains(Biomes.FROZEN_OCEAN) || targets.contains(Biomes.DEEP_FROZEN_OCEAN)) {
            targets.add(ModernBetaBiomes.BETA_FROZEN_OCEAN);
        }

        if (targets.contains(Biomes.WARM_OCEAN)) {
            targets.add(ModernBetaBiomes.BETA_WARM_OCEAN);
        }

        if (targets.contains(Biomes.DESERT)) {
            targets.add(ModernBetaBiomes.BETA_DESERT);
            targets.add(ModernBetaBiomes.BETA_ICE_DESERT);
        }

        if (targets.contains(Biomes.LUKEWARM_OCEAN) || targets.contains(Biomes.DEEP_LUKEWARM_OCEAN)) {
            targets.add(ModernBetaBiomes.BETA_LUKEWARM_OCEAN);
        }

        return original.call(/*? >=26.2 {*//*biomes, *//*?} else {*/(Object)/*?}*/ targets.toArray(new ResourceKey[0]));
    }
}
