package mod.bluestaggo.modernerbeta.world.feature;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.TemperatureHeightScaling;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.mixin.AccessorBiome;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LightType;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class BetaFreezeTopLayerFeature extends Feature<DefaultFeatureConfig> {
    public BetaFreezeTopLayerFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos pos = context.getOrigin();
        
        ChunkGenerator chunkGenerator = context.getGenerator();
        BiomeSource biomeSource = chunkGenerator.getBiomeSource();
        
        setFreezeTopLayer(world, pos, biomeSource, false);
        return true;
    }

    public static void setFreezeTopLayer(StructureWorldAccess world, BlockPos pos, BiomeSource biomeSource, boolean modernHeightSnow) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        BlockPos.Mutable mutableDown = new BlockPos.Mutable();
        
        for (int localX = 0; localX < 16; ++localX) {
            for (int localZ = 0; localZ < 16; ++localZ) {
                int x = pos.getX() + localX;
                int z = pos.getZ() + localZ;
                int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z);
                
                mutable.set(x, y, z);
                mutableDown.set(mutable).move(Direction.DOWN, 1);
                
                TemperatureHeightScaling heightType = TemperatureHeightScaling.NONE;
                double temp = world.getBiome(mutable).value().getTemperature();
                double coldThreshold = 0.15;

                Biome.TemperatureModifier temperatureModifier = ((AccessorBiome)(Object)world.getBiome(mutable).value()).getWeather().temperatureModifier();
                if (biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
                    heightType = modernBetaBiomeSource.getBiomeProvider().getTemperatureHeightScaling();
                    if (modernBetaBiomeSource.getBiomeProvider() instanceof ClimateSampler climateSampler
                        && climateSampler.useBiomeFeature()) {
                        temp = climateSampler.sampleModifiedTemperature(mutable, temperatureModifier);
                        coldThreshold = climateSampler.getSnowThreshold();
                    } else if (temperatureModifier != Biome.TemperatureModifier.NONE) {
                        temp = temperatureModifier.getModifiedTemperature(mutable, (float)temp);
                    }
                }

                if (modernHeightSnow) {
                    heightType = TemperatureHeightScaling.MAJOR_RELEASE;
                }
                
                if (canSetIce(world, mutableDown, false, temp, coldThreshold, heightType)) {
                    world.setBlockState(mutableDown, Blocks.ICE.getDefaultState(), Block.NOTIFY_LISTENERS);
                }

                if (canSetSnow(world, mutable, temp, coldThreshold, heightType)) {
                    world.setBlockState(mutable, Blocks.SNOW.getDefaultState(), Block.NOTIFY_LISTENERS);

                    BlockState blockState = world.getBlockState(mutableDown);
                    if (blockState.contains(SnowyBlock.SNOWY)) {
                        world.setBlockState(mutableDown, blockState.with(SnowyBlock.SNOWY, true), Block.NOTIFY_LISTENERS);
                    }
                }
            }
        }
    }

    public static boolean canSetIce(
        WorldView worldView,
        BlockPos blockPos,
        boolean doWaterCheck,
        double temp,
        double coldThreshold,
        TemperatureHeightScaling heightType
    ) {
        if (heightType.modifyTemperature(blockPos, temp) >= coldThreshold) {
            return false;
        }
        
        if (blockPos.getY() >= worldView.getBottomY() &&
            blockPos.getY() < VersionCompat.getTopYExclusive(worldView) &&
            worldView.getLightLevel(LightType.BLOCK, blockPos) < 10
        ) {
            BlockState blockState = worldView.getBlockState(blockPos);
            FluidState fluidState = worldView.getFluidState(blockPos);

            if (fluidState.getFluid() == Fluids.WATER && blockState.getBlock() instanceof FluidBlock) {
                if (!doWaterCheck) {
                    return true;
                }

                boolean submerged = 
                    worldView.isWater(blockPos.west()) &&
                    worldView.isWater(blockPos.east()) &&
                    worldView.isWater(blockPos.north()) &&
                    worldView.isWater(blockPos.south());
                
                if (!submerged) {
                    return true;
                }
            }
        }
        
        return false;
    }

    public static boolean canSetSnow(WorldView worldView, BlockPos blockPos, double temp, double coldThreshold, TemperatureHeightScaling heightType) {
        if (heightType.modifyTemperature(blockPos, temp) >= coldThreshold) {
            return false;
        }
        
        if (blockPos.getY() >= 0 && blockPos.getY() < 256 && worldView.getLightLevel(LightType.BLOCK, blockPos) < 10) {
            BlockState blockState = worldView.getBlockState(blockPos);
            
            if (blockState.isAir() && Blocks.SNOW.getDefaultState().canPlaceAt(worldView, blockPos)) {
                return true;
            }
        }
        
        return false;
    }
}
