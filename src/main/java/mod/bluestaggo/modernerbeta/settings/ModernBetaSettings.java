//~dotLocation
package mod.bluestaggo.modernerbeta.settings;

import com.google.common.collect.Iterators;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.settings.component.ClimateDistribution;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ConfiguredLayers;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ModernBetaSettings implements Iterable<SettingsComponent<?>> {
    public static final Codec<ModernBetaSettings> CODEC
        = SettingsComponentType.TYPE_TO_VALUE_MAP_CODEC.flatComapMap(
            ModernBetaSettings::new,
            settings -> DataResult.success(settings.components)
        );

    public static final Codec<ModernBetaSettings> WORLD_CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            CodecUtil.assumeMapUnsafe(SettingsComponentType.TYPE_TO_VALUE_MAP_CODEC).forGetter(settings -> settings.components),
            CodecUtil.registryLookupCodec()
        ).apply(instance, ModernBetaSettings::new)
    );

    public static final ResourceLocation DEFAULT_PRESET_ID = ModernerBeta.createId("default");

    private final Map<SettingsComponentType<?>, Object> components;
    protected RegistryOps.RegistryInfoLookup registries;

    public static ModernBetaSettings empty() {
        return new ModernBetaSettings(Collections.emptyMap());
    }

    public static Builder builder(RegistryOps.RegistryInfoLookup registries) {
        return new Builder(registries);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(ModernBetaSettings settings) {
        return new Builder(settings.registries)
            .addAll(settings);
    }

    public static Builder betaFractalLayers(Map<ResourceLocation, String> outputs, ClimateDistribution climateDistribution, Layer... pipeline) {
        return betaFractalLayers(outputs, climateDistribution, Arrays.asList(pipeline));
    }

    public static Builder betaFractalLayers(Map<ResourceLocation, String> outputs, ClimateDistribution climateDistribution, List<Layer> pipeline) {
        return betaFractalLayers(new ConfiguredLayers(pipeline, outputs), climateDistribution);
    }

    public static Builder betaFractalLayers(ConfiguredLayers configuredLayers, ClimateDistribution climateDistribution) {
        return new Builder()
            .add(SettingsComponentTypes.PROVIDER, ModernBetaBuiltInTypes.Biome.BETA_FRACTAL.id)
            .add(SettingsComponentTypes.FRACTAL_LAYERS, configuredLayers)
            .add(SettingsComponentTypes.CLIMATE_DISTRIBUTION, climateDistribution);
    }

    public static Builder fractalLayers(Map<ResourceLocation, String> outputs, Layer... pipeline) {
        return fractalLayers(outputs, Arrays.asList(pipeline));
    }

    public static Builder fractalLayers(Map<ResourceLocation, String> outputs, List<Layer> pipeline) {
        return fractalLayers(new ConfiguredLayers(pipeline, outputs));
    }

    public static Builder fractalLayers(ConfiguredLayers configuredLayers) {
        return new Builder()
            .add(SettingsComponentTypes.PROVIDER, ModernBetaBuiltInTypes.Biome.FRACTAL.id)
            .add(SettingsComponentTypes.FRACTAL_LAYERS, configuredLayers);
    }

    public static Builder singleBiome(ResourceKey<Biome> biome) {
        return new Builder()
            .add(SettingsComponentTypes.PROVIDER, ModernBetaBuiltInTypes.Biome.SINGLE.id)
            .add(SettingsComponentTypes.SINGLE_BIOME, biome.location());
    }

    public static ModernBetaSettings noCaveBiomes() {
        return new Builder()
            .add(SettingsComponentTypes.PROVIDER, ModernBetaBuiltInTypes.CaveBiome.NONE.id)
            .build();
    }

    public static ModernBetaSettings fromCompound(CompoundTag compound) {
        return VersionCompat.getOrThrow(CODEC.decode(NbtOps.INSTANCE, compound)).getFirst();
    }

    public static ModernBetaSettings fromCompound(HolderLookup.Provider registries, CompoundTag compound) {
        return VersionCompat.getOrThrow(WORLD_CODEC.decode(RegistryOps.create(NbtOps.INSTANCE, registries), compound)).getFirst();
    }

    public static ModernBetaSettings fromCompound(RegistryOps.RegistryInfoLookup registries, CompoundTag compound) {
        return VersionCompat.getOrThrow(WORLD_CODEC.decode(RegistryOps.create(NbtOps.INSTANCE, registries), compound)).getFirst();
    }

    private ModernBetaSettings(Map<SettingsComponentType<?>, Object> components) {
        this.components = components;
    }

    private ModernBetaSettings(Map<SettingsComponentType<?>, Object> components, RegistryOps.RegistryInfoLookup registries) {
        this(components);
        this.registries = registries;
    }

    public ResourceLocation getProvider() {
        return this.getOrThrow(SettingsComponentTypes.PROVIDER);
    }

    public ModernBetaSettings mapPreset(HolderGetter<ModernBetaSettingsPreset> presetRegistry, Function<ModernBetaSettingsPreset, ModernBetaSettings> settingsProvider) {
        ModernBetaSettings settings = this;

        while (true) {
            ResourceLocation presetId = settings.get(SettingsComponentTypes.PRESET);
            Optional<ModernBetaSettingsPreset> preset = ModernBetaSettingsPreset.getPreset(presetId, presetRegistry);

            if (preset.isEmpty())
                return settings;

            settings = settingsProvider.apply(preset.get())
                .extend()
                .addAll(settings.extend()
                    .remove(SettingsComponentTypes.PRESET))
                .build();
        }
    }

    public Optional<ModernBetaSettings> getBasePresetSettings(HolderGetter<ModernBetaSettingsPreset> presetRegistry, Function<ModernBetaSettingsPreset, ModernBetaSettings> settingsProvider) {
        ResourceLocation presetId = this.get(SettingsComponentTypes.PRESET);
        Optional<ModernBetaSettingsPreset> preset = ModernBetaSettingsPreset.getPreset(presetId, presetRegistry);

        return preset.map(settingsPreset -> settingsProvider.apply(settingsPreset)
                .mapPreset(presetRegistry, settingsProvider));
    }

    @SuppressWarnings("unchecked")
    public <T> T get(SettingsComponentType<T> type) {
        return (T) this.components.get(type);
    }

    public <T> T getOrDefault(SettingsComponentType<T> type) {
        T value = this.get(type);
        return value == null ? type.defaultValueGetter().getDefault(this, registries) : value;
    }

    public <T> T getOrElse(SettingsComponentType<T> type, T defaultValue) {
        T value = this.get(type);
        return value == null ? defaultValue : value;
    }

    public <T> T getOrThrow(SettingsComponentType<T> type) {
        T value = this.get(type);
        if (value == null) {
            ResourceLocation id = ModernBetaRegistries.SETTINGS_COMPONENT_TYPE.getKey(type);
            throw new IllegalArgumentException("Component of type \"" + id + "\" supplied a null value!");
        }
        return value;
    }

    public <T> SettingsComponent<T> getTyped(SettingsComponentType<T> type) {
        T value = this.get(type);
        return value != null ? new SettingsComponent<>(type, value) : null;
    }

    @SuppressWarnings("unchecked")
    public ModernBetaSettings getDifference(ModernBetaSettings newSettings, HolderGetter<ModernBetaSettingsPreset> presetRegistry, Function<ModernBetaSettingsPreset, ModernBetaSettings> settingsProvider) {
        ResourceLocation basePreset = this.get(SettingsComponentTypes.PRESET);
        if (basePreset == null)
            return newSettings;

        Optional<ModernBetaSettings> basePresetSettings = this.getBasePresetSettings(presetRegistry, settingsProvider);
        if (basePresetSettings.isEmpty())
            return newSettings;

        ModernBetaSettings baseSettings = basePresetSettings.get();
        Builder builder = new Builder(this.registries);
        builder.add(SettingsComponentTypes.PRESET, basePreset);
        DynamicOps<Tag> ops = newSettings.registries != null ? RegistryOps.create(NbtOps.INSTANCE, newSettings.registries) : NbtOps.INSTANCE;

        newSettings.stream()
            .filter(component -> {
                Codec<Object> codec = (Codec<Object>) component.type().codec();
                return !Objects.equals(
                    codec.encodeStart(ops, baseSettings.getOrDefault(component.type())).map(Function.identity()).result().orElse(null),
                    codec.encodeStart(ops, component.value()).map(Function.identity()).result().orElse(null)
                );
            })
            .forEach(builder::add);
        return builder.build();
    }

    public int size() {
        return this.components.size();
    }

    public boolean isEmpty() {
        return this.components.isEmpty();
    }

    @NotNull
    @Override
    public Iterator<SettingsComponent<?>> iterator() {
        return Iterators.transform(this.components.keySet().iterator(), type -> Objects.requireNonNull(this.getTyped(type)));
    }

    public Stream<SettingsComponent<?>> stream() {
        return StreamSupport.stream(
            Spliterators.spliterator(this.iterator(), this.components.size(),
                Spliterator.DISTINCT | Spliterator.SIZED | Spliterator.NONNULL | Spliterator.IMMUTABLE), false);
    }

    public CompoundTag toCompound() {
        DynamicOps<Tag> ops = registries != null ? RegistryOps.create(NbtOps.INSTANCE, registries) : NbtOps.INSTANCE;
        return (CompoundTag)VersionCompat.getOrThrow(CODEC.encode(this, ops, new CompoundTag()));
    }

    public Builder extend() {
        return new Builder(this.registries).addAll(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) return false;
        ModernBetaSettings that = (ModernBetaSettings) o;
        return Objects.equals(components, that.components);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(components);
    }

    @Override
    public String toString() {
        return "ModernBetaSettings {" + this.components + "}";
    }

    public static class Builder {
        private final Reference2ObjectMap<SettingsComponentType<?>, Object> components = new Reference2ObjectArrayMap<>();
        private final RegistryOps.RegistryInfoLookup registries;

        private Builder() {
            this(null);
        }

        private Builder(RegistryOps.RegistryInfoLookup registries) {
            this.registries = registries;
        }

        public <T> Builder add(SettingsComponentType<T> type, T value) {
            this.components.put(type, value);
            return this;
        }

        public <T> Builder add(SettingsComponent<T> component) {
            return this.add(component.type(), component.value());
        }

        public Builder addDefault(SettingsComponentType<?>... types) {
            for (SettingsComponentType<?> type : types) {
                this.components.put(type, type.defaultValueGetter().getDefault(null, this.registries));
            }
            return this;
        }

        public Builder addAll(ModernBetaSettings settings) {
            settings.stream().forEach(component -> this.components.put(component.type(), component.value()));
            return this;
        }

        public Builder addAll(Builder builder) {
            this.components.putAll(builder.components);
            return this;
        }

        public <T> Builder remove(SettingsComponentType<T> type) {
            this.components.remove(type);
            return this;
        }

        @SuppressWarnings("unchecked")
        public <T> Builder replace(SettingsComponentType<T> type, UnaryOperator<T> operator) {
            T component = (T)this.components.get(type);
            if (component != null) {
                this.components.put(type, operator.apply(component));
            }
            return this;
        }

        public ModernBetaSettings build() {
            return new ModernBetaSettings(
                this.components.size() < 8 ? this.components
                    : new Reference2ObjectOpenHashMap<>(this.components),
                registries
            );
        }
    }
}
