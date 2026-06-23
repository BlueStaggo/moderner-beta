package mod.bluestaggo.modernerbeta.level.feature;

//? if >=26.3
//import com.mojang.serialization.MapCodec;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.TemperatureHeightScaling;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.level.biome.ClimateHelper;
import mod.bluestaggo.modernerbeta.mixin.BiomeAccessor;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
//? if <26.3 {
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
//? }

//~ if >=26.3 'extends Feature<NoneFeatureConfiguration>' -> 'implements Feature'
public class BetaSnowAndFreezeFeature extends Feature<NoneFeatureConfiguration> {
    //? if >=26.3 {
    /*public static final BetaSnowAndFreezeFeature INSTANCE = new BetaSnowAndFreezeFeature();
    public static final MapCodec<BetaSnowAndFreezeFeature> CODEC = MapCodec.unit(INSTANCE);

    public BetaSnowAndFreezeFeature() {
    }

    @Override
    public MapCodec<? extends Feature> codec() {
        return CODEC;
    }
    *///? } else {
    public BetaSnowAndFreezeFeature(com.mojang.serialization.Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    //? }

    @Override
    public boolean place(
        //? if >=26.3 {
        /*WorldGenLevel level,
        ChunkGenerator chunkGenerator,
        net.minecraft.util.RandomSource random,
        BlockPos pos
        *///? } else {
        FeaturePlaceContext<NoneFeatureConfiguration> context
        //? }
    ) {
        //? if <26.3 {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        ChunkGenerator chunkGenerator = context.chunkGenerator();
        //? }

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
                
                if (ClimateHelper.shouldFreeze(level, mutableDown, false, temp, coldThreshold, heightType)) {
                    level.setBlock(mutableDown, Blocks.ICE.defaultBlockState(), Block.UPDATE_CLIENTS);
                }

                if (ClimateHelper.shouldSnow(level, mutable, temp, coldThreshold, heightType)) {
                    level.setBlock(mutable, Blocks.SNOW.defaultBlockState(), Block.UPDATE_CLIENTS);

                    BlockState blockState = level.getBlockState(mutableDown);
                    if (blockState.hasProperty(SnowyBlock.SNOWY)) {
                        level.setBlock(mutableDown, blockState.setValue(SnowyBlock.SNOWY, true), Block.UPDATE_CLIENTS);
                    }
                }
            }
        }
    }
}
