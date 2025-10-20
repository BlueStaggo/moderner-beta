package mod.bluestaggo.modernerbeta.settings;

import com.google.common.collect.Iterators;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.component.ClimateDistribution;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ConfiguredLayers;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.slf4j.event.Level;

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

    public static final ResourceLocation DEFAULT_PRESET_ID = ModernerBeta.createId("default");

    private final Map<SettingsComponentType<?>, Object> components;

    public static ModernBetaSettings empty() {
        return new ModernBetaSettings(Collections.emptyMap());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(ModernBetaSettings settings) {
        return new Builder()
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

    public static ModernBetaSettings singleBiome(ResourceKey<Biome> biome) {
        return new Builder()
            .add(SettingsComponentTypes.PROVIDER, ModernBetaBuiltInTypes.Biome.SINGLE.id)
            .add(SettingsComponentTypes.SINGLE_BIOME, biome.location())
            .build();
    }

    public static ModernBetaSettings noCaveBiomes() {
        return new Builder()
            .add(SettingsComponentTypes.PROVIDER, ModernBetaBuiltInTypes.CaveBiome.NONE.id)
            .build();
    }

    public static ModernBetaSettings fromCompound(CompoundTag compound) {
        return VersionCompat.getOrThrow(CODEC.decode(NbtOps.INSTANCE, compound)).getFirst();
    }

    private ModernBetaSettings(Map<SettingsComponentType<?>, Object> components) {
        this.components = components;
    }

    public ResourceLocation getProvider() {
        return this.getOrThrow(SettingsComponentTypes.PROVIDER);
    }

    public ModernBetaSettings mapPreset(HolderGetter<ModernBetaSettingsPreset> presetRegistry, Function<ModernBetaSettingsPreset, ModernBetaSettings> settingsProvider) {
        ModernBetaSettings settings = this;

        while (true) {
            ResourceLocation presetId = settings.get(SettingsComponentTypes.PRESET);
            if (presetId == null) {
                return settings;
            }

            if (presetId.equals(DEFAULT_PRESET_ID)) {
                presetId = ModernerBeta.config.getOrDefault(SettingsComponentTypes.CONFIG_MISCELLANEOUS).defaultSettingsPreset();
            }

            Optional<Holder.Reference<ModernBetaSettingsPreset>> preset = presetRegistry.get(ResourceKey.create(ModernBetaResourceKeys.SETTINGS_PRESET, presetId));
            if (preset.isEmpty()) {
                ModernerBeta.log(Level.WARN, "Modern beta settings reference preset \"" + presetId + "\" which is not registered.");
                return settings;
            }

            settings = settingsProvider.apply(preset.get().value())
                .extend()
                .addAll(settings.extend()
                    .remove(SettingsComponentTypes.PRESET))
                .build();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(SettingsComponentType<T> type) {
        return (T) this.components.get(type);
    }

    public <T> T getOrDefault(SettingsComponentType<T> type) {
        T value = this.get(type);
        return value == null ? type.defaultValue() : value;
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
        if (basePreset == null) {
            return newSettings;
        }
        ModernBetaSettings baseSettings = this.mapPreset(presetRegistry, settingsProvider);

        Builder builder = new Builder();
        builder.add(SettingsComponentTypes.PRESET, basePreset);
        newSettings.stream()
            .filter(component -> {
                Codec<Object> codec = (Codec<Object>) component.type().codec();
                return !Objects.equals(
                    codec.encodeStart(NbtOps.INSTANCE, baseSettings.getOrDefault(component.type())).mapOrElse(Function.identity(), error -> new Object()),
                    codec.encodeStart(NbtOps.INSTANCE, component.value()).mapOrElse(Function.identity(), error -> new Object())
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
        return (CompoundTag)VersionCompat.getOrThrow(CODEC.encode(this, NbtOps.INSTANCE, new CompoundTag()));
    }

    public Builder extend() {
        return new Builder().addAll(this);
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

        private Builder() {
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
                this.components.put(type, type.defaultValue());
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
                    : new Reference2ObjectOpenHashMap<>(this.components)
            );
        }
    }
}
