package mod.bluestaggo.modernerbeta.level.feature.placement;

import com.mojang.serialization.Codec;
//? if >=26.3
//import com.mojang.serialization.MapCodec;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.util.noise.OctaveNoise;
import mod.bluestaggo.modernerbeta.level.feature.placement.noise.NoiseBasedCountAlpha;
//? if <26.3
import org.jetbrains.annotations.NotNull;

public class NoiseBasedCountPlacementModifierAlpha extends NoiseBasedCountPlacementModifier {
    public static final com.mojang.serialization.MapCodec<NoiseBasedCountPlacementModifierAlpha> MODIFIER_CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            Codec.INT.fieldOf("count").forGetter(arg -> arg.count),
            Codec.DOUBLE.fieldOf("extra_chance").forGetter(arg -> arg.extraChance),
            Codec.INT.fieldOf("extra_count").forGetter(arg -> arg.extraCount)
        ).apply(instance, NoiseBasedCountPlacementModifierAlpha::of));
    
    protected NoiseBasedCountPlacementModifierAlpha(int count, double extraChance, int extraCount) {
        super(count, extraChance, extraCount);
    }
    
    public static NoiseBasedCountPlacementModifierAlpha of(int count, double extraChance, int extraCount) {
        return new NoiseBasedCountPlacementModifierAlpha(count, extraChance, extraCount);
    }
    
    @Override
    public void setOctaves(OctaveNoise octaves) {
        this.noiseDecorator = new NoiseBasedCountAlpha(octaves);
    }
    
    //? if >=26.3 {
    /*@Override
    public MapCodec<? extends net.minecraft.world.level.levelgen.placement.RepeatingPlacement> codec() {
        return MODIFIER_CODEC;
    }
    *///? } else {
    @Override
    public @NotNull net.minecraft.world.level.levelgen.placement.PlacementModifierType<?> type() {
        return ModernBetaPlacementTypes.ALPHA_NOISE_BASED_COUNT;
    }
    //? }
}
