package mod.bluestaggo.modernerbeta.forgelike.mixin;

import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.FeatureSorter;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.function.Supplier;

@Mixin(ChunkGenerator.class)
public interface ChunkGeneratorAccessor {
    @Accessor("biomeSource")
    void setBiomeSource(BiomeSource source);
    @Accessor("featuresPerStep")
    void setFeaturesPerStep(Supplier<List<FeatureSorter.StepFeatureData>> source);
}
