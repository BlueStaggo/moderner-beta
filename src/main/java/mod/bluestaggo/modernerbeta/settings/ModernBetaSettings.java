package mod.bluestaggo.modernerbeta.settings;

import com.google.common.collect.Iterators;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ConfiguredLayers;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ModernBetaSettings implements Iterable<SettingsComponent<?>> {
    public static final Codec<ModernBetaSettings> CODEC
        = SettingsComponentType.TYPE_TO_VALUE_MAP_CODEC.flatComapMap(
            ModernBetaSettings::new,
            settings -> DataResult.success(settings.components)
        );

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

    public static ModernBetaSettings fractalLayers(Layer... layers) {
        return fractalLayers(Arrays.asList(layers));
    }

    public static ModernBetaSettings fractalLayers(List<Layer> layers) {
        return fractalLayers(new ConfiguredLayers(layers));
    }

    public static ModernBetaSettings fractalLayers(ConfiguredLayers configuredLayers) {
        return new Builder()
            .add(SettingsComponentTypes.PROVIDER, ModernBetaBuiltInTypes.Biome.FRACTAL.id)
            .add(SettingsComponentTypes.FRACTAL_LAYERS, configuredLayers)
            .build();
    }

    public static ModernBetaSettings singleBiome(RegistryKey<Biome> biome) {
        return new Builder()
            .add(SettingsComponentTypes.PROVIDER, ModernBetaBuiltInTypes.Biome.SINGLE.id)
            .add(SettingsComponentTypes.SINGLE_BIOME, biome.getValue())
            .build();
    }

    public static ModernBetaSettings noCaveBiomes() {
        return new Builder()
            .add(SettingsComponentTypes.PROVIDER, ModernBetaBuiltInTypes.CaveBiome.NONE.id)
            .build();
    }

    public static ModernBetaSettings fromCompound(NbtCompound compound) {
        return CODEC.decode(NbtOps.INSTANCE, compound).getOrThrow().getFirst();
    }

    private ModernBetaSettings(Map<SettingsComponentType<?>, Object> components) {
        this.components = components;
    }

    public Identifier getProvider() {
        return this.getOrThrow(SettingsComponentTypes.PROVIDER);
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
            throw new IllegalArgumentException("Could not find component \"" + type + "\"");
        }
        return value;
    }

    public <T> SettingsComponent<T> getTyped(SettingsComponentType<T> type) {
        T value = this.get(type);
        return value != null ? new SettingsComponent<>(type, value) : null;
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

    public NbtCompound toCompound() {
        return (NbtCompound)CODEC.encode(this, NbtOps.INSTANCE, new NbtCompound()).getOrThrow();
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
