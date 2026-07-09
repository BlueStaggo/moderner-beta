//~worldGenLevel
package mod.bluestaggo.modernerbeta.level.feature.trunk;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.feature.ModernBetaTrunkPlacers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BetaLargeOakTrunkPlacer extends TrunkPlacer {
    public static final com.mojang.serialization.MapCodec<BetaLargeOakTrunkPlacer> CODEC = VersionCompat.createMaybeMapCodec(instance -> trunkPlacerParts(instance)
            .and(Codec.BOOL.fieldOf("rotate_logs").forGetter(p -> p.rotateLogs))
            .apply(instance, BetaLargeOakTrunkPlacer::new));

    private final boolean rotateLogs;

    private static final double HEIGHT_SCALE = 0.618D;
    private static final double CLUSTER_DENSITY = 1.382D;
    private static final double BRANCH_SLOPE = 0.381D;
    private static final double BRANCH_LENGTH = 0.328D;

    public BetaLargeOakTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight, boolean rotateLogs) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
        this.rotateLogs = rotateLogs;
    }

    @Override
    protected @NotNull TrunkPlacerType<?> type() {
        return ModernBetaTrunkPlacers.BETA_LARGE_OAK;
    }

    @Override
    public @NotNull List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> replacer, RandomSource random, int height, BlockPos basePos, TreeConfiguration config) {
        if (!canGenerate(level, basePos, height))
            return List.of();

        int foliageHeight = 5;
        double branchDensity = 1.0;

        setDirtAt(level, replacer, random, basePos.below(), config);
        int treeHeight = Mth.floor(height * HEIGHT_SCALE);

        if (treeHeight >= height) {
            treeHeight = height - 1;
        }

        int foliageBlobCount = Math.min(1, (int) (CLUSTER_DENSITY + Math.pow(branchDensity * height / 13.0D, 2.0D)));

        int foliageBaseY = basePos.getY() + height - foliageHeight;
        int treeTopY = basePos.getY() + treeHeight;
        int treeRelY = foliageBaseY - basePos.getY();

        List<BranchPosition> list = Lists.newArrayList();
        list.add(new BranchPosition(basePos.atY(foliageBaseY), treeTopY));

        --foliageBaseY;

        while (treeRelY >= 0) {
            int currentBlobCount = 0;
            float foliageDistance = this.getFoliageDistance(treeRelY, height);

            // If foliage distance given, generate foliage
            if (foliageDistance >= 0.0F) {
                while (currentBlobCount < foliageBlobCount) {
                    double randRadius = branchDensity * foliageDistance * (random.nextFloat() + BRANCH_LENGTH);
                    double randAngle = random.nextFloat() * 2.0F * Math.PI;

                    int randX = Mth.floor(randRadius * Math.sin(randAngle) + 0.5D);
                    int randZ = Mth.floor(randRadius * Math.cos(randAngle) + 0.5D);

                    BlockPos startPos = basePos.offset(randX, treeRelY - 1, randZ);
                    BlockPos endPos = startPos.above(foliageHeight);

                    if (this.makeOrCheckBranch(level, replacer, random, startPos, endPos, false, config) == -1) {
                        int xLength = Math.abs(basePos.getX() - startPos.getX());
                        int zLength = Math.abs(basePos.getZ() - startPos.getZ());

                        double distance = startPos.getY() - Math.sqrt(xLength * xLength + zLength * zLength) * BRANCH_SLOPE;
                        int endY = distance > treeTopY ? treeTopY : (int)distance;
                        endPos = new BlockPos(basePos.getX(), endY, basePos.getZ());

                        if (this.makeOrCheckBranch(level, replacer, random, endPos, startPos, false, config) == -1) {
                            list.add(new BranchPosition(startPos, endPos.getY()));
                        }
                    }

                    ++currentBlobCount;
                }
            }

            --foliageBaseY;
            --treeRelY;
        }

        this.makeOrCheckBranch(level, replacer, random, basePos, basePos.above(treeHeight), true, config);
        this.makeBranches(level, replacer, random, height, basePos, list, config);
        List<FoliagePlacer.FoliageAttachment> nodes = Lists.newArrayList();

        for (BranchPosition branchPosition : list) {
            nodes.add(branchPosition.node);
        }

        return nodes;
    }

    @Override
    public int getTreeHeight(RandomSource random) {
        long newSeed = random.nextLong();
        random.setSeed(newSeed);

        return this.baseHeight + random.nextInt(this.heightRandA + 1);
    }

    private boolean canGenerate(LevelSimulatedReader level, BlockPos basePos, int height) {
        BlockPos treeStartPos = new BlockPos(basePos.getX(), basePos.getY(), basePos.getZ());
        BlockPos treeEndPos = treeStartPos.above(height - 1);

        /*BlockState blockState = level.getBlockState(treeStartPos.below());
        if (!blockState.is(BlockTags.DIRT)) {
            return false;
        }*/

        int testHeight = this.makeOrCheckBranch(level, null, null, treeStartPos, treeEndPos, false, null);

        if (testHeight == -1) {
            return true;
        } else if (testHeight < 6) {
            return false;
        }

        return true;
    }

    private int makeOrCheckBranch(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> replacer, RandomSource random, BlockPos startPos, BlockPos branchPos, boolean make, TreeConfiguration config) {
        if (!make && Objects.equals(startPos, branchPos)) {
            return -1;
        }

        BlockPos startMinus = branchPos.offset(-startPos.getX(), -startPos.getY(), -startPos.getZ());
        int longestSide = this.getLongestSide(startMinus);
        float xM = (float)startMinus.getX() / longestSide;
        float yM = (float)startMinus.getY() / longestSide;
        float zM = (float)startMinus.getZ() / longestSide;

        final float add = make ? 0.5F : 0.0F;

        for (int i = 0; i <= longestSide; i++) {
            BlockPos offset = startPos.offset(Mth.floor(i * xM + add), Mth.floor(i * yM + add), Mth.floor(i * zM + add));
            if (make) {
                this.placeLog(level, replacer, random, offset, config, state ->
                        rotateLogs ? state.trySetValue(RotatedPillarBlock.AXIS, this.getLogAxis(startPos, offset)) : state);
            } else if (!this.isFree(level, offset)) {
                return i;
            }
        }

        return -1;
    }

    private int getLongestSide(BlockPos offset) {
        int x = Mth.abs(offset.getX());
        int y = Mth.abs(offset.getY());
        int z = Mth.abs(offset.getZ());
        return Math.max(x, Math.max(y, z));
    }

    private Direction.Axis getLogAxis(BlockPos branchStart, BlockPos branchEnd) {
        Direction.Axis axis = Direction.Axis.Y;
        int distanceX = Math.abs(branchEnd.getX() - branchStart.getX());
        int distanceZ = Math.abs(branchEnd.getZ() - branchStart.getZ());
        int maxDistance = Math.max(distanceX, distanceZ);
        if (maxDistance > 0) {
            if (distanceX == maxDistance) {
                axis = Direction.Axis.X;
            } else {
                axis = Direction.Axis.Z;
            }
        }

        return axis;
    }

    private boolean isHighEnough(int treeHeight, int height) {
        return height >= treeHeight * 0.2;
    }

    private void makeBranches(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> replacer, RandomSource random, int treeHeight, BlockPos startPos, List<BranchPosition> branchPositions, TreeConfiguration config) {
        for (BranchPosition branchPosition : branchPositions) {
            int endY = branchPosition.endY;
            BlockPos blockPos = new BlockPos(startPos.getX(), endY, startPos.getZ());
            if (!blockPos.equals(branchPosition.node.pos()) && this.isHighEnough(treeHeight, endY - startPos.getY())) {
                this.makeOrCheckBranch(level, replacer, random, blockPos, branchPosition.node.pos(), true, config);
            }
        }
    }

    private float getFoliageDistance(int treeRelY, int treeHeight) {
        if (treeRelY < treeHeight * 0.3F) {
            return -1.0F - (float) HEIGHT_SCALE;
        } else {
            float radius = treeHeight / 2.0F;
            float distFromRadius = radius - treeRelY;
            float distance = Mth.sqrt(radius * radius - distFromRadius * distFromRadius);
            if (distFromRadius == 0.0F) {
                distance = radius;
            } else if (Math.abs(distFromRadius) >= radius) {
                return 0.0F;
            }

            return distance * 0.5F;
        }
    }

    record BranchPosition(FoliagePlacer.FoliageAttachment node, int endY) {
        public BranchPosition(BlockPos pos, int endY) {
            this(new FoliagePlacer.FoliageAttachment(pos, 0, false), endY);
        }
    }
}
