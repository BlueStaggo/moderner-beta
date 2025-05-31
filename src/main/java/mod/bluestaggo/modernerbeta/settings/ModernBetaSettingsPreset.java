package mod.bluestaggo.modernerbeta.settings;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;
import org.slf4j.event.Level;

import java.util.function.Supplier;

public record ModernBetaSettingsPreset(ModernBetaSettings chunkSettings, ModernBetaSettings biomeSettings, ModernBetaSettings caveBiomeSettings) {
    public static final Codec<ModernBetaSettingsPreset> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            ModernBetaSettings.CODEC.fieldOf("chunkSettings").forGetter(ModernBetaSettingsPreset::chunkSettings),
            ModernBetaSettings.CODEC.fieldOf("biomeSettings").forGetter(ModernBetaSettingsPreset::biomeSettings),
            ModernBetaSettings.CODEC.fieldOf("caveBiomeSettings").forGetter(ModernBetaSettingsPreset::caveBiomeSettings)
        ).apply(instance, ModernBetaSettingsPreset::new)
    );

    public static final Supplier<ModernBetaSettingsPreset> DEFAULT = () -> new ModernBetaSettingsPreset(
        ModernBetaSettings.builder()
            .add(SettingsComponentTypes.PROVIDER, ModernBetaBuiltInTypes.Chunk.BETA.id)
            .addDefault(
                SettingsComponentTypes.DEEPSLATE_GENERATION,
                SettingsComponentTypes.USE_SURFACE_RULES,
                SettingsComponentTypes.SEA_LEVEL_OFFSET,
                SettingsComponentTypes.CAVE_GENERATION,
                SettingsComponentTypes.NOISE_SCALE,
                SettingsComponentTypes.NOISE_SLIDE
            )
            .build(),
        ModernBetaSettings.builder()
            .add(SettingsComponentTypes.PROVIDER, ModernBetaBuiltInTypes.Biome.BETA.id)
            .add(SettingsComponentTypes.USE_OCEAN_BIOMES, true)
            .addDefault(
                SettingsComponentTypes.CLIMATE_SCALE,
                SettingsComponentTypes.CLIMATE_MAPPINGS
            )
            .build(),
        ModernBetaSettings.builder()
            .add(SettingsComponentTypes.PROVIDER, ModernBetaBuiltInTypes.CaveBiome.VORONOI.id)
            .addDefault(SettingsComponentTypes.CAVE_BIOME_VORONOI)
            .build()
    );

    public ModernBetaSettingsPreset(
        NbtCompound newChunkSettings,
        NbtCompound newBiomeSettings,
        NbtCompound newCaveBiomeSettings
    ) {
        this(
            ModernBetaSettings.fromCompound(newChunkSettings),
            ModernBetaSettings.fromCompound(newBiomeSettings),
            ModernBetaSettings.fromCompound(newCaveBiomeSettings)
        );
    }
    
    public Pair<ModernBetaSettingsPreset, Boolean> setJson(String stringChunk, String stringBiome, String stringCaveBiome) {
        ModernBetaSettings chunkSettings;
        ModernBetaSettings biomeSettings;
        ModernBetaSettings caveBiomeSettings;
        
        boolean successful = true;
        
        try {
            Gson gson = ModernerBeta.getSettingsGson().create();

            JsonElement jsonChunk = stringChunk != null && !stringChunk.isBlank() ? gson.fromJson(stringChunk, JsonElement.class) : null;
            JsonElement jsonBiome = stringBiome != null && !stringBiome.isBlank() ? gson.fromJson(stringBiome, JsonElement.class) : null;
            JsonElement jsonCaveBiome = stringCaveBiome != null && !stringCaveBiome.isBlank() ? gson.fromJson(stringCaveBiome, JsonElement.class) : null;

            // Attempt to read settings
            chunkSettings = jsonChunk != null ?
                ModernBetaSettings.CODEC.decode(JsonOps.INSTANCE, jsonChunk).getOrThrow().getFirst() :
                this.chunkSettings;
            
            biomeSettings = jsonBiome != null ?
                ModernBetaSettings.CODEC.decode(JsonOps.INSTANCE, jsonBiome).getOrThrow().getFirst() :
                this.biomeSettings;
            
            caveBiomeSettings = jsonCaveBiome != null ?
                ModernBetaSettings.CODEC.decode(JsonOps.INSTANCE, jsonCaveBiome).getOrThrow().getFirst() :
                this.caveBiomeSettings;
            
            // Test providers
            ModernBetaRegistries.CHUNK.get(chunkSettings.getProvider());
            ModernBetaRegistries.BIOME.get(biomeSettings.getProvider());
            ModernBetaRegistries.CAVE_BIOME.get(caveBiomeSettings.getProvider());
        } catch (Exception e) {
            ModernerBeta.log(Level.ERROR, "Unable to read settings JSON! Reverting to previous settings..");
            ModernerBeta.log(Level.ERROR, String.format("Reason: %s", e.getMessage()));
            successful = false;
            
            chunkSettings = this.chunkSettings;
            biomeSettings = this.biomeSettings;
            caveBiomeSettings = this.caveBiomeSettings;
        }
        
        return new Pair<>(new ModernBetaSettingsPreset(chunkSettings, biomeSettings, caveBiomeSettings), successful);
    }

    public Pair<ModernBetaSettingsPreset, Boolean> setNbt(NbtCompound nbtChunk, NbtCompound nbtBiome, NbtCompound nbtCaveBiome) {
        ModernBetaSettings chunkSettings;
        ModernBetaSettings biomeSettings;
        ModernBetaSettings caveBiomeSettings;

        boolean successful = true;

        try {
            // Attempt to read settings
            chunkSettings = nbtChunk != null ?
                ModernBetaSettings.fromCompound(nbtChunk) :
                this.chunkSettings;

            biomeSettings = nbtBiome != null ?
                ModernBetaSettings.fromCompound(nbtBiome) :
                this.biomeSettings;

            caveBiomeSettings = nbtCaveBiome != null ?
                ModernBetaSettings.fromCompound(nbtCaveBiome) :
                this.caveBiomeSettings;

            // Test providers
            ModernBetaRegistries.CHUNK.get(chunkSettings.getProvider());
            ModernBetaRegistries.BIOME.get(biomeSettings.getProvider());
            ModernBetaRegistries.CAVE_BIOME.get(caveBiomeSettings.getProvider());
        } catch (Exception e) {
            ModernerBeta.log(Level.ERROR, "Unable to read settings NBT! Reverting to previous settings..");
            ModernerBeta.log(Level.ERROR, String.format("Reason: %s", e.getMessage()));
            successful = false;

            chunkSettings = this.chunkSettings;
            biomeSettings = this.biomeSettings;
            caveBiomeSettings = this.caveBiomeSettings;
        }

        return new Pair<>(new ModernBetaSettingsPreset(chunkSettings, biomeSettings, caveBiomeSettings), successful);
    }
    
    public ModernBetaSettingsPreset copy() {
        return this.setJson("", "", "").getLeft();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != ModernBetaSettingsPreset.class)
            return false;

        ModernBetaSettingsPreset other = (ModernBetaSettingsPreset)obj;

        return
            this.chunkSettings.equals(other.chunkSettings) &&
            this.biomeSettings.equals(other.biomeSettings) &&
            this.caveBiomeSettings.equals(other.caveBiomeSettings);
    }
}
