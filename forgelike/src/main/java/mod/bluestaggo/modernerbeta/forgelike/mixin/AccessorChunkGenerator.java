package mod.bluestaggo.modernerbeta.forgelike.mixin;

import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.util.PlacedFeatureIndexer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.function.Supplier;

@Mixin(ChunkGenerator.class)
public interface AccessorChunkGenerator {
    @Accessor("biomeSource")
    void setBiomeSource(BiomeSource source);
    @Accessor("indexedFeaturesListSupplier")
    void setIndexedFeaturesListSupplier(Supplier<List<PlacedFeatureIndexer.IndexedFeatures>> source);
}
