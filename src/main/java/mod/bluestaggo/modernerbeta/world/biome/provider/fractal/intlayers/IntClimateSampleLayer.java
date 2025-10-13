package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.intlayers;

import mod.bluestaggo.modernerbeta.mixin.AccessorBiome;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;

public class IntClimateSampleLayer extends IntLayer {
    private final Layer biomeLayer;
    private final boolean downfall;
    private final IntLayer parent;
    private final int mixLevel;

    public IntClimateSampleLayer(Layer biomeLayer, boolean downfall) {
        this(biomeLayer, downfall, null, 0);
    }

    public IntClimateSampleLayer(Layer biomeLayer, boolean downfall, IntLayer parent, int mixLevel) {
        super(0);
        this.biomeLayer = biomeLayer;
        this.downfall = downfall;
        this.parent = parent;
        this.mixLevel = mixLevel;
    }

    @Override
    public void init(long worldSeed) {
        if (this.parent != null) {
            this.parent.init(worldSeed);
        }
        super.init(worldSeed);
    }

    @Override
    public int generate(HolderGetter<Biome> biomeRegistry, int x, int z) {
        Biome biome = getBiomeFromLayer(biomeRegistry, this.biomeLayer, x, z);
        if (biome == null) {
            return 32768;
        }

        Biome.ClimateSettings weather = ((AccessorBiome)(Object)biome).getClimateSettings();
        float floatValue = downfall ? weather.downfall() : weather.temperature();
        int intValue = (int)(floatValue * 65536.0F);

        if (this.parent == null) {
            return intValue;
        }

        int baseValue = this.parent.sample(biomeRegistry, x, z);
        return baseValue + (intValue - baseValue) / this.mixLevel;
    }
}
