package mod.bluestaggo.modernerbeta.world.feature;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.TemperatureHeightScaling;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.mixin.AccessorBiome;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
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

public class BetaFreezeTopLayerFeature extends Feature<NoneFeatureConfiguration> {
    public BetaFreezeTopLayerFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        BlockPos pos = context.origin();
        
        ChunkGenerator chunkGenerator = context.chunkGenerator();
        BiomeSource biomeSource = chunkGenerator.getBiomeSource();
        
        setFreezeTopLayer(world, pos, biomeSource, false);
        return true;
    }

    public static void setFreezeTopLayer(WorldGenLevel world, BlockPos pos, BiomeSource biomeSource, boolean modernHeightSnow) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos mutableDown = new BlockPos.MutableBlockPos();
        
        for (int localX = 0; localX < 16; ++localX) {
            for (int localZ = 0; localZ < 16; ++localZ) {
                int x = pos.getX() + localX;
                int z = pos.getZ() + localZ;
                int y = world.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
                
                mutable.set(x, y, z);
                mutableDown.set(mutable).move(Direction.DOWN, 1);
                
                TemperatureHeightScaling heightType = TemperatureHeightScaling.NONE;
                double temp = world.getBiome(mutable).value().getBaseTemperature();
                double coldThreshold = 0.15;

                Biome.TemperatureModifier temperatureModifier = ((AccessorBiome)(Object)world.getBiome(mutable).value()).getClimateSettings().temperatureModifier();
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
                
                if (canSetIce(world, mutableDown, false, temp, coldThreshold, heightType)) {
                    world.setBlock(mutableDown, Blocks.ICE.defaultBlockState(), Block.UPDATE_CLIENTS);
                }

                if (canSetSnow(world, mutable, temp, coldThreshold, heightType)) {
                    world.setBlock(mutable, Blocks.SNOW.defaultBlockState(), Block.UPDATE_CLIENTS);

                    BlockState blockState = world.getBlockState(mutableDown);
                    if (blockState.hasProperty(SnowyDirtBlock.SNOWY)) {
                        world.setBlock(mutableDown, blockState.setValue(SnowyDirtBlock.SNOWY, true), Block.UPDATE_CLIENTS);
                    }
                }
            }
        }
    }

    public static boolean canSetIce(
        LevelReader worldView,
        BlockPos blockPos,
        boolean doWaterCheck,
        double temp,
        double coldThreshold,
        TemperatureHeightScaling heightType
    ) {
        if (heightType.modifyTemperature(blockPos, temp) >= coldThreshold) {
            return false;
        }
        
        if (blockPos.getY() >= worldView.getMinY() &&
            blockPos.getY() < VersionCompat.getTopYExclusive(worldView) &&
            worldView.getBrightness(LightLayer.BLOCK, blockPos) < 10
        ) {
            BlockState blockState = worldView.getBlockState(blockPos);
            FluidState fluidState = worldView.getFluidState(blockPos);

            if (fluidState.getType() == Fluids.WATER && blockState.getBlock() instanceof LiquidBlock) {
                if (!doWaterCheck) {
                    return true;
                }

                boolean submerged = 
                    worldView.isWaterAt(blockPos.west()) &&
                    worldView.isWaterAt(blockPos.east()) &&
                    worldView.isWaterAt(blockPos.north()) &&
                    worldView.isWaterAt(blockPos.south());
                
                if (!submerged) {
                    return true;
                }
            }
        }
        
        return false;
    }

    public static boolean canSetSnow(LevelReader worldView, BlockPos blockPos, double temp, double coldThreshold, TemperatureHeightScaling heightType) {
        if (heightType.modifyTemperature(blockPos, temp) >= coldThreshold) {
            return false;
        }
        
        if (blockPos.getY() >= 0 && blockPos.getY() < 256 && worldView.getBrightness(LightLayer.BLOCK, blockPos) < 10) {
            BlockState blockState = worldView.getBlockState(blockPos);
            
            if (blockState.isAir() && Blocks.SNOW.defaultBlockState().canSurvive(worldView, blockPos)) {
                return true;
            }
        }
        
        return false;
    }
}
