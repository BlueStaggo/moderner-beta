package mod.bluestaggo.modernerbeta.settings;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import org.slf4j.event.Level;

import java.util.List;
import java.util.function.Supplier;

public record ModernBetaSettingsPreset(ModernBetaSettings chunkSettings, ModernBetaSettings biomeSettings, ModernBetaSettings caveBiomeSettings) {
    public static final Codec<ModernBetaSettingsPreset> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            ModernBetaSettings.CODEC.fieldOf("chunkSettings").forGetter(ModernBetaSettingsPreset::chunkSettings),
            ModernBetaSettings.CODEC.fieldOf("biomeSettings").forGetter(ModernBetaSettingsPreset::biomeSettings),
            ModernBetaSettings.CODEC.fieldOf("caveBiomeSettings").forGetter(ModernBetaSettingsPreset::caveBiomeSettings)
        ).apply(instance, ModernBetaSettingsPreset::new)
    );

    public static ModernBetaSettingsPreset referenced(ResourceLocation presetId) {
        return new ModernBetaSettingsPreset(
            ModernBetaSettings.builder()
                .add(SettingsComponentTypes.PRESET, presetId)
                .build(),
            ModernBetaSettings.builder()
                .add(SettingsComponentTypes.PRESET, presetId)
                .build(),
            ModernBetaSettings.builder()
                .add(SettingsComponentTypes.PRESET, presetId)
                .build()
        );
    }

    public ModernBetaSettingsPreset(
        CompoundTag newChunkSettings,
        CompoundTag newBiomeSettings,
        CompoundTag newCaveBiomeSettings
    ) {
        this(
            ModernBetaSettings.fromCompound(newChunkSettings),
            ModernBetaSettings.fromCompound(newBiomeSettings),
            ModernBetaSettings.fromCompound(newCaveBiomeSettings)
        );
    }
    
    public Tuple<ModernBetaSettingsPreset, Boolean> setJson(String stringChunk, String stringBiome, String stringCaveBiome) {
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
                VersionCompat.getOrThrow(ModernBetaSettings.CODEC.decode(JsonOps.INSTANCE, jsonChunk)).getFirst() :
                this.chunkSettings;
            
            biomeSettings = jsonBiome != null ?
                VersionCompat.getOrThrow(ModernBetaSettings.CODEC.decode(JsonOps.INSTANCE, jsonBiome)).getFirst() :
                this.biomeSettings;
            
            caveBiomeSettings = jsonCaveBiome != null ?
                VersionCompat.getOrThrow(ModernBetaSettings.CODEC.decode(JsonOps.INSTANCE, jsonCaveBiome)).getFirst() :
                this.caveBiomeSettings;
            
            // Test providers
            if (chunkSettings.get(SettingsComponentTypes.PRESET) == null)
                ModernBetaRegistries.CHUNK.getValue(chunkSettings.getProvider());
            if (biomeSettings.get(SettingsComponentTypes.PRESET) == null)
                ModernBetaRegistries.BIOME.getValue(biomeSettings.getProvider());
            if (caveBiomeSettings.get(SettingsComponentTypes.PRESET) == null)
                ModernBetaRegistries.CAVE_BIOME.getValue(caveBiomeSettings.getProvider());
        } catch (Exception e) {
            ModernerBeta.log(Level.ERROR, "Unable to read settings JSON! Reverting to previous settings..");
            ModernerBeta.log(Level.ERROR, String.format("Reason: %s", e.getMessage()));
            successful = false;
            
            chunkSettings = this.chunkSettings;
            biomeSettings = this.biomeSettings;
            caveBiomeSettings = this.caveBiomeSettings;
        }
        
        return new Tuple<>(new ModernBetaSettingsPreset(chunkSettings, biomeSettings, caveBiomeSettings), successful);
    }

    public Tuple<ModernBetaSettingsPreset, Boolean> setNbt(CompoundTag nbtChunk, CompoundTag nbtBiome, CompoundTag nbtCaveBiome) {
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
            if (chunkSettings.get(SettingsComponentTypes.PRESET) == null)
                ModernBetaRegistries.CHUNK.getValue(chunkSettings.getProvider());
            if (biomeSettings.get(SettingsComponentTypes.PRESET) == null)
                ModernBetaRegistries.BIOME.getValue(biomeSettings.getProvider());
            if (caveBiomeSettings.get(SettingsComponentTypes.PRESET) == null)
                ModernBetaRegistries.CAVE_BIOME.getValue(caveBiomeSettings.getProvider());
        } catch (Exception e) {
            ModernerBeta.log(Level.ERROR, "Unable to read settings NBT! Reverting to previous settings..");
            ModernerBeta.log(Level.ERROR, String.format("Reason: %s", e.getMessage()));
            successful = false;

            chunkSettings = this.chunkSettings;
            biomeSettings = this.biomeSettings;
            caveBiomeSettings = this.caveBiomeSettings;
        }

        return new Tuple<>(new ModernBetaSettingsPreset(chunkSettings, biomeSettings, caveBiomeSettings), successful);
    }
    
    public List<ModernBetaSettings> asList() {
        return List.of(this.chunkSettings, this.biomeSettings, this.caveBiomeSettings);
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
