package mod.bespectacled.modernbeta.world.feature.foliage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bespectacled.modernbeta.world.feature.ModernBetaFoliagePlacers;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

public class Oak14a08FoliagePlacer extends FoliagePlacer {
	public static final MapCodec<Oak14a08FoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(instance -> fillFoliagePlacerFields(instance)
			.and(Codec.intRange(0, 16).fieldOf("height").forGetter(placer -> placer.height))
			.apply(instance, Oak14a08FoliagePlacer::new));

	private final int height;

	public Oak14a08FoliagePlacer(IntProvider radius, IntProvider offset, int height) {
		super(radius, offset);
		this.height = height;
	}

	@Override
	protected FoliagePlacerType<?> getType() {
		return ModernBetaFoliagePlacers.OAK_14A_08_FOLIAGE_PLACER;
	}

	@Override
	protected void generate(TestableWorld world, BlockPlacer placer, Random random, TreeFeatureConfig config, int trunkHeight, TreeNode treeNode, int foliageHeight, int radius, int offset) {
		for (int y = offset; y >= offset - foliageHeight; --y) {
			this.generateSquare(world, placer, random, config, treeNode.getCenter(), radius, y, treeNode.isGiantTrunk());
		}
	}

	@Override
	public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
		return this.height;
	}

	@Override
	protected boolean isInvalidForLeaves(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
		return y == 0 && Math.abs(dx) + Math.abs(dz) >= radius * 2;
	}
}
