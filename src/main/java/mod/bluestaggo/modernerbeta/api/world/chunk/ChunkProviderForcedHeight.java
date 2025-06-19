package mod.bluestaggo.modernerbeta.api.world.chunk;

import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.ForcedBiomeHeight;
import mod.bluestaggo.modernerbeta.world.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ChunkProviderForcedHeight extends ChunkProviderNoise {
    private final int heightSampleRadius;
    private final float[] biomeHeightWeights;

    private final Map<ExtendedBiomeId, HeightConfig> biomeHeightValues;
    private final ForcedBiomeHeight forcedBiomeHeight;

    public ChunkProviderForcedHeight(ModernBetaChunkGenerator chunkGenerator, long seed) {
        super(chunkGenerator, seed);

        this.forcedBiomeHeight = this.getChunkSettings().getOrDefault(SettingsComponentTypes.FORCED_BIOME_HEIGHT);

        this.biomeHeightValues = Stream.concat(
            forcedBiomeHeight.heightOverrides().entrySet().stream(),
            ModernBetaRegistries.HEIGHT_CONFIG.streamEntries()
                .filter(RegistryEntry::hasKeyAndValue)
                .flatMap(entry -> {
                    TagKey<Biome> heightConfigTag = TagKey.of(RegistryKeys.BIOME, entry.getKey().orElseThrow().getValue());
                    HeightConfig heightConfig = entry.value();
                    return chunkGenerator.getBiomeSource().getBiomes().stream()
                        .filter(biome -> biome.isIn(heightConfigTag))
                        .map(biome -> ExtendedBiomeId.of(biome.getKey().orElseThrow().getValue(), heightConfig.type()))
                        .filter(extId -> !this.forcedBiomeHeight.heightOverrides().containsKey(extId))
                        .map(extId -> Map.entry(extId, heightConfig));
                })
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (base, duplicate) -> base));

        this.heightSampleRadius = this.getHeightSampleRadius();
        int hsr = this.heightSampleRadius;
        int hsd = 2 * this.heightSampleRadius + 1;
        this.biomeHeightWeights = new float[hsd * hsd];

        for (int x = -hsr; x <= hsr; x++) {
            for (int z = -hsr; z <= hsr; z++) {
                float value = 10.0F / MathHelper.sqrt((float)(x * x + z * z) + 0.2F);
                this.biomeHeightWeights[x + hsr + (z + hsr) * hsd] = value;
            }
        }
    }

    public ExtendedBiomeId getExtendedBiomeId(int biomeX, int biomeZ) {
        if (this.chunkGenerator.getBiomeSource() instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
            return modernBetaBiomeSource.getBiomeForHeightGen(biomeX, 16, biomeZ);
        } else {
            return ExtendedBiomeId.of(this.getBiome(biomeX, 16, biomeZ, null).getKey().orElseThrow().getValue());
        }
    }

    public HeightConfig getHeightConfigOfBiome(ExtendedBiomeId extendedBiomeId) {
        return this.biomeHeightValues.getOrDefault(extendedBiomeId, HeightConfig.DEFAULT);
    }

    public HeightConfig getRawHeightConfigAt(int x, int z) {
        return this.getHeightConfigOfBiome(this.getExtendedBiomeId(x, z));
    }

    public HeightConfig getHeightConfigAt(int noiseX, int noiseZ) {
        float scale = 0.0F;
        float depth = 0.0F;
        float totalWeight = 0.0F;

        ExtendedBiomeId biome = this.getExtendedBiomeId(noiseX, noiseZ);
        double minSurfaceHeight = this.getHeightConfigOfBiome(biome).depth();

        int hsr = this.heightSampleRadius;
        int hsd = 2 * this.heightSampleRadius + 1;
        for (int biomeX = -hsr; biomeX <= hsr; biomeX++) {
            for (int biomeZ = -hsr; biomeZ <= hsr; biomeZ++) {
                biome = this.getExtendedBiomeId(noiseX + biomeX, noiseZ + biomeZ);
                HeightConfig heightConfig = this.getHeightConfigOfBiome(biome);

                float thisScale = this.forcedBiomeHeight.scaleOffset() + heightConfig.scale() * this.forcedBiomeHeight.scaleWeight();
                float thisDepth = this.forcedBiomeHeight.depthOffset() + heightConfig.depth() * this.forcedBiomeHeight.depthWeight();

                float weight = biomeHeightWeights[biomeX + hsr + (biomeZ + hsr) * hsd] / Math.max(thisDepth + 2.0F, 0.01F);
                if (heightConfig.depth() > minSurfaceHeight) {
                    weight /= 2.0F;
                }

                scale += thisScale * weight;
                depth += thisDepth * weight;
                totalWeight += weight;
            }
        }

        scale /= totalWeight;
        depth /= totalWeight;
        scale = scale * 0.9F + 0.1F;
        depth = (depth * 4.0F - 1.0F) / 8.0F;

        return new HeightConfig(depth, scale);
    }

    protected int getHeightSampleRadius() {
        return 2;
    }
}
