package mod.bluestaggo.modernerbeta.level.feature.placement;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.util.noise.OctaveNoise;
import mod.bluestaggo.modernerbeta.level.feature.placement.noise.NoiseBasedCountInfdev420;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.NotNull;

public class NoiseBasedCountPlacementModifierInfdev420 extends NoiseBasedCountPlacementModifier {
    public static final com.mojang.serialization.MapCodec<NoiseBasedCountPlacementModifierInfdev420> MODIFIER_CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            Codec.INT.fieldOf("count").forGetter(arg -> arg.count),
            Codec.DOUBLE.fieldOf("extra_chance").forGetter(arg -> arg.extraChance),
            Codec.INT.fieldOf("extra_count").forGetter(arg -> arg.extraCount)
        ).apply(instance, NoiseBasedCountPlacementModifierInfdev420::of));
    
    protected NoiseBasedCountPlacementModifierInfdev420(int count, double extraChance, int extraCount) {
        super(count, extraChance, extraCount);
    }
    
    public static NoiseBasedCountPlacementModifierInfdev420 of(int count, double extraChance, int extraCount) {
        return new NoiseBasedCountPlacementModifierInfdev420(count, extraChance, extraCount);
    }
    
    @Override
    public void setOctaves(OctaveNoise octaves) {
        this.noiseDecorator = new NoiseBasedCountInfdev420(octaves);
    }
    
    @Override
    public @NotNull PlacementModifierType<?> type() {
        return ModernBetaPlacementTypes.INFDEV_420_NOISE_BASED_COUNT;
    }

}
