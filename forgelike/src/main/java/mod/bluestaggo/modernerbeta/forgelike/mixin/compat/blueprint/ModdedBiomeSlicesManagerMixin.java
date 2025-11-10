package mod.bluestaggo.modernerbeta.forgelike.mixin.compat.blueprint;

import mod.bluestaggo.modernerbeta.forgelike.mixin.ChunkGeneratorAccessor;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.FeatureSorter;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("UnresolvedMixinReference")
@Pseudo
@Mixin(targets = "com.teamabnormals.blueprint.common.world.modification.ModdedBiomeSlicesManager")
public abstract class ModdedBiomeSlicesManagerMixin {
    @Redirect(
        method = "onServerAboutToStart",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/level/chunk/ChunkGenerator;biomeSource:Lnet/minecraft/world/level/biome/BiomeSource;",
            opcode = Opcodes.PUTFIELD
        )
    )
    private static void preventSettingBiomeSourceForModernBeta(ChunkGenerator chunkGenerator, BiomeSource biomeSource) {
        BiomeSource oldBiomeSource = chunkGenerator.getBiomeSource();

        if (!(oldBiomeSource instanceof ModernBetaBiomeSource)) {
            ((ChunkGeneratorAccessor) chunkGenerator).setBiomeSource(biomeSource);
        }
    }

    @Redirect(
        method = "onServerAboutToStart",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/level/chunk/ChunkGenerator;featuresPerStep:Ljava/util/function/Supplier;",
            opcode = Opcodes.PUTFIELD
        )
    )
    private static void preventSettingFeaturesPerStepForModernBeta(ChunkGenerator chunkGenerator, Supplier<List<FeatureSorter.StepFeatureData>> indexedFeatureSupplier) {
        BiomeSource oldBiomeSource = chunkGenerator.getBiomeSource();

        if (!(oldBiomeSource instanceof ModernBetaBiomeSource)) {
            ((ChunkGeneratorAccessor) chunkGenerator).setFeaturesPerStep(indexedFeatureSupplier);
        }
    }
}
