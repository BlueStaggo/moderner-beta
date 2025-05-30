package mod.bluestaggo.modernerbeta.settings;

import com.google.gson.Gson;
import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.util.NbtCompoundBuilder;
import mod.bluestaggo.modernerbeta.util.NbtReader;
import mod.bluestaggo.modernerbeta.util.NbtTags;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomes;
import mod.bluestaggo.modernerbeta.world.biome.provider.climate.ClimateMapping;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ConfiguredLayers;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.ConstantBiomeLayer;
import mod.bluestaggo.modernerbeta.world.biome.voronoi.VoronoiPointBiome;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ModernBetaSettingsBiome implements ModernBetaSettings {
    public final Identifier biomeProvider;
    public final Identifier singleBiome;
    public final boolean useOceanBiomes;
    
    public final float climateTempNoiseScale;
    public final float climateRainNoiseScale;
    public final float climateDetailNoiseScale;
    public final float climateWeirdNoiseScale;
    public final Map<String, ClimateMapping> climateMappings;

    public final List<VoronoiPointBiome> voronoiPoints;

    public final ConfiguredLayers fractalLayers;

    public ModernBetaSettingsBiome() {
        this(new Builder());
    }
    
    public ModernBetaSettingsBiome(ModernBetaSettingsBiome.Builder builder) {
        this.biomeProvider = builder.biomeProvider;
        this.singleBiome = builder.singleBiome;
        this.useOceanBiomes = builder.useOceanBiomes;
        
        this.climateTempNoiseScale = builder.climateTempNoiseScale;
        this.climateRainNoiseScale = builder.climateRainNoiseScale;
        this.climateDetailNoiseScale = builder.climateDetailNoiseScale;
        this.climateWeirdNoiseScale = builder.climateWeirdNoiseScale;
        this.climateMappings = builder.climateMappings;

        this.voronoiPoints = builder.voronoiPoints;

        this.fractalLayers = builder.fractalLayers;
    }
    
    public static ModernBetaSettingsBiome fromString(String string) {
        Gson gson = ModernerBeta.getSettingsGson().create();
        return gson.fromJson(string, ModernBetaSettingsBiome.class);
    }
    
    public static ModernBetaSettingsBiome fromCompound(NbtCompound compound) {
        return new Builder().fromCompound(compound).build();
    }
    
    public NbtCompound toCompound() {
        NbtCompound compound = new NbtCompoundBuilder()
            .putIdentifier(NbtTags.BIOME_PROVIDER, this.biomeProvider)
            .putIdentifier(NbtTags.SINGLE_BIOME, this.singleBiome)
            .putBoolean(NbtTags.USE_OCEAN_BIOMES, this.useOceanBiomes)
            .putFloat(NbtTags.CLIMATE_TEMP_NOISE_SCALE, this.climateTempNoiseScale)
            .putFloat(NbtTags.CLIMATE_RAIN_NOISE_SCALE, this.climateRainNoiseScale)
            .putFloat(NbtTags.CLIMATE_DETAIL_NOISE_SCALE, this.climateDetailNoiseScale)
            .putFloat(NbtTags.CLIMATE_WEIRD_NOISE_SCALE, this.climateWeirdNoiseScale)
            .putCompound(NbtTags.CLIMATE_MAPPINGS, ClimateMapping.mapToNbt(this.climateMappings))
            .putList(NbtTags.VORONOI_POINTS, VoronoiPointBiome.listToNbt(this.voronoiPoints))
            .build();
        compound.put(NbtTags.FRACTAL_LAYERS, ConfiguredLayers.CODEC, this.fractalLayers);
        return compound;
    }
    
    public static class Builder {
        public Identifier biomeProvider;
        public Identifier singleBiome;
        public boolean useOceanBiomes;
        
        public float climateTempNoiseScale;
        public float climateRainNoiseScale;
        public float climateDetailNoiseScale;
        public float climateWeirdNoiseScale;
        public Map<String, ClimateMapping> climateMappings;
        
        public List<VoronoiPointBiome> voronoiPoints;

        public ConfiguredLayers fractalLayers;

        public Builder() {
            this.biomeProvider = ModernBetaBuiltInTypes.Biome.BETA.id;
            this.singleBiome = ModernBetaBiomes.BETA_PLAINS.getValue();
            this.useOceanBiomes = true;
            
            this.climateTempNoiseScale = 0.025f;
            this.climateRainNoiseScale = 0.05f;
            this.climateDetailNoiseScale = 0.25f;
            this.climateWeirdNoiseScale = 0.003125f;
            this.climateMappings = createClimateMapping(
                new ClimateMapping(
                    ModernBetaBiomes.BETA_DESERT.getValue().toString(),
                    ModernBetaBiomes.BETA_OCEAN.getValue().toString()
                ),
                new ClimateMapping(
                    ModernBetaBiomes.BETA_FOREST.getValue().toString(),
                    ModernBetaBiomes.BETA_OCEAN.getValue().toString()
                ),
                new ClimateMapping(
                    ModernBetaBiomes.BETA_TUNDRA.getValue().toString(),
                    ModernBetaBiomes.BETA_FROZEN_OCEAN.getValue().toString()
                ),
                new ClimateMapping(
                    ModernBetaBiomes.BETA_PLAINS.getValue().toString(),
                    ModernBetaBiomes.BETA_OCEAN.getValue().toString()
                ),
                new ClimateMapping(
                    ModernBetaBiomes.BETA_RAINFOREST.getValue().toString(),
                    ModernBetaBiomes.BETA_WARM_OCEAN.getValue().toString()
                ),
                new ClimateMapping(
                    ModernBetaBiomes.BETA_SAVANNA.getValue().toString(),
                    ModernBetaBiomes.BETA_OCEAN.getValue().toString()
                ),
                new ClimateMapping(
                    ModernBetaBiomes.BETA_SHRUBLAND.getValue().toString(),
                    ModernBetaBiomes.BETA_OCEAN.getValue().toString()
                ),
                new ClimateMapping(
                    ModernBetaBiomes.BETA_SEASONAL_FOREST.getValue().toString(),
                    ModernBetaBiomes.BETA_LUKEWARM_OCEAN.getValue().toString()
                ),
                new ClimateMapping(
                    ModernBetaBiomes.BETA_SWAMPLAND.getValue().toString(),
                    ModernBetaBiomes.BETA_COLD_OCEAN.getValue().toString()
                ),
                new ClimateMapping(
                    ModernBetaBiomes.BETA_TAIGA.getValue().toString(),
                    ModernBetaBiomes.BETA_FROZEN_OCEAN.getValue().toString()
                ),
                new ClimateMapping(
                    ModernBetaBiomes.BETA_TUNDRA.getValue().toString(),
                    ModernBetaBiomes.BETA_FROZEN_OCEAN.getValue().toString()
                )
            );

            this.voronoiPoints = List.of(
                new VoronoiPointBiome(
                    ModernBetaBiomes.BETA_PLAINS.getValue().toString(),
                    ModernBetaBiomes.BETA_OCEAN.getValue().toString(),
                    ModernBetaBiomes.BETA_OCEAN.getValue().toString(),
                    0.5, 0.5, 0.5
                )
            );

            this.fractalLayers = new ConfiguredLayers(Collections.singletonList(
                new ConstantBiomeLayer("land", 1234, ExtendedBiomeId.of("minecraft:plains"))
            ));
        }
        
        public Builder fromCompound(NbtCompound compound) {
            NbtReader reader = new NbtReader(compound);
            
            this.biomeProvider = reader.readIdentifier(NbtTags.BIOME_PROVIDER, this.biomeProvider);
            this.singleBiome = reader.readIdentifier(NbtTags.SINGLE_BIOME, this.singleBiome);
            this.useOceanBiomes = reader.readBoolean(NbtTags.USE_OCEAN_BIOMES, this.useOceanBiomes);
            
            this.climateTempNoiseScale = reader.readFloat(NbtTags.CLIMATE_TEMP_NOISE_SCALE, this.climateTempNoiseScale);
            this.climateRainNoiseScale = reader.readFloat(NbtTags.CLIMATE_RAIN_NOISE_SCALE, this.climateRainNoiseScale);
            this.climateDetailNoiseScale = reader.readFloat(NbtTags.CLIMATE_DETAIL_NOISE_SCALE, this.climateDetailNoiseScale);
            this.climateWeirdNoiseScale = reader.readFloat(NbtTags.CLIMATE_WEIRD_NOISE_SCALE, this.climateWeirdNoiseScale);
            this.climateMappings = ClimateMapping.mapFromReader(reader, this.climateMappings);

            this.voronoiPoints = VoronoiPointBiome.listFromReader(reader, this.voronoiPoints);

            compound.get(NbtTags.FRACTAL_LAYERS, ConfiguredLayers.CODEC)
                .ifPresent(fractalLayers -> this.fractalLayers = fractalLayers);

            return this;
        }
        
        public ModernBetaSettingsBiome build() {
            return new ModernBetaSettingsBiome(this);
        }

        public static Map<String, ClimateMapping> createClimateMapping(
            ClimateMapping desert,
            ClimateMapping forest,
            ClimateMapping iceDesert,
            ClimateMapping plains,
            ClimateMapping rainforest,
            ClimateMapping savanna,
            ClimateMapping shrubland,
            ClimateMapping seasonal_forest,
            ClimateMapping swampland,
            ClimateMapping taiga,
            ClimateMapping tundra
        ) {
            return Map.ofEntries(
                Map.entry("desert", desert),
                Map.entry("forest", forest),
                Map.entry("ice_desert", iceDesert),
                Map.entry("plains", plains),
                Map.entry("rainforest", rainforest),
                Map.entry("savanna", savanna),
                Map.entry("shrubland", shrubland),
                Map.entry("seasonal_forest", seasonal_forest),
                Map.entry("swampland", swampland),
                Map.entry("taiga", taiga),
                Map.entry("tundra", tundra)
            );
        }
    }
}