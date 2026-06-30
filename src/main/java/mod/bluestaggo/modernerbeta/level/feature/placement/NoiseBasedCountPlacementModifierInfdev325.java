package mod.bluestaggo.modernerbeta.level.feature.placement;

import com.mojang.serialization.Codec;
//? if >=26.3
//import com.mojang.serialization.MapCodec;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.util.noise.OctaveNoise;
import mod.bluestaggo.modernerbeta.level.feature.placement.noise.NoiseBasedCountInfdev325;
//? if <26.3
import org.jetbrains.annotations.NotNull;

public class NoiseBasedCountPlacementModifierInfdev325 extends NoiseBasedCountPlacementModifier {
    public static final com.mojang.serialization.MapCodec<NoiseBasedCountPlacementModifierInfdev325> MODIFIER_CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            Codec.INT.fieldOf("count").forGetter(arg -> arg.count),
            Codec.DOUBLE.fieldOf("extra_chance").forGetter(arg -> arg.extraChance),
            Codec.INT.fieldOf("extra_count").forGetter(arg -> arg.extraCount)
        ).apply(instance, NoiseBasedCountPlacementModifierInfdev325::of));

    protected NoiseBasedCountPlacementModifierInfdev325(int count, double extraChance, int extraCount) {
        super(count, extraChance, extraCount);
    }
    
    public static NoiseBasedCountPlacementModifierInfdev325 of(int count, double extraChance, int extraCount) {
        return new NoiseBasedCountPlacementModifierInfdev325(count, extraChance, extraCount);
    }
    
    @Override
    public void setOctaves(OctaveNoise octaves) {
        this.noiseDecorator = new NoiseBasedCountInfdev325(octaves);
    }
    
    //? if >=26.3 {
    /*@Override
    public MapCodec<? extends net.minecraft.world.level.levelgen.placement.RepeatingPlacement> codec() {
        return MODIFIER_CODEC;
    }
    *///? } else {
    @Override
    public @NotNull net.minecraft.world.level.levelgen.placement.PlacementModifierType<?> type() {
        return ModernBetaPlacementTypes.INFDEV_325_NOISE_BASED_COUNT;
    }
    //? }
}
