package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.world.level.biome.Biome;

import java.util.Set;

import static mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeIds.OCEAN;
import static mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeIds.PLAINS;

public class InitLandLayer extends Layer {
    public static final com.mojang.serialization.MapCodec<InitLandLayer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillLayerFields(instance)
            .and(Codec.INT.fieldOf("landChance").orElse(10).forGetter(layer -> layer.landChance))
            .apply(instance, InitLandLayer::new)
    );

    public final int landChance;

    public InitLandLayer(String id, long seed) {
        this(id, seed, 10);
    }

    public InitLandLayer(String id, long seed, int landChance) {
        super(id, seed);
        this.landChance = landChance;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.INIT_LAND;
    }

    @Override
    protected ExtendedHolder<Biome> generate(int x, int z) {
        if (x == 0 && z == 0 || this.getRandom(x, z).nextInt(this.landChance) == 0) {
            return PLAINS;
        }
        return OCEAN;
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedHolder<Biome>> biomes) {
        biomes.add(OCEAN);
        biomes.add(PLAINS);
    }
}
