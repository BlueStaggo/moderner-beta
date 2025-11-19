//~dotLocation
package mod.bluestaggo.modernerbeta.api.level.chunk;

import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.ForcedBiomeHeight;
import mod.bluestaggo.modernerbeta.level.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaChunkGenerator;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;

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
            ModernBetaRegistries.HEIGHT_CONFIG.listElements()
                .filter(Holder::isBound)
                .flatMap(entry -> {
                    TagKey<Biome> heightConfigTag = TagKey.create(Registries.BIOME, entry.unwrapKey().orElseThrow().location());
                    HeightConfig heightConfig = entry.value();
                    return chunkGenerator.getBiomeSource().possibleBiomes().stream()
                        .filter(biome -> biome.is(heightConfigTag))
                        .map(biome -> ExtendedBiomeId.of(biome.unwrapKey().orElseThrow().location(), heightConfig.type()))
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
                this.biomeHeightWeights[x + hsr + (z + hsr) * hsd] = this.calculateBiomeHeightWeight(x, z);
            }
        }
    }

    public ExtendedBiomeId getExtendedBiomeId(int biomeX, int biomeZ) {
        if (this.chunkGenerator.getBiomeSource() instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
            return modernBetaBiomeSource.getBiomeForHeightGen(biomeX, 16, biomeZ);
        } else {
            return ExtendedBiomeId.of(this.getBiome(biomeX, 16, biomeZ, null).unwrapKey().orElseThrow().location());
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
                float weight = biomeHeightWeights[biomeX + hsr + (biomeZ + hsr) * hsd];
                if (weight <= 0.0F) {
                    continue;
                }

                biome = this.getExtendedBiomeId(noiseX + biomeX, noiseZ + biomeZ);
                HeightConfig heightConfig = this.getHeightConfigOfBiome(biome);

                float thisScale = heightConfig.scale();
                float thisDepth = heightConfig.depth();

                if (!this.forcedBiomeHeight.modifyOnlyPositiveDepth() || thisDepth > 0.0F) {
                    thisScale = this.forcedBiomeHeight.scaleOffset() + thisScale * this.forcedBiomeHeight.scaleWeight();
                    thisDepth = this.forcedBiomeHeight.depthOffset() + thisDepth * this.forcedBiomeHeight.depthWeight();
                }

                weight /= Math.max(thisDepth + 2.0F, 0.01F);
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

    protected float calculateBiomeHeightWeight(int x, int z) {
        return 10.0F / Mth.sqrt((float)(x * x + z * z) + 0.2F);
    }
}
