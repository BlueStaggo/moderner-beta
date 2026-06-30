package mod.bluestaggo.modernerbeta.level.carver;

import com.mojang.serialization.Codec;
//? if >=26.3 {
/*import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
*///? }
//? if <26.3 {
import mod.bluestaggo.modernerbeta.util.BlockStates;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
//? }
import net.minecraft.core.*;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.ChunkPos;
//? if <26.3
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
//? if <26.3 {
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
//? }
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.carver.*;
//? if >=26.3
//import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
//? if <26.3
import org.apache.commons.lang3.mutable.MutableBoolean;

//? if >=26.3
//import java.util.Optional;
//? if <26.3
import java.util.function.Function;

//? if >=26.3 {
/*public record BetaCaveWorldCarver(
    float probability,
    HeightProvider y,
    FloatProvider roomVerticalRadiusMultiplier,
    FloatProvider horizontalRadiusMultiplier,
    FloatProvider verticalRadiusMultiplier,
    FloatProvider floorLevel,
    Optional<Boolean> useFixedCaves,
    Optional<Boolean> useAquifers
) implements WorldCarver {
*///? } else {
public class BetaCaveWorldCarver extends WorldCarver<BetaCaveCarverConfiguration> {
//? }
    //? if >=26.3 {
    /*public static final MapCodec<BetaCaveConfiguredWorldCarver<?>> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
        Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(c -> c.probability),
        HeightProvider.CODEC.fieldOf("y").forGetter(c -> c.y),
        FloatProviders.CODEC.fieldOf("room_vertical_radius_multiplier").forGetter(c -> c.roomVerticalRadiusMultiplier),
        FloatProviders.CODEC.fieldOf("horizontal_radius_multiplier").forGetter(c -> c.horizontalRadiusMultiplier),
        FloatProviders.CODEC.fieldOf("vertical_radius_multiplier").forGetter(c -> c.verticalRadiusMultiplier),
        FloatProviders.codec(-1.0F, 1.0F).fieldOf("floor_level").forGetter(c -> c.floorLevel),
        Codec.BOOL.optionalFieldOf("use_fixed_caves").forGetter(c -> c.useFixedCaves),
        Codec.BOOL.optionalFieldOf("use_aquifers").forGetter(c -> c.useAquifers)
    ).apply(i, BetaCaveWorldCarver::new));

    @Override
    public MapCodec<? extends ConfiguredWorldCarver<?>> codec() {
        return MAP_CODEC;
    }
    *///? } else {
    public BetaCaveWorldCarver(Codec<BetaCaveCarverConfiguration> caveCodec) {
        super(caveCodec);
    }
    //? }

    @Override
    public boolean isStartChunk(/*? <26.3 {*/ BetaCaveCarverConfiguration config, /*? }*/ RandomSource random) {
        return true;
    }

    //~ if >=26.3 'config.' -> 'this.' {
    @Override
    public boolean carve(
        //? if >=26.3 {
        /*WorldGenerationContext context,
        *///? } else {
        CarvingContext context,
        BetaCaveCarverConfiguration config,
        ChunkAccess chunk,
        Function<BlockPos, Holder<Biome>> posToBiome,
        //? }
        RandomSource random,
        //? if <26.3
        Aquifer aquiferSampler,
        //? if >=26.3
        //ChunkPos chunkPos,
        ChunkPos pos,
        //? if <26.3 {
        CarvingMask carvingMask
        //? } else {
        /*CarverOutput output
        *///? }
    ) {
        //? if <26.3
        ChunkPos chunkPos = chunk.getPos();
        boolean useFixedCaves = config.useFixedCaves.orElse(false);
        
        int caveCount = random.nextInt(random.nextInt(random.nextInt(40) + 1) + 1);
        if (random.nextInt(getMaxCaveCount()) != 0) {
            caveCount = 0;
        }

        for (int i = 0; i < caveCount; ++i) {
            double x = pos.getBlockX(random.nextInt(16)); // Starts
            double y = config.y.sample(random, context); // 1.17 stuff
            double z = pos.getBlockZ(random.nextInt(16));
            
            // 1.17 stuff
            double horizontalScale = config.horizontalRadiusMultiplier.sample(random);
            double verticalScale = config.verticalRadiusMultiplier.sample(random);
            double floorLevel = config.floorLevel.sample(random);
            
            WorldCarver.CarveSkipChecker skipPredicate = (/*? <26.3 {*/ CarvingContext, /*? }*/ scaledRelativeX, scaledRelativeY, scaledRelativeZ, relativeY) ->
                !isPositionExcluded(scaledRelativeX, scaledRelativeY, scaledRelativeZ, floorLevel);

            int tunnelCount = 1;
            if (random.nextInt(4) == 0) {
                //~ if >=26.3 'yScale.' -> 'roomVerticalRadiusMultiplier.'
                double yScale = config.yScale.sample(random);
                
                carveCave(
                    context,
                    //? if <26.3 {
                    config,
                    chunk,
                    posToBiome,
                    //? }
                    random,
                    chunkPos,
                    x, y, z,
                    yScale,
                    skipPredicate,
                    //? if >=26.3 {
                    /*output,
                    *///? } else {
                    carvingMask,
                    aquiferSampler,
                    //? }
                    useFixedCaves
                );
                tunnelCount += random.nextInt(4);
            }

            for (int j = 0; j < tunnelCount; ++j) {
                float yaw = random.nextFloat() * 3.141593F * 2.0F;
                float pitch = (random.nextFloat() - 0.5F) * 2.0F / 8F;
                float width = getTunnelSystemWidth(random, useFixedCaves);

                carveTunnels(
                    context,
                    //? if <26.3 {
                    config,
                    chunk,
                    posToBiome,
                    //? }
                    random, 
                    chunkPos,
                    x, y, z, 
                    horizontalScale, verticalScale, 
                    width, yaw, pitch, 
                    0, 0, 1.0D,
                    skipPredicate,
                    //? if >=26.3 {
                    /*output,
                    *///? } else {
                    carvingMask,
                    aquiferSampler,
                    //? }
                    useFixedCaves
                );
            }
        }

        return true;
    }

    private void carveCave(
        //? if >=26.3 {
        /*WorldGenerationContext context,
        *///? } else {
        CarvingContext context,
        BetaCaveCarverConfiguration config,
        ChunkAccess chunk,
        Function<BlockPos, Holder<Biome>> posToBiome,
        //? }
        RandomSource random,
        ChunkPos chunkPos,
        double x, 
        double y, 
        double z,
        double yScale,
        WorldCarver.CarveSkipChecker skipPredicate,
        //? if >=26.3 {
        /*CarverOutput output,
        *///? } else {
        CarvingMask carvingMask,
        Aquifer aquiferSampler,
        //? }
        boolean useFixedCaves
    ) {
        carveTunnels(
            context,
            //? if <26.3 {
            config,
            chunk,
            posToBiome,
            //? }
            random,
            chunkPos,
            x, y, z,
            1.0, 1.0,
            1.0F + random.nextFloat() * 6F,
            0.0F, 0.0F,
            -1, -1, yScale,
            skipPredicate,
            //? if >=26.3 {
            /*output,
            *///? } else {
            carvingMask,
            aquiferSampler,
            //? }
            useFixedCaves
        );
    }

    private void carveTunnels(
        //? if >=26.3 {
        /*WorldGenerationContext context,
        *///? } else {
        CarvingContext context,
        BetaCaveCarverConfiguration config,
        ChunkAccess chunk,
        Function<BlockPos, Holder<Biome>> posToBiome,
        //? }
        RandomSource initialRandom,
        ChunkPos chunkPos,
        double x,
        double y,
        double z,
        double horizontalScale,
        double verticalScale,
        float width,
        float yaw,
        float pitch,
        int branch,
        int branchCount,
        double yawPitchRatio,
        WorldCarver.CarveSkipChecker skipPredicate,
        //? if >=26.3 {
        /*CarverOutput output,
        *///? } else {
        CarvingMask carvingMask,
        Aquifer aquiferSampler,
        //? }
        boolean useFixedCaves
    ) {
        float f2 = 0.0F;
        float f3 = 0.0F;

        RandomSource random = new SingleThreadedRandomSource(initialRandom.nextLong());

        if (branchCount <= 0) {
            int someNumMaxStarts = 8 * 16 - 16;
            branchCount = someNumMaxStarts - random.nextInt(someNumMaxStarts / 4);
        }

        boolean noStarts = false;
        if (branch == -1) {
            branch = branchCount / 2;
            noStarts = true;
        }

        int randBranch = random.nextInt(branchCount / 2) + branchCount / 4;
        boolean vary = random.nextInt(6) == 0;

        for (; branch < branchCount; branch++) {
            double tunnelHorizontalScale = 1.5D + (double) (Mth.sin((float) branch * 3.141593F / (float) branchCount)
                    * width * 1.0F);
            double tunnelVerticalScale = tunnelHorizontalScale * yawPitchRatio;

            float f4 = Mth.cos(pitch);
            float f5 = Mth.sin(pitch);

            x += Mth.cos(yaw) * f4;
            y += f5;
            z += Mth.sin(yaw) * f4;

            pitch *= vary ? 0.92F : 0.7F;

            pitch += f3 * 0.1F;
            yaw += f2 * 0.1F;

            f3 *= 0.9F;
            f2 *= 0.75F;

            f3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            f2 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4F;

            if (!noStarts && branch == randBranch && width > 1.0F) {
                carveTunnels(
                    context,
                    //? if <26.3 {
                    config,
                    chunk,
                    posToBiome,
                    //? }
                    useFixedCaves ? random : initialRandom,
                    chunkPos,
                    x, y, z,
                    horizontalScale, verticalScale,
                    random.nextFloat() * 0.5F + 0.5F,
                    yaw - 1.570796F, pitch / 3F,
                    branch, branchCount, 1.0D,
                    skipPredicate,
                    //? if >=26.3 {
                    /*output,
                    *///? } else {
                    carvingMask,
                    aquiferSampler,
                    //? }
                    useFixedCaves
                );
                carveTunnels(
                    context,
                    //? if <26.3 {
                    config,
                    chunk,
                    posToBiome,
                    //? }
                    useFixedCaves ? random : initialRandom,
                    chunkPos,
                    x, y, z,
                    horizontalScale, verticalScale,
                    random.nextFloat() * 0.5F + 0.5F,
                    yaw + 1.570796F, pitch / 3F,
                    branch, branchCount, 1.0D,
                    skipPredicate,
                    //? if >=26.3 {
                    /*output,
                    *///? } else {
                    carvingMask,
                    aquiferSampler,
                    //? }
                    useFixedCaves
                );
                return;
            }

            if (!noStarts && random.nextInt(4) == 0) {
                continue;
            }

            if (!canCarveBranch(chunkPos, x, z, branch, branchCount, width)) {
                return;
            }

            carveRegion(
                context,
                //? if <26.3 {
                config,
                chunk,
                posToBiome,
                //? }
                chunkPos,
                x, y, z, 
                tunnelHorizontalScale * horizontalScale, 
                tunnelVerticalScale * verticalScale,
                skipPredicate,
                //? if >=26.3 {
                /*output
                *///? } else {
                carvingMask,
                aquiferSampler
                //? }
            );

            if (noStarts) {
                break;
            }
        }

    }

    private boolean carveRegion(
        //? if >=26.3 {
        /*WorldGenerationContext context,
        *///? } else {
        CarvingContext context,
        BetaCaveCarverConfiguration config,
        ChunkAccess chunk,
        Function<BlockPos, Holder<Biome>> posToBiome,
        //? }
        ChunkPos chunkPos,
        double x, 
        double y, 
        double z, 
        double horizontalScale,
        double verticalScale,
        WorldCarver.CarveSkipChecker skipPredicate,
        //? if >=26.3 {
        /*CarverOutput output
        *///? } else {
        CarvingMask carvingMask,
        Aquifer aquiferSampler
        //? }
    ) {
        double ctrX = chunkPos.x() * 16 + 8;
        double ctrZ = chunkPos.z() * 16 + 8;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos tmp = new BlockPos.MutableBlockPos();

        if ( // Check for valid tunnel starts, I guess? Or to prevent overlap?
        x < ctrX - 16D - horizontalScale * 2D || z < ctrZ - 16D - horizontalScale * 2D || x > ctrX + 16D + horizontalScale * 2D
                || z > ctrZ + 16D + horizontalScale * 2D) {
            return false;
        }

        int mainChunkStartX = chunkPos.x() * 16;
        int mainChunkStartZ = chunkPos.z() * 16;
        
        // Get min and max extents of tunnel, relative to chunk coords.
        int minX = Mth.floor(x - horizontalScale) - mainChunkStartX - 1;
        int maxX = Mth.floor(x + horizontalScale) - mainChunkStartX + 1;

        int minY = Mth.floor(y - verticalScale) - 1;
        int maxY = Mth.floor(y + verticalScale) + 1;

        int minZ = Mth.floor(z - horizontalScale) - mainChunkStartZ - 1;
        int maxZ = Mth.floor(z + horizontalScale) - mainChunkStartZ + 1;

        if (minX < 0) {
            minX = 0;
        }
        if (maxX > 16) {
            maxX = 16;
        }

        //~ if >=26.3 'context.getMinGenY() + 1' -> 'output.minY()', 'context.getMinGenY() + context.getGenDepth() - 8' -> 'output.maxY()' {
        if (minY < context.getMinGenY() + 1) {
            minY = context.getMinGenY() + 1;
        }
        if (maxY > context.getMinGenY() + context.getGenDepth() - 8) {
            maxY = context.getMinGenY() + context.getGenDepth() - 8;
        }
        //~ }

        if (minZ < 0) {
            minZ = 0;
        }
        if (maxZ > 16) {
            maxZ = 16;
        }

        if (isRegionUncarvable(context, /*? if >=26.3 {*/ /*output, *//*? } else {*/ config, chunk, /*? }*/ chunkPos, minX, maxX, minY, maxY, minZ, maxZ)) {
            return false;
        }

        boolean carved = /*? if >=26.3 {*/ /*true *//*? } else { */ false /*? }*/;
        for (int localX = minX; localX < maxX; localX++) {
            //? if <26.3
            int offsetX = chunkPos.getBlockX(localX);
            double scaledRelX = ((double) (localX + chunkPos.x() * 16) + 0.5D - x) / horizontalScale;

            for (int localZ = minZ; localZ < maxZ; localZ++) {
                //? if <26.3
                int offsetZ = chunkPos.getBlockZ(localZ);
                double scaledRelZ = ((double) (localZ + chunkPos.z() * 16) + 0.5D - z) / horizontalScale;
                //? if <26.3
                MutableBoolean replacedGrassy = new MutableBoolean(false);

                for (int localY = maxY; localY > minY; localY--) {
                    double scaledRelY = ((double) (localY - 1) + 0.5D - y) / verticalScale;

                    //? if >=26.3 {
                    /*if (!skipPredicate.shouldSkip(scaledRelX, scaledRelY, scaledRelZ, localY)) {
                        output.carve(localX, localY, localZ);
                    }
                    *///? } else {
                    if (skipPredicate.shouldSkip(context, scaledRelX, scaledRelY, scaledRelZ, localY) ||
                        carvingMask.get(localX, localY, localZ))
                        continue;

                    carvingMask.set(localX, localY, localZ);
                    pos.set(offsetX, localY, offsetZ);
                    
                    carved |= carveBlock(context, config, chunk, posToBiome, carvingMask, pos, tmp, aquiferSampler, replacedGrassy);
                    //? }
                }
            }
        }

        return carved;
    }

    //? if <26.3 {
    @Override
    protected boolean carveBlock(
            CarvingContext context,
            BetaCaveCarverConfiguration config,
            ChunkAccess chunk,
            Function<BlockPos, Holder<Biome>> posToBiome,
            CarvingMask carvingMask,
            BlockPos.MutableBlockPos pos,
            BlockPos.MutableBlockPos tmp,
            Aquifer aquiferSampler,
            MutableBoolean replacedGrassy
    ) {
        boolean useSurfaceRules = config.useSurfaceRules.orElse(false);
        if (useSurfaceRules) {
            return super.carveBlock(context, config, chunk, posToBiome, carvingMask, pos, tmp, aquiferSampler, replacedGrassy);
        }

        BlockState state = chunk.getBlockState(pos);

        boolean replacedGrass = false;
        if (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.MYCELIUM)) {
            replacedGrass = true;
        }

        // Don't use canCarveBlock for accuracy, for now.
        if (state.is(config.replaceable)) {
            BlockState carverState = getCarveState(context, config, pos, aquiferSampler);

            if (carverState == null)
                return false;

            VersionCompat.setBlockState(chunk, pos, carverState);

            if (aquiferSampler.shouldScheduleFluidUpdate() && !carverState.getFluidState().isEmpty()) {
                chunk.markPosForPostprocessing(pos);
            }

            // Replaces carved-out dirt with grass, if block that was removed was grass.
            if (replacedGrass) {
                tmp.setWithOffset(pos, Direction.DOWN);
                if (chunk.getBlockState(tmp).is(Blocks.DIRT)) {
                    VersionCompat.setBlockState(chunk, tmp, BlockStates.GRASS_BLOCK);
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public BlockState getCarveState(CarvingContext context, BetaCaveCarverConfiguration config, BlockPos pos, Aquifer aquiferSampler) {
        if (pos.getY() <= config.lavaLevel.resolveY(context)) {
            return BlockStates.LAVA;
        }

        boolean useAquifers = config.useAquifers.orElse(false);

        if (!useAquifers) {
            return BlockStates.AIR;
        }

        // TODO: Produces too many flooded caves, re-visit this later.

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        BlockState state = aquiferSampler.computeSubstance(new DensityFunction.SinglePointContext(x, y, z), 0.0);

        if (state == null) {
            return isDebugEnabled(config) ? config.debugSettings.getBarrierState() : null;
        }
        
        return isDebugEnabled(config) ? getDebugState(config, state) : state;
    }
    //? }

    private boolean canCarveBranch(
        ChunkPos chunkPos,
        double x, 
        double z, 
        int branch, 
        int branchCount,
        float baseWidth
    ) {
        double ctrX = chunkPos.x() * 16 + 8;
        double ctrZ = chunkPos.z() * 16 + 8;

        double d1 = x - ctrX;
        double d2 = z - ctrZ;
        double d3 = branchCount - branch;
        double d4 = baseWidth + 2.0F + 16F;

        if (d1 * d1 + d2 * d2 - d3 * d3 > d4 * d4) {
            return false;
        }

        return true;
    }

    //? if <26.3
    @SuppressWarnings("ConstantValue")
    private boolean isRegionUncarvable(
        //? if >=26.3 {
        /*WorldGenerationContext context,
        CarverOutput output,
        *///? } else {
        CarvingContext context,
        BetaCaveCarverConfiguration config,
        ChunkAccess chunk,
        //? }
        ChunkPos chunkPos,
        int relMinX, 
        int relMaxX,
        int minY, 
        int maxY,
        int relMinZ, 
        int relMaxZ
    ) {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        
        boolean useAquifers = config.useAquifers.orElse(false);

        for (int relX = relMinX; relX < relMaxX; relX++) {
            for (int relZ = relMinZ; relZ < relMaxZ; relZ++) {
                for (int relY = maxY + 1; relY >= minY - 1; relY--) {
                    if (relY < context.getMinGenY() || relY >= context.getMinGenY() + context.getGenDepth()) {
                        continue;
                    }

                    if (/*? >=26.3 {*/ /*output instanceof ReferenceCarvingMask refMask *//*? } else { */ true /*? }*/) {
                        //TODO: lavaLevel on 26.3
                        //? if <26.3
                        int lavaLevel = config.lavaLevel.resolveY(context);
                        //? if >=26.3 {
                        /*Block block = refMask.getBlockAt(blockPos.set(relX, relY, relZ));
                        *///? } else {
                        Block block = chunk.getBlockState(blockPos.set(relX, relY, relZ)).getBlock();
                        //? }

                        // Don't carve into water bodies, unless useAquifers enabled
                        if (!useAquifers && block == Blocks.WATER) {
                            return true;
                        }

                        // Don't carve into lava aquifers that spawn above lava level, unless useAquifers enabled
                        //? if <26.3 {
                        if (!useAquifers && block == Blocks.LAVA && relY > lavaLevel) {
                            return true;
                        }
                        //? }
                    }

                    if (relY != minY - 1 && isOnBoundary(relMinX, relMaxX, relMinZ, relMaxZ, relX, relZ)) {
                        relY = minY;
                    }
                }

            }
        }

        return false;
    }

    private boolean isPositionExcluded(
        double scaledRelativeX, 
        double scaledRelativeY, 
        double scaledRelativeZ,
        double floorY
    ) {
        return 
            scaledRelativeY > floorY && 
            scaledRelativeX * scaledRelativeX + 
            scaledRelativeY * scaledRelativeY + 
            scaledRelativeZ * scaledRelativeZ < 1.0D;
    }

    private boolean isOnBoundary(int minX, int maxX, int minZ, int maxZ, int relX, int relZ) {
        return relX != minX && relX != maxX - 1 && relZ != minZ && relZ != maxZ - 1;
    }

    //TODO: is this even used?
    protected int getCaveY(RandomSource random) {
        return random.nextInt(random.nextInt(120) + 8);
    }

    protected int getMaxCaveCount() {
        return 15;
    }

    protected float getTunnelSystemWidth(RandomSource random, boolean useFixedCaves) {
        float width = random.nextFloat() * 2.0f + random.nextFloat();
        if (useFixedCaves && random.nextInt(10) == 0) {
            width *= random.nextFloat() * random.nextFloat() * 3.0F + 1.0F;
        }
        return width;
    }

    //? if <26.3 {
    private static BlockState getDebugState(CarverConfiguration config, BlockState state) {
        if (state.is(Blocks.AIR)) {
            return config.debugSettings.getAirState();
        }
        
        if (state.is(Blocks.WATER)) {
            BlockState waterState = config.debugSettings.getWaterState();
            if (waterState.hasProperty(BlockStateProperties.WATERLOGGED)) {
                return (BlockState)waterState.setValue(BlockStateProperties.WATERLOGGED, true);
            }
            return waterState;
        }
        
        if (state.is(Blocks.LAVA)) {
            return config.debugSettings.getLavaState();
        }
        
        return state;
    }
    
    private static boolean isDebugEnabled(CarverConfiguration config) {
        return config.debugSettings.isDebugMode();
    }
    //? }
    //~ }
}
