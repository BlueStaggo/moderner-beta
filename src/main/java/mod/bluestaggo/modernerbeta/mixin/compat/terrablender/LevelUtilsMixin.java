//? if <26.1 {
package mod.bluestaggo.modernerbeta.mixin.compat.terrablender;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;

@SuppressWarnings("UnresolvedMixinReference")
@Pseudo
@Mixin(targets = "terrablender.util.LevelUtils")
public class LevelUtilsMixin {
    //This is a unique case of an upstream issue that has to be patched within Moderner Beta
    //  as upstream no longer supports versions older than 26.1
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Inject(method = "initializeBiomes", at = @At(value = "RETURN", ordinal = /*? >=1.20.4 {*/ 2 /*? } else {*/ /*0 *//*? }*/))
    private static void fixUpOopsie(
        RegistryAccess registryAccess,
        Holder<DimensionType> dimensionType,
        ResourceKey<LevelStem> levelResourceKey,
        ChunkGenerator chunkGenerator,
        long seed,
        CallbackInfo ci,
        @Local NoiseGeneratorSettings generatorSettings
    ) {
        try {
            //Annoying that I cannot shadow this method.
            Method regionTypeMeth = Class.forName("terrablender.util.LevelUtils")
                    .getMethod("getRegionTypeForDimension", Holder.class);
            Enum<?> regionType = (Enum<?>) regionTypeMeth.invoke(null, dimensionType);

            if (regionType == null)
                return;

            Class<?> extendedSettings = Class.forName("terrablender.worldgen.IExtendedNoiseGeneratorSettings");
            //? if >=1.20.4 {
            Class<Enum> ruleCategoryClass = (Class<Enum>) Class.forName("terrablender.api.SurfaceRuleManager$RuleCategory");
            Enum<?> ruleCategory = Enum.valueOf(ruleCategoryClass, regionType.name());

            Method meth = extendedSettings.getMethod("setRuleCategory", ruleCategoryClass);
            meth.invoke(generatorSettings, ruleCategory);
            //? } else {
            /*Class<?> regionTypeClass = Class.forName("terrablender.api.RegionType");

            Method meth = extendedSettings.getMethod("setRegionType", regionTypeClass);
            meth.invoke(generatorSettings, regionType);
            *///? }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
//? }