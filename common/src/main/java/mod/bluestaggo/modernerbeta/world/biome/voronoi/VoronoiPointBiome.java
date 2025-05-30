package mod.bluestaggo.modernerbeta.world.biome.voronoi;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.*;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;

import java.util.List;

public record VoronoiPointBiome(Identifier biome, Identifier oceanBiome, Identifier deepOceanBiome, double temp, double rain, double weird) {
    public static final Codec<VoronoiPointBiome> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Identifier.CODEC.fieldOf("biome").forGetter(VoronoiPointBiome::biome),
            Identifier.CODEC.fieldOf("oceanBiome").forGetter(VoronoiPointBiome::oceanBiome),
            Identifier.CODEC.fieldOf("deepOceanBiome").forGetter(VoronoiPointBiome::deepOceanBiome),
            Codec.DOUBLE.fieldOf("temp").forGetter(VoronoiPointBiome::temp),
            Codec.DOUBLE.fieldOf("rain").forGetter(VoronoiPointBiome::rain),
            Codec.DOUBLE.fieldOf("weird").forGetter(VoronoiPointBiome::weird)
        ).apply(instance, VoronoiPointBiome::new)
    );

    public static final VoronoiPointBiome DEFAULT = new VoronoiPointBiome(ModernBetaBiomes.BETA_FOREST.getValue(), ModernBetaBiomes.BETA_OCEAN.getValue(), 0.5, 0.5);
    
    public VoronoiPointBiome(Identifier biome, Identifier oceanBiome, double temp, double rain) {
        this(biome, oceanBiome, oceanBiome, temp, rain, 0.5);
    }
}
