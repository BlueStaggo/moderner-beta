package mod.bluestaggo.modernerbeta.settings;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.util.LoggingUtil;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.event.Level;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public record ModernBetaSettingsPreset(
    Optional<Component> presetName,
    Optional<Component> presetDescription,
    ModernBetaSettings chunkSettings,
    ModernBetaSettings biomeSettings,
    ModernBetaSettings caveBiomeSettings
) implements NameAndDescriptionItem {
    public static final Codec<ModernBetaSettingsPreset> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            net.minecraft.network.chat.ComponentSerialization.CODEC.optionalFieldOf("name").forGetter(ModernBetaSettingsPreset::presetName),
            net.minecraft.network.chat.ComponentSerialization.CODEC.optionalFieldOf("description").forGetter(ModernBetaSettingsPreset::presetDescription),
            ModernBetaSettings.WORLD_CODEC.fieldOf("chunkSettings").forGetter(ModernBetaSettingsPreset::chunkSettings),
            ModernBetaSettings.WORLD_CODEC.fieldOf("biomeSettings").forGetter(ModernBetaSettingsPreset::biomeSettings),
            ModernBetaSettings.WORLD_CODEC.fieldOf("caveBiomeSettings").forGetter(ModernBetaSettingsPreset::caveBiomeSettings)
        ).apply(instance, ModernBetaSettingsPreset::new)
    );

    public static final Codec<ModernBetaSettingsPreset> PRESET_REFERENCE_CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("preset").forGetter(preset -> null),
            CodecUtil.registryLookupCodec()
        ).apply(instance, ModernBetaSettingsPreset::referenced)
    );

    public static final Codec<ModernBetaSettingsPreset> SETTINGS_TEXT_CODEC = Codec.either(CODEC, PRESET_REFERENCE_CODEC)
            .xmap(e -> e.map(Function.identity(), Function.identity()), Either::left);

    public ModernBetaSettingsPreset(
        ModernBetaSettings chunkSettings,
        ModernBetaSettings biomeSettings,
        ModernBetaSettings caveBiomeSettings
    ) {
        this(
            Optional.empty(),
            Optional.empty(),
            chunkSettings,
            biomeSettings,
            caveBiomeSettings
        );
    }

    public ModernBetaSettingsPreset(
        ResourceLocation presetId,
        ModernBetaSettings chunkSettings,
        ModernBetaSettings biomeSettings,
        ModernBetaSettings caveBiomeSettings
    ) {
        this(
            Optional.of(makeTitleComponent(presetId)),
            Optional.of(makeDescriptionComponent(presetId)),
            chunkSettings,
            biomeSettings,
            caveBiomeSettings
        );
    }

    public static ModernBetaSettingsPreset referenced(Holder<ModernBetaSettingsPreset> preset) {
        ModernBetaSettingsPreset presetValue = preset.value();
        ResourceLocation presetId = preset.unwrapKey().orElseThrow().location();
        RegistryOps.RegistryInfoLookup lookup = presetValue.chunkSettings.registries;

        return new ModernBetaSettingsPreset(
            Optional.of(presetValue.makeOrGetTitleComponent(presetId)),
            Optional.of(presetValue.makeOrGetDescriptionComponent(presetId)),
            ModernBetaSettings.builder(lookup)
                .add(SettingsComponentTypes.PRESET, presetId)
                .build(),
            ModernBetaSettings.builder(lookup)
                .add(SettingsComponentTypes.PRESET, presetId)
                .build(),
            ModernBetaSettings.builder(lookup)
                .add(SettingsComponentTypes.PRESET, presetId)
                .build()
        );
    }

    public static ModernBetaSettingsPreset referenced(ResourceLocation presetId) {
        return referenced(presetId, null);
    }

    public static ModernBetaSettingsPreset referenced(ResourceLocation presetId, RegistryOps.RegistryInfoLookup lookup) {
        return new ModernBetaSettingsPreset(
            presetId,
            ModernBetaSettings.builder(lookup)
                .add(SettingsComponentTypes.PRESET, presetId)
                .build(),
            ModernBetaSettings.builder(lookup)
                .add(SettingsComponentTypes.PRESET, presetId)
                .build(),
            ModernBetaSettings.builder(lookup)
                .add(SettingsComponentTypes.PRESET, presetId)
                .build()
        );
    }

    public ModernBetaSettingsPreset(
        HolderLookup.Provider registries,
        CompoundTag newChunkSettings,
        CompoundTag newBiomeSettings,
        CompoundTag newCaveBiomeSettings
    ) {
        this(
            ModernBetaSettings.fromCompound(registries, newChunkSettings),
            ModernBetaSettings.fromCompound(registries, newBiomeSettings),
            ModernBetaSettings.fromCompound(registries, newCaveBiomeSettings)
        );
    }

    public static Pair<ModernBetaSettingsPreset, Boolean> fromJson(HolderLookup.Provider registries, String jsonString) {
        if (jsonString == null || jsonString.isBlank())
            return new Pair<>(null, false);

        DynamicOps<JsonElement> ops = registries != null ? RegistryOps.create(JsonOps.INSTANCE, registries) : JsonOps.INSTANCE;

        ModernBetaSettingsPreset newPreset = null;
        boolean success = false;

        try {
            Gson gson = ModernerBeta.getSettingsGson().create();
            JsonElement json = gson.fromJson(jsonString, JsonElement.class);

            newPreset = json != null ?
                    VersionCompat.getOrThrow(ModernBetaSettingsPreset.CODEC.decode(ops, json)).getFirst() : null;
            success = true;
        } catch (Exception e) {
            LoggingUtil.log(Level.ERROR, "Unable to read settings JSON! Reverting to previous settings..");
            LoggingUtil.log(Level.ERROR, String.format("Reason: %s", e.getMessage()));
        }

        return new Pair<>(newPreset, success);
    }

    public Pair<ModernBetaSettingsPreset, Boolean> setJson(HolderLookup.Provider registries, String stringChunk, String stringBiome, String stringCaveBiome) {
        ModernBetaSettings chunkSettings;
        ModernBetaSettings biomeSettings;
        ModernBetaSettings caveBiomeSettings;

        boolean successful = true;

        try {
            DynamicOps<JsonElement> ops = registries != null ? RegistryOps.create(JsonOps.INSTANCE, registries) : JsonOps.INSTANCE;
            Gson gson = ModernerBeta.getSettingsGson().create();

            JsonElement jsonChunk = stringChunk != null && !stringChunk.isBlank() ? gson.fromJson(stringChunk, JsonElement.class) : null;
            JsonElement jsonBiome = stringBiome != null && !stringBiome.isBlank() ? gson.fromJson(stringBiome, JsonElement.class) : null;
            JsonElement jsonCaveBiome = stringCaveBiome != null && !stringCaveBiome.isBlank() ? gson.fromJson(stringCaveBiome, JsonElement.class) : null;

            // Attempt to read settings
            chunkSettings = jsonChunk != null ?
                VersionCompat.getOrThrow(ModernBetaSettings.WORLD_CODEC.decode(ops, jsonChunk)).getFirst() :
                this.chunkSettings;

            biomeSettings = jsonBiome != null ?
                VersionCompat.getOrThrow(ModernBetaSettings.WORLD_CODEC.decode(ops, jsonBiome)).getFirst() :
                this.biomeSettings;

            caveBiomeSettings = jsonCaveBiome != null ?
                VersionCompat.getOrThrow(ModernBetaSettings.WORLD_CODEC.decode(ops, jsonCaveBiome)).getFirst() :
                this.caveBiomeSettings;

            // Test providers
            if (chunkSettings.get(SettingsComponentTypes.PRESET) == null)
                ModernBetaRegistries.CHUNK.get(chunkSettings.getProvider());
            if (biomeSettings.get(SettingsComponentTypes.PRESET) == null)
                ModernBetaRegistries.BIOME.get(biomeSettings.getProvider());
            if (caveBiomeSettings.get(SettingsComponentTypes.PRESET) == null)
                ModernBetaRegistries.CAVE_BIOME.get(caveBiomeSettings.getProvider());
        } catch (Exception e) {
            LoggingUtil.log(Level.ERROR, "Unable to read settings JSON! Reverting to previous settings..");
            LoggingUtil.log(Level.ERROR, String.format("Reason: %s", e.getMessage()));
            successful = false;

            chunkSettings = this.chunkSettings;
            biomeSettings = this.biomeSettings;
            caveBiomeSettings = this.caveBiomeSettings;
        }

        return new Pair<>(new ModernBetaSettingsPreset(chunkSettings, biomeSettings, caveBiomeSettings), successful);
    }

    @SuppressWarnings("ConstantValue")
    public Pair<ModernBetaSettingsPreset, Boolean> setNbt(
        HolderLookup.Provider registries,
        CompoundTag nbtChunk,
        CompoundTag nbtBiome,
        CompoundTag nbtCaveBiome
    ) {
        HolderGetter<ModernBetaSettingsPreset> presetRegistry = registries.lookupOrThrow(ModernBetaResourceKeys.SETTINGS_PRESET);

        ModernBetaSettings chunkSettings = this.chunkSettings;
        ModernBetaSettings biomeSettings = this.biomeSettings;
        ModernBetaSettings caveBiomeSettings = this.caveBiomeSettings;

        boolean successful = true;

        try {
            // Attempt to read settings
            if (nbtChunk != null) {
                chunkSettings = ModernBetaSettings.fromCompound(registries, nbtChunk);
                if (presetRegistry != null) {
                    chunkSettings = this.chunkSettings.getDifference(
                        chunkSettings, presetRegistry, ModernBetaSettingsPreset::chunkSettings);
                }
            }

            if (nbtBiome != null) {
                biomeSettings = ModernBetaSettings.fromCompound(registries, nbtBiome);
                if (presetRegistry != null) {
                    biomeSettings = this.biomeSettings.getDifference(
                        biomeSettings, presetRegistry, ModernBetaSettingsPreset::biomeSettings);
                }
            }

            if (nbtCaveBiome != null) {
                caveBiomeSettings = ModernBetaSettings.fromCompound(registries, nbtCaveBiome);
                if (presetRegistry != null) {
                    caveBiomeSettings = this.caveBiomeSettings.getDifference(
                        caveBiomeSettings, presetRegistry, ModernBetaSettingsPreset::caveBiomeSettings);
                }
            }

            // Test providers
            if (chunkSettings.get(SettingsComponentTypes.PRESET) == null)
                ModernBetaRegistries.CHUNK.get(chunkSettings.getProvider());
            if (biomeSettings.get(SettingsComponentTypes.PRESET) == null)
                ModernBetaRegistries.BIOME.get(biomeSettings.getProvider());
            if (caveBiomeSettings.get(SettingsComponentTypes.PRESET) == null)
                ModernBetaRegistries.CAVE_BIOME.get(caveBiomeSettings.getProvider());
        } catch (Exception e) {
            LoggingUtil.log(Level.ERROR, "Unable to read settings NBT! Reverting to previous settings..");
            LoggingUtil.log(Level.ERROR, String.format("Reason: %s", e.getMessage()));
            successful = false;

            chunkSettings = this.chunkSettings;
            biomeSettings = this.biomeSettings;
            caveBiomeSettings = this.caveBiomeSettings;
        }

        return new Pair<>(new ModernBetaSettingsPreset(chunkSettings, biomeSettings, caveBiomeSettings), successful);
    }

    public static Optional<ModernBetaSettingsPreset> getPreset(ResourceLocation presetId, HolderGetter<ModernBetaSettingsPreset> presetRegistry) {
        if (presetId == null) {
            return Optional.empty();
        }

        if (presetId.equals(ModernBetaSettings.DEFAULT_PRESET_ID)) {
            presetId = ModernerBeta.config.getOrDefault(SettingsComponentTypes.CONFIG_MISCELLANEOUS).defaultSettingsPreset();
        }

        Optional<Holder.Reference<ModernBetaSettingsPreset>> preset = presetRegistry.get(ResourceKey.create(ModernBetaResourceKeys.SETTINGS_PRESET, presetId));

        if (preset.isEmpty()) {
            LoggingUtil.log(Level.WARN, "Attempted to get Modern Beta preset \"" + presetId + "\", which is not registered.");
            return Optional.empty();
        }

        return Optional.of(preset.get().value());
    }

    public ModernBetaSettingsPreset mapped(HolderGetter<ModernBetaSettingsPreset> presetRegistry) {
        return new ModernBetaSettingsPreset(
            this.chunkSettings.mapPreset(presetRegistry, ModernBetaSettingsPreset::chunkSettings),
            this.biomeSettings.mapPreset(presetRegistry, ModernBetaSettingsPreset::biomeSettings),
            this.caveBiomeSettings.mapPreset(presetRegistry, ModernBetaSettingsPreset::caveBiomeSettings)
        );
    }

    public ModernBetaSettingsPreset withNameAndDesc(Component title, Component description) {
        return new ModernBetaSettingsPreset(
            Optional.of(title),
            Optional.of(description),
            this.chunkSettings,
            this.biomeSettings,
            this.caveBiomeSettings
        );
    }

    public ModernBetaSettingsPreset withNameAndDesc(ResourceLocation id) {
        return this.withNameAndDesc(makeTitleComponent(id), makeDescriptionComponent(id));
    }

    public List<ModernBetaSettings> asList() {
        return List.of(this.chunkSettings, this.biomeSettings, this.caveBiomeSettings);
    }

    @Override
    public Component makeOrGetTitleComponent(ResourceLocation fallbackId) {
        return presetName.orElseGet(() -> makeTitleComponent(fallbackId));
    }

    @Override
    public Component makeOrGetDescriptionComponent(ResourceLocation fallbackId) {
        return presetDescription.orElseGet(() -> makeDescriptionComponent(fallbackId));
    }

    private static Component makeTitleComponent(ResourceLocation id) {
        return Component.translatable("createWorld.customize.modern_beta.preset.name." + id.toLanguageKey()).withStyle(ChatFormatting.YELLOW);
    }

    private static Component makeDescriptionComponent(ResourceLocation id) {
        return Component.translatable("createWorld.customize.modern_beta.preset.desc." + id.toLanguageKey());
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
