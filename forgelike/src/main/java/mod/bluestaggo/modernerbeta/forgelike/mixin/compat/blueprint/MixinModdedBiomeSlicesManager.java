package mod.bluestaggo.modernerbeta.forgelike.mixin.compat.blueprint;

import mod.bluestaggo.modernerbeta.forgelike.mixin.AccessorChunkGenerator;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
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
public abstract class MixinModdedBiomeSlicesManager {
    @Redirect(
        method = "onServerAboutToStart",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/gen/chunk/ChunkGenerator;biomeSource:Lnet/minecraft/world/biome/source/BiomeSource;",
            opcode = Opcodes.PUTFIELD
        )
    )
    private static void preventSettingBiomeSourceForModernBeta(ChunkGenerator chunkGenerator, BiomeSource biomeSource) {
        BiomeSource oldBiomeSource = chunkGenerator.getBiomeSource();

        if (!(oldBiomeSource instanceof ModernBetaBiomeSource)) {
            ((AccessorChunkGenerator) chunkGenerator).setBiomeSource(biomeSource);
        }
    }

    @Redirect(
        method = "onServerAboutToStart",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/gen/chunk/ChunkGenerator;indexedFeaturesListSupplier:Ljava/util/function/Supplier;",
            opcode = Opcodes.PUTFIELD
        )
    )
    private static void preventSettingIndexedFeatureSupplierForModernBeta(ChunkGenerator chunkGenerator, Supplier<List<FeatureSorter.StepFeatureData>> indexedFeatureSupplier) {
        BiomeSource oldBiomeSource = chunkGenerator.getBiomeSource();

        if (!(oldBiomeSource instanceof ModernBetaBiomeSource)) {
            ((AccessorChunkGenerator) chunkGenerator).setFeaturesPerStep(indexedFeatureSupplier);
        }
    }
}
