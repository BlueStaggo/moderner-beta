package mod.bluestaggo.modernerbeta.level.biome.voronoi;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.List;
import java.util.Optional;

public record VoronoiPointCaveBiome(Optional<Holder<Biome>> biome, double temp, double rain, double depth) {
    public static final Codec<VoronoiPointCaveBiome> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Biome.CODEC.optionalFieldOf("biome").forGetter(VoronoiPointCaveBiome::biome),
            Codec.DOUBLE.fieldOf("temp").forGetter(VoronoiPointCaveBiome::temp),
            Codec.DOUBLE.fieldOf("rain").forGetter(VoronoiPointCaveBiome::rain),
            Codec.DOUBLE.fieldOf("depth").forGetter(VoronoiPointCaveBiome::depth)
        ).apply(instance, VoronoiPointCaveBiome::new)
    );

    public VoronoiPointCaveBiome(Holder<Biome> biome, double temp, double rain, double depth) {
        this(Optional.of(biome), temp, rain, depth);
    }

    public static List<VoronoiPointCaveBiome> getDefaultPoints(RegistryOps.RegistryInfoLookup lookup) {
        HolderGetter<Biome> biomeRegistry = lookup.lookup(Registries.BIOME).orElseThrow().getter();

        return List.of(
            new VoronoiPointCaveBiome(Optional.empty(), 0.0, 0.5, 0.75),
            new VoronoiPointCaveBiome(biomeRegistry.getOrThrow(Biomes.LUSH_CAVES), 0.1, 0.5, 0.75),
            //? if >=26.2 {
            /*new VoronoiPointCaveBiome(Optional.empty(), 0.35, 0.5, 0.75),
            new VoronoiPointCaveBiome(Biomes.SULFUR_CAVES, 0.5, 0.5, 0.75),
            new VoronoiPointCaveBiome(Optional.empty(), 0.75, 0.5, 0.75),
            *///? } else {
            new VoronoiPointCaveBiome(Optional.empty(), 0.5, 0.5, 0.75),
            //? }
            new VoronoiPointCaveBiome(biomeRegistry.getOrThrow(Biomes.DRIPSTONE_CAVES), 0.9, 0.5, 0.75),
            new VoronoiPointCaveBiome(Optional.empty(), 1.0, 0.5, 0.75),

            new VoronoiPointCaveBiome(Optional.empty(), 0.0, 0.5, 0.25),
            new VoronoiPointCaveBiome(biomeRegistry.getOrThrow(Biomes.LUSH_CAVES), 0.2, 0.5, 0.25),
            new VoronoiPointCaveBiome(Optional.empty(), 0.4, 0.5, 0.25),
            new VoronoiPointCaveBiome(biomeRegistry.getOrThrow(Biomes.DEEP_DARK), 0.5, 0.5, 0.25),
            new VoronoiPointCaveBiome(Optional.empty(), 0.6, 0.5, 0.25),
            //? if >=26.2 {
            /*new VoronoiPointCaveBiome(Biomes.SULFUR_CAVES, 0.65, 0.5, 0.25),
            new VoronoiPointCaveBiome(Optional.empty(), 0.75, 0.5, 0.25),
            *///? }
            new VoronoiPointCaveBiome(biomeRegistry.getOrThrow(Biomes.DRIPSTONE_CAVES), 0.8, 0.5, 0.25),
            new VoronoiPointCaveBiome(Optional.empty(), 1.0, 0.5, 0.25)
        );
    }
}
