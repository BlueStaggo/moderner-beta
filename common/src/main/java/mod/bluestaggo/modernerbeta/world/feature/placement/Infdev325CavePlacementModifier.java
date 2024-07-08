package mod.bluestaggo.modernerbeta.world.feature.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.noise.PerlinOctaveNoise;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.FeaturePlacementContext;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Infdev325CavePlacementModifier extends PlacementModifier {
    public static final Codec<Infdev325CavePlacementModifier> MODIFIER_CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.INT.fieldOf("min_section").forGetter(i -> i.minSection),
            Codec.INT.fieldOf("max_section").forGetter(i -> i.maxSection)
        ).apply(instance, Infdev325CavePlacementModifier::of));

    private final int minSection;
    private final int maxSection;

    private PerlinOctaveNoise octaves;

    protected Infdev325CavePlacementModifier(int minSection, int maxSection) {
        this.minSection = minSection;
        this.maxSection = maxSection;
    }

    public static Infdev325CavePlacementModifier of(int minSection, int maxSection) {
        return new Infdev325CavePlacementModifier(minSection, maxSection);
    }

    public void setOctaves(PerlinOctaveNoise octaves) {
        this.octaves = octaves;
    }

    @Override
    public PlacementModifierType<?> getType() {
        return ModernBetaPlacementTypes.INFDEV_325_CAVES.get();
    }

    @Override
    public Stream<BlockPos> getPositions(FeaturePlacementContext context, Random random, BlockPos pos) {
        int x = pos.getX();
        int z = pos.getZ();
        List<BlockPos> positions = new ArrayList<>();

        double scale = 0.0625D;
        for (int section = this.minSection; section <= this.maxSection; section++) {
            final int fsection = section;
            int count = (int) (this.octaves.sample(x * scale, section * scale * 64.0D, z * scale) + (128.0D - section * 16.0D) / 64.0D);
            positions.addAll(IntStream.range(0, count).mapToObj(i -> pos.withY(fsection * 16).add(random.nextInt(16), random.nextInt(16), random.nextInt(16))).toList());
        }

        return positions.stream();
    }
}
