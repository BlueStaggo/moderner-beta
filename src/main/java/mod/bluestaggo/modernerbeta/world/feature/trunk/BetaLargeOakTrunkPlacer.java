package mod.bluestaggo.modernerbeta.world.feature.trunk;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.feature.ModernBetaTrunkPlacers;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class BetaLargeOakTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<BetaLargeOakTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(instance -> fillTrunkPlacerFields(instance)
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
    protected TrunkPlacerType<?> getType() {
        return ModernBetaTrunkPlacers.BETA_LARGE_OAK_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.TreeNode> generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, int height, BlockPos basePos, TreeFeatureConfig config) {
        int foliageHeight = 5;
        double branchDensity = 1.0;

        setToDirt(world, replacer, random, basePos.down(), config);
        int treeHeight = MathHelper.floor(height * HEIGHT_SCALE);

        if (treeHeight >= height) {
            treeHeight = height - 1;
        }

        int foliageBlobCount = Math.min(1, (int) (CLUSTER_DENSITY + Math.pow(branchDensity * height / 13.0D, 2.0D)));

        int foliageBaseY = basePos.getY() + height - foliageHeight;
        int treeTopY = basePos.getY() + treeHeight;
        int treeRelY = foliageBaseY - basePos.getY();

        List<BranchPosition> list = Lists.newArrayList();
        list.add(new BranchPosition(basePos.up(treeRelY), treeTopY));

        --foliageBaseY;

        while (treeRelY >= 0) {
            int currentBlobCount = 0;
            float foliageDistance = this.getFoliageDistance(treeRelY, height);

            // If foliage distance given, generate foliage
            if (foliageDistance >= 0.0F) {
                while (currentBlobCount < foliageBlobCount) {
                    double randRadius = branchDensity * foliageDistance * (random.nextFloat() + BRANCH_LENGTH);
                    double randAngle = random.nextFloat() * 2.0F * Math.PI;

                    int randX = (int) (randRadius * Math.sin(randAngle) + 0.5D);
                    int randZ = (int) (randRadius * Math.cos(randAngle) + 0.5D);

                    BlockPos startPos = basePos.add(randX, treeRelY, randZ);
                    BlockPos endPos = startPos.up(foliageHeight);

                    if (this.makeOrCheckBranch(world, replacer, random, startPos, endPos, false, config)) {
                        int xLength = Math.abs(basePos.getX() - startPos.getX());
                        int zLength = Math.abs(basePos.getZ() - startPos.getZ());

                        double distance = startPos.getY() - Math.sqrt(xLength * xLength + zLength * zLength) * BRANCH_SLOPE;
                        int endY = distance > treeTopY ? treeTopY : (int)distance;
                        endPos = new BlockPos(basePos.getX(), endY, basePos.getZ());

                        if (this.makeOrCheckBranch(world, replacer, random, endPos, startPos, false, config)) {
                            list.add(new BranchPosition(startPos, endPos.getY()));
                        }
                    }

                    ++currentBlobCount;
                }
            }

            --foliageBaseY;
            --treeRelY;
        }

        this.makeOrCheckBranch(world, replacer, random, basePos, basePos.up(treeHeight), true, config);
        this.makeBranches(world, replacer, random, height, basePos, list, config);
        List<FoliagePlacer.TreeNode> nodes = Lists.newArrayList();

        for (BranchPosition branchPosition : list) {
            nodes.add(branchPosition.node);
        }

        return nodes;
    }

    @Override
    public int getHeight(Random random) {
        return this.baseHeight + random.nextInt(this.firstRandomHeight + 1);
    }

    private boolean makeOrCheckBranch(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, BlockPos startPos, BlockPos branchPos, boolean make, TreeFeatureConfig config) {
        if (!make && Objects.equals(startPos, branchPos)) {
            return true;
        }

        BlockPos startMinus = branchPos.add(-startPos.getX(), -startPos.getY(), -startPos.getZ());
        int longestSide = this.getLongestSide(startMinus);
        float xM = (float)startMinus.getX() / longestSide;
        float yM = (float)startMinus.getY() / longestSide;
        float zM = (float)startMinus.getZ() / longestSide;

        for (int i = 0; i <= longestSide; i++) {
            BlockPos offset = startPos.add(MathHelper.floor(0.5F + i * xM), MathHelper.floor(0.5F + i * yM), MathHelper.floor(0.5F + i * zM));
            if (make) {
                this.getAndSetState(world, replacer, random, offset, config, state ->
                        rotateLogs ? state.withIfExists(PillarBlock.AXIS, this.getLogAxis(startPos, offset)) : state);
            } else if (!this.canReplaceOrIsLog(world, offset)) {
                return false;
            }
        }

        return true;
    }

    private int getLongestSide(BlockPos offset) {
        int x = MathHelper.abs(offset.getX());
        int y = MathHelper.abs(offset.getY());
        int z = MathHelper.abs(offset.getZ());
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

    private void makeBranches(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, int treeHeight, BlockPos startPos, List<BranchPosition> branchPositions, TreeFeatureConfig config) {
        for (BranchPosition branchPosition : branchPositions) {
            int endY = branchPosition.endY;
            BlockPos blockPos = new BlockPos(startPos.getX(), endY, startPos.getZ());
            if (!blockPos.equals(branchPosition.node.getCenter()) && this.isHighEnough(treeHeight, endY - startPos.getY())) {
                this.makeOrCheckBranch(world, replacer, random, blockPos, branchPosition.node.getCenter(), true, config);
            }
        }
    }

    private float getFoliageDistance(int treeRelY, int treeHeight) {
        if (treeRelY < treeHeight * 0.3F) {
            return -1.0F - (float) HEIGHT_SCALE;
        } else {
            float radius = treeHeight / 2.0F;
            float distFromRadius = radius - treeRelY;
            float distance = MathHelper.sqrt(radius * radius - distFromRadius * distFromRadius);
            if (distFromRadius == 0.0F) {
                distance = radius;
            } else if (Math.abs(distFromRadius) >= radius) {
                return 0.0F;
            }

            return distance * 0.5F;
        }
    }

    record BranchPosition(FoliagePlacer.TreeNode node, int endY) {
        public BranchPosition(BlockPos pos, int endY) {
            this(new FoliagePlacer.TreeNode(pos, 0, false), endY);
        }
    }
}
