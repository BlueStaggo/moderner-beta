package mod.bluestaggo.modernerbeta.world.feature.foliage;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.feature.ModernBetaFoliagePlacers;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class BetaLargeOakFoliagePlacer extends BlobFoliagePlacer {
    public static final com.mojang.serialization.MapCodec<BetaLargeOakFoliagePlacer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> blobParts(instance).apply(instance, BetaLargeOakFoliagePlacer::new));

    public BetaLargeOakFoliagePlacer(IntProvider radius, IntProvider offset, int height) {
        super(radius, offset, height);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModernBetaFoliagePlacers.BETA_LARGE_OAK_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader world, FoliageSetter placer, RandomSource random, TreeConfiguration config, int trunkHeight, FoliageAttachment treeNode, int foliageHeight, int radius, int offset) {
        for (int curY = offset; curY >= offset - foliageHeight; curY--) {
            float blobRadius = curY != offset && curY != offset - foliageHeight ? radius + 1 : radius;

            // Generate blob layer at curY
            this.placeLeavesRow(world, placer, random, config, treeNode.pos(), (int) (blobRadius + 0.618D), curY, treeNode.doubleTrunk());
        }
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return Mth.square(Math.abs(dx) + 0.5F) + Mth.square(Math.abs(dz) + 0.5F) > radius * radius;
    }
}
