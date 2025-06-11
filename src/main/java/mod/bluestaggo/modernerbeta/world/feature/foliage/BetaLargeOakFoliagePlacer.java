package mod.bluestaggo.modernerbeta.world.feature.foliage;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.feature.ModernBetaFoliagePlacers;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

public class BetaLargeOakFoliagePlacer extends BlobFoliagePlacer {
    public static final MapCodec<BetaLargeOakFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(instance -> createCodec(instance).apply(instance, BetaLargeOakFoliagePlacer::new));

    public BetaLargeOakFoliagePlacer(IntProvider radius, IntProvider offset, int height) {
        super(radius, offset, height);
    }

    @Override
    protected FoliagePlacerType<?> getType() {
        return ModernBetaFoliagePlacers.BETA_LARGE_OAK_FOLIAGE_PLACER;
    }

    @Override
    protected void generate(TestableWorld world, BlockPlacer placer, Random random, TreeFeatureConfig config, int trunkHeight, TreeNode treeNode, int foliageHeight, int radius, int offset) {
        for (int curY = offset; curY >= offset - foliageHeight; curY--) {
            float blobRadius = curY != offset && curY != offset - foliageHeight ? radius + 1 : radius;

            // Generate blob layer at curY
            this.generateSquare(world, placer, random, config, treeNode.getCenter(), (int) (blobRadius + 0.618D), curY, treeNode.isGiantTrunk());
        }
    }

    @Override
    protected boolean isInvalidForLeaves(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return MathHelper.square(Math.abs(dx) + 0.5F) + MathHelper.square(Math.abs(dz) + 0.5F) > radius * radius;
    }
}
