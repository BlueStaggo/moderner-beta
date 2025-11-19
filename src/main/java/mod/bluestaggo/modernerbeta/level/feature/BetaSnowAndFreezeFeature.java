//~minBuild
package mod.bluestaggo.modernerbeta.level.feature;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.TemperatureHeightScaling;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.mixin.BiomeAccessor;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class BetaSnowAndFreezeFeature extends Feature<NoneFeatureConfiguration> {
    public BetaSnowAndFreezeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        
        ChunkGenerator chunkGenerator = context.chunkGenerator();
        BiomeSource biomeSource = chunkGenerator.getBiomeSource();
        
        setFreezeTopLayer(level, pos, biomeSource, false);
        return true;
    }

    public static void setFreezeTopLayer(WorldGenLevel level, BlockPos pos, BiomeSource biomeSource, boolean modernHeightSnow) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos mutableDown = new BlockPos.MutableBlockPos();
        
        for (int localX = 0; localX < 16; ++localX) {
            for (int localZ = 0; localZ < 16; ++localZ) {
                int x = pos.getX() + localX;
                int z = pos.getZ() + localZ;
                int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
                
                mutable.set(x, y, z);
                mutableDown.set(mutable).move(Direction.DOWN, 1);
                
                TemperatureHeightScaling heightType = TemperatureHeightScaling.NONE;
                double temp = level.getBiome(mutable).value().getBaseTemperature();
                double coldThreshold = 0.15;

                Biome.TemperatureModifier temperatureModifier = ((BiomeAccessor)(Object)level.getBiome(mutable).value()).getClimateSettings().temperatureModifier();
                if (biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
                    heightType = modernBetaBiomeSource.getBiomeProvider().getTemperatureHeightScaling();
                    if (modernBetaBiomeSource.getBiomeProvider() instanceof ClimateSampler climateSampler
                        && climateSampler.useBiomeFeature()) {
                        temp = climateSampler.sampleModifiedTemperature(mutable, temperatureModifier);
                        coldThreshold = climateSampler.getSnowThreshold();
                    } else if (heightType.supportsModifier(temperatureModifier)) {
                        temp = temperatureModifier.modifyTemperature(mutable, (float)temp);
                    }
                }

                if (modernHeightSnow) {
                    heightType = TemperatureHeightScaling.MAJOR_RELEASE;
                }
                
                if (canSetIce(level, mutableDown, false, temp, coldThreshold, heightType)) {
                    level.setBlock(mutableDown, Blocks.ICE.defaultBlockState(), Block.UPDATE_CLIENTS);
                }

                if (canSetSnow(level, mutable, temp, coldThreshold, heightType)) {
                    level.setBlock(mutable, Blocks.SNOW.defaultBlockState(), Block.UPDATE_CLIENTS);

                    BlockState blockState = level.getBlockState(mutableDown);
                    if (blockState.hasProperty(SnowyDirtBlock.SNOWY)) {
                        level.setBlock(mutableDown, blockState.setValue(SnowyDirtBlock.SNOWY, true), Block.UPDATE_CLIENTS);
                    }
                }
            }
        }
    }

    public static boolean canSetIce(
        LevelReader level,
        BlockPos blockPos,
        boolean doWaterCheck,
        double temp,
        double coldThreshold,
        TemperatureHeightScaling heightType
    ) {
        if (heightType.modifyTemperature(blockPos, temp) >= coldThreshold) {
            return false;
        }
        
        if (blockPos.getY() >= level.getMinY() &&
            blockPos.getY() < VersionCompat.getTopYExclusive(level) &&
            level.getBrightness(LightLayer.BLOCK, blockPos) < 10
        ) {
            BlockState blockState = level.getBlockState(blockPos);
            FluidState fluidState = level.getFluidState(blockPos);

            if (fluidState.getType() == Fluids.WATER && blockState.getBlock() instanceof LiquidBlock) {
                if (!doWaterCheck) {
                    return true;
                }

                boolean submerged = 
                    level.isWaterAt(blockPos.west()) &&
                    level.isWaterAt(blockPos.east()) &&
                    level.isWaterAt(blockPos.north()) &&
                    level.isWaterAt(blockPos.south());
                
                if (!submerged) {
                    return true;
                }
            }
        }
        
        return false;
    }

    public static boolean canSetSnow(LevelReader level, BlockPos blockPos, double temp, double coldThreshold, TemperatureHeightScaling heightType) {
        if (heightType.modifyTemperature(blockPos, temp) >= coldThreshold) {
            return false;
        }
        
        if (blockPos.getY() >= 0 && blockPos.getY() < 256 && level.getBrightness(LightLayer.BLOCK, blockPos) < 10) {
            BlockState blockState = level.getBlockState(blockPos);
            
            if (blockState.isAir() && Blocks.SNOW.defaultBlockState().canSurvive(level, blockPos)) {
                return true;
            }
        }
        
        return false;
    }
}
