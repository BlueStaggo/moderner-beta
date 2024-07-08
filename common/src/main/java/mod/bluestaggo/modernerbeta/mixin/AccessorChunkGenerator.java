package mod.bluestaggo.modernerbeta.mixin;

import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.util.PlacedFeatureIndexer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.function.Supplier;

@Mixin(ChunkGenerator.class)
public interface AccessorChunkGenerator {
    @Accessor
    public Supplier<List<PlacedFeatureIndexer.IndexedFeatures>> getIndexedFeaturesListSupplier();
}
