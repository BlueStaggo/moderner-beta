package mod.bluestaggo.modernerbeta.level.feature;

//? if >=26.3
//import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.feature.*;
//? if <26.3
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

import java.util.List;

//~ if >=26.3 'Feature<OreConfiguration>' -> 'AbstractOreFeature'
public class BetaOreClayFeature extends Feature<OreConfiguration> {
    //? if >=26.3 {
    /*public static final MapCodec<BetaOreClayFeature> CODEC = makeCodec(BetaOreClayFeature::new);

    public BetaOreClayFeature(List<BlockReplacement> targetStates, int size, float discardChanceOnAirExposure) {
        super(targetStates, size, discardChanceOnAirExposure);
    }

    public BetaOreClayFeature(
        net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest target,
        BlockState state,
        int size
    ) {
        this(List.of(new BlockReplacement(target, state)), size, 0.0F);
    }

    @Override
    public MapCodec<BetaOreClayFeature> codec() {
        return CODEC;
    }
    *///? } else {
    public BetaOreClayFeature(com.mojang.serialization.Codec<OreConfiguration> configCodec) {
        super(configCodec);
    }
    //? }

    @Override
    public boolean place(
        //? if >=26.3 {
        /*WorldGenLevel level,
        net.minecraft.world.level.chunk.ChunkGenerator chunkGenerator,
        RandomSource random,
        BlockPos pos
        *///? } else {
        FeaturePlaceContext<OreConfiguration> context
        //? }
    ) {
        //? if <26.3 {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        OreConfiguration config = context.config();
        RandomSource random = context.random();
        //? }
        
        int baseX = pos.getX();
        int baseY = pos.getY();
        int baseZ = pos.getZ();

        //~ if >=26.3 'config.' -> 'this.'
        int numberOfBlocks = config.size;
        
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        
        if(!level.isStateAtPosition(pos, state -> state.is(Blocks.WATER))) {
            return false;
        }
        
        float radius = random.nextFloat() * 3.141593F;
        
        double x0 = (float)(baseX + 8) + (Mth.sin(radius) * (float)numberOfBlocks) / 8F;
        double x1 = (float)(baseX + 8) - (Mth.sin(radius) * (float)numberOfBlocks) / 8F;
        double z0 = (float)(baseZ + 8) + (Mth.cos(radius) * (float)numberOfBlocks) / 8F;
        double z1 = (float)(baseZ + 8) - (Mth.cos(radius) * (float)numberOfBlocks) / 8F;
        
        double y0 = baseY + random.nextInt(3) + 2;
        double y1 = baseY + random.nextInt(3) + 2;
        
        try (BulkSectionAccess chunkSectionCache = new BulkSectionAccess(level)) {
            for(int block = 0; block <= numberOfBlocks; block++) {
                double d6 = x0 + ((x1 - x0) * (double)block) / (double)numberOfBlocks;
                double d7 = y0 + ((y1 - y0) * (double)block) / (double)numberOfBlocks;
                double d8 = z0 + ((z1 - z0) * (double)block) / (double)numberOfBlocks;
                
                double d9 = (random.nextDouble() * (double)numberOfBlocks) / 16D;
                
                double d10 = (double)(Mth.sin(((float)block * 3.141593F) / (float)numberOfBlocks) + 1.0F) * d9 + 1.0D;
                double d11 = (double)(Mth.sin(((float)block * 3.141593F) / (float)numberOfBlocks) + 1.0F) * d9 + 1.0D;
                
                int minX = Mth.floor(d6 - d10 / 2D);
                int maxX = Mth.floor(d6 + d10 / 2D);
                int minY = Mth.floor(d7 - d11 / 2D);
                int maxY = Mth.floor(d7 + d11 / 2D);
                int minZ = Mth.floor(d8 - d10 / 2D);
                int maxZ = Mth.floor(d8 + d10 / 2D);
                
                for(int x = minX; x <= maxX; x++) {
                    for(int y = minY; y <= maxY; y++) {
                        for(int z = minZ; z <= maxZ; z++) {
                            double dX = (((double)x + 0.5D) - d6) / (d10 / 2D);
                            double dY = (((double)y + 0.5D) - d7) / (d11 / 2D);
                            double dZ = (((double)z + 0.5D) - d8) / (d10 / 2D);
                            
                            // Check bounds
                            if(dX * dX + dY * dY + dZ * dZ >= 1.0D) {
                                continue;
                            }

                            LevelChunkSection chunkSection = chunkSectionCache.getSection(mutablePos.set(x, y, z));
                            
                            if (!level.isOutsideBuildHeight(y) && chunkSection != null) {
                                int localX = SectionPos.sectionRelative(x); 
                                int localY = SectionPos.sectionRelative(y);
                                int localZ = SectionPos.sectionRelative(z);
                                BlockState state = chunkSection.getBlockState(localX, localY, localZ);

                                //~ if >=26.3 'OreConfiguration.TargetBlockState' -> 'BlockReplacement', 'config.' -> 'this.'
                                for (final OreConfiguration.TargetBlockState target : config.targetStates) {
                                    //~ if >=26.3 'OreFeature.' -> 'this.'
                                    if (OreFeature.canPlaceOre(state, chunkSectionCache::getBlockState, random, /*? <26.3 {*/config, /*? }*/ target, mutablePos)) {
                                        //~ if >=26.3 '.state' -> '.state()'
                                        chunkSection.setBlockState(localX, localY, localZ, target.state, false);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return true;
    }
}
