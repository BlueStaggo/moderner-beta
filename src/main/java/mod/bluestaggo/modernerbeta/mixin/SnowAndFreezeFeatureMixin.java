//~registryOr
package mod.bluestaggo.modernerbeta.mixin;

import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bluestaggo.modernerbeta.world.chunk.provider.ChunkProviderEarlyRelease;
import mod.bluestaggo.modernerbeta.world.chunk.provider.ChunkProviderMajorRelease;
import mod.bluestaggo.modernerbeta.world.feature.BetaSnowAndFreezeFeature;
import mod.bluestaggo.modernerbeta.world.feature.placed.ModernBetaMiscPlacedFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.SnowAndFreezeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/*
 * Mixin is unfortunately needed since vanilla biomes mixed with Beta/PE biomes may confuse feature decorator,
 * and cause some chunks to not properly generate Beta/PE climate-influenced snow/ice layer.
 * 
 * Beta biomes + vanilla cave biomes is a situation where this would commonly occur.
 * 
 * TODO: Revisit this in a little to see if still necessary.
 * 
 * To Test --
 * Version: 6.0+1.19.4
 * World Preset: Beta Large Biomes
 * Seed: 8252128008552916748
 * Coord: -2770 139 -3404
 * 
 */
@Mixin(SnowAndFreezeFeature.class)
public abstract class SnowAndFreezeFeatureMixin {
    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void injectPlace(FeaturePlaceContext<NoneFeatureConfiguration> context, CallbackInfoReturnable<Boolean> info) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        
        ChunkGenerator chunkGenerator = context.chunkGenerator();
        BiomeSource biomeSource = chunkGenerator.getBiomeSource();

        if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator
                && (modernBetaChunkGenerator.getChunkProvider() instanceof ChunkProviderEarlyRelease
                        || modernBetaChunkGenerator.getChunkProvider() instanceof ChunkProviderMajorRelease)) {
            BetaSnowAndFreezeFeature.setFreezeTopLayer(level, pos, biomeSource, modernBetaChunkGenerator.allowSurfaceRules());
            info.setReturnValue(true);
            return;
        }
        
        boolean hasClimateSampler =
            biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource &&
            modernBetaBiomeSource.getBiomeProvider() instanceof ClimateSampler;
        
        if (hasClimateSampler) {
            int x = pos.getX();
            int z = pos.getZ();
            int y = context.level().getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z);
            
            BlockPos topPos = new BlockPos(x, y, z);
            Holder<Biome> topBiome = context.level().getBiome(topPos);
            
            Reference<PlacedFeature> betaFreezeTopLayer = context.level()
                .registryAccess()
                .lookupOrThrow(Registries.PLACED_FEATURE)
                //? if >=1.21.2 {
                .get
                //? } else {
                /*.getHolder
                *///? }
                    (ModernBetaMiscPlacedFeatures.FREEZE_TOP_LAYER)
                .orElse(null);

            boolean hasBetaFreezeTopLayer = topBiome.value()
                .getGenerationSettings()
                .features()
                .stream()
                .anyMatch(list -> list.contains(betaFreezeTopLayer));
            
            if (hasBetaFreezeTopLayer) {
                BetaSnowAndFreezeFeature.setFreezeTopLayer(level, pos, biomeSource, false);
                info.setReturnValue(true);
            }
        }
    }
}
