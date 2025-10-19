//? if forge {
/*package mod.bluestaggo.modernerbeta.forgelike.registry;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Lifecycle;
import mod.bluestaggo.modernerbeta.forgelike.mixin.HolderReferenceMixin;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public class ForgeRegistryWrapper<T> implements Registry<T> {
    private final Supplier<IForgeRegistry<T>> forgeRegistrySupplier;
    private final HolderLookup.RegistryLookup<T> wrapper;

    public ForgeRegistryWrapper(Supplier<IForgeRegistry<T>> forgeRegistrySupplier) {
        this.forgeRegistrySupplier = forgeRegistrySupplier;
        this.wrapper = new HolderLookup.RegistryLookup<>() {
            @Override
            public @NotNull ResourceKey<? extends Registry<? extends T>> key() {
                return getForgeRegistry().getRegistryKey();
            }

            @Override
            public @NotNull Lifecycle registryLifecycle() {
                return Lifecycle.stable();
            }

            @Override
            public @NotNull Stream<Holder.Reference<T>> listElements() {
                return getForgeRegistry().getEntries().stream()
                    .map(entry -> Holder.Reference.createStandAlone(this, entry.getKey()));
            }

            @Override
            public @NotNull Stream<HolderSet.Named<T>> listTags() {
                return ForgeRegistryWrapper.this.getTags()
                    .map(Pair::getSecond);
            }

            @Override
            public @NotNull Optional<Holder.Reference<T>> get(@NotNull ResourceKey<T> key) {
                return ForgeRegistryWrapper.this.getHolder(key);
            }

            @Override
            public @NotNull Optional<HolderSet.Named<T>> get(@NotNull TagKey<T> tag) {
                return ForgeRegistryWrapper.this.getTag(tag);
            }
        };
    }

    public IForgeRegistry<T> getForgeRegistry() {
        IForgeRegistry<T> registry = this.forgeRegistrySupplier.get();
        if (registry == null) {
            throw new IllegalStateException("Tried to access forge registry before it is ready");
        }
        return registry;
    }

    @Override
    public @NotNull ResourceKey<? extends Registry<T>> key() {
        return this.getForgeRegistry().getRegistryKey();
    }

    @Override
    public @Nullable ResourceLocation getKey(@NotNull T value) {
        return this.getForgeRegistry().getKey(value);
    }

    @Override
    public @NotNull Optional<ResourceKey<T>> getResourceKey(@NotNull T entry) {
        return this.getForgeRegistry().getResourceKey(entry);
    }

    @Override
    public int getId(@Nullable T value) {
        throw new UnsupportedOperationException("Forge registries do not have raw IDs associated with its entries");
    }

    @Override
    public @Nullable T byId(int index) {
        throw new UnsupportedOperationException("Forge registries do not have raw IDs associated with its entries");
    }

    @Override
    public int size() {
        return this.getForgeRegistry().getEntries().size();
    }

    @Override
    public @Nullable T get(@Nullable ResourceKey<T> key) {
        if (key == null) {
            return null;
        }
        return this.getForgeRegistry().getValue(key.location());
    }

    @Override
    public @Nullable T get(@Nullable ResourceLocation id) {
        return this.getForgeRegistry().getValue(id);
    }

    @Override
    public @NotNull Lifecycle lifecycle(@NotNull T entry) {
        return Lifecycle.stable();
    }

    @Override
    public @NotNull Lifecycle registryLifecycle() {
        return Lifecycle.stable();
    }

    @Override
    public @NotNull Set<ResourceLocation> keySet() {
        return this.getForgeRegistry().getKeys();
    }

    @Override
    public @NotNull Set<Map.Entry<ResourceKey<T>, T>> entrySet() {
        return this.getForgeRegistry().getEntries();
    }

    @Override
    public @NotNull Set<ResourceKey<T>> registryKeySet() {
        return this.getForgeRegistry().getEntries().stream()
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
    }

    @Override
    public @NotNull Optional<Holder.Reference<T>> getRandom(@NotNull RandomSource random) {
        if (this.size() == 0) {
            return Optional.empty();
        }

        Collection<ResourceLocation> keys = this.getForgeRegistry().getKeys();
        int index = random.nextInt(keys.size());
        return Optional.of(this.createIntrusiveHolder(ResourceKey.create(this.key(), keys.stream().skip(index).findAny().orElseThrow())));
    }

    @Override
    public boolean containsKey(@NotNull ResourceLocation id) {
        return this.getForgeRegistry().containsKey(id);
    }

    @Override
    public boolean containsKey(ResourceKey<T> key) {
        return this.getForgeRegistry().containsKey(key.location());
    }

    @Override
    public @NotNull Registry<T> freeze() {
        return this;
    }

    @Override
    public @NotNull Holder.Reference<T> createIntrusiveHolder(@NotNull T value) {
        //return RegistryEntry.Reference.intrusive(this.wrapper, value);
        return this.createIntrusiveHolder(this.getResourceKey(value).orElseThrow(), value);
    }

    public Holder.Reference<T> createIntrusiveHolder(ResourceKey<T> key) {
        return this.createIntrusiveHolder(key, this.get(key));
    }

    @SuppressWarnings("unchecked")
    public Holder.Reference<T> createIntrusiveHolder(ResourceKey<T> key, T value) {
        Holder.Reference<T> entry = Holder.Reference.createStandAlone(this.wrapper, key);
        ((HolderReferenceMixin<T>)entry).invokeBindValue(value);
        return entry;
    }

    @Override
    public @NotNull Optional<Holder.Reference<T>> getHolder(int rawId) {
        throw new UnsupportedOperationException("Forge registries do not have raw IDs associated with its entries");
    }

    @Override
    public @NotNull Optional<Holder.Reference<T>> getHolder(@NotNull ResourceKey<T> key) {
        T value = this.get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(this.createIntrusiveHolder(value));
    }

    @Override
    public @NotNull Holder<T> wrapAsHolder(@NotNull T value) {
        return this.createIntrusiveHolder(value);
    }

    @Override
    public @NotNull Stream<Holder.Reference<T>> holders() {
        return this.getForgeRegistry().getEntries().stream()
            .map(entry -> this.createIntrusiveHolder(entry.getKey(), entry.getValue()));
    }

    @Override
    public @NotNull Optional<HolderSet.Named<T>> getTag(@NotNull TagKey<T> tag) {
        return Optional.of(HolderSet.emptyNamed(this.wrapper, tag));
    }

    @Override
    public @NotNull HolderSet.Named<T> getOrCreateTag(@NotNull TagKey<T> tag) {
        return HolderSet.emptyNamed(this.wrapper, tag);
    }

    @Override
    public @NotNull Stream<Pair<TagKey<T>, HolderSet.Named<T>>> getTags() {
        return Objects.requireNonNull(this.getForgeRegistry().tags())
            .getTagNames()
            .map(tag -> Pair.of(tag, this.getTag(tag).orElseThrow()));
    }

    @Override
    public @NotNull Stream<TagKey<T>> getTagNames() {
        return Objects.requireNonNull(this.getForgeRegistry().tags()).getTagNames();
    }

    @Override
    public void resetTags() {
    }

    @Override
    public void bindTags(@NotNull Map<TagKey<T>, List<Holder<T>>> tagEntries) {
    }

    @Override
    public @NotNull HolderOwner<T> holderOwner() {
        return this.wrapper;
    }

    @Override
    public @NotNull HolderLookup.RegistryLookup<T> asLookup() {
        return this.wrapper;
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return this.getForgeRegistry().getValues().iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ForgeRegistryWrapper<?> that = (ForgeRegistryWrapper<?>) o;
        return this.forgeRegistrySupplier.get() == that.forgeRegistrySupplier.get();
    }
}
*///?}
