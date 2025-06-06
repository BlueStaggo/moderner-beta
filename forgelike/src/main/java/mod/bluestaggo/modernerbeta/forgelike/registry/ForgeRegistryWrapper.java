//? if forge {
/*package mod.bluestaggo.modernerbeta.forgelike.registry;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.entry.RegistryEntryOwner;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
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
    private final RegistryWrapper.Impl<T> wrapper;

    public ForgeRegistryWrapper(Supplier<IForgeRegistry<T>> forgeRegistrySupplier) {
        this.forgeRegistrySupplier = forgeRegistrySupplier;
        this.wrapper = new RegistryWrapper.Impl<>() {
            @Override
            public RegistryKey<? extends Registry<? extends T>> getRegistryKey() {
                return getForgeRegistry().getRegistryKey();
            }

            @Override
            public Lifecycle getLifecycle() {
                return Lifecycle.stable();
            }

            @Override
            public Stream<RegistryEntry.Reference<T>> streamEntries() {
                return getForgeRegistry().getEntries().stream()
                    .map(entry -> RegistryEntry.Reference.standAlone(this, entry.getKey()));
            }

            @Override
            public Stream<RegistryEntryList.Named<T>> streamTags() {
                return ForgeRegistryWrapper.this.streamTagsAndEntries()
                    .map(Pair::getSecond);
            }

            @Override
            public Optional<RegistryEntry.Reference<T>> getOptional(RegistryKey<T> key) {
                return ForgeRegistryWrapper.this.getEntry(key);
            }

            @Override
            public Optional<RegistryEntryList.Named<T>> getOptional(TagKey<T> tag) {
                return ForgeRegistryWrapper.this.getEntryList(tag);
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
    public RegistryKey<? extends Registry<T>> getKey() {
        return this.getForgeRegistry().getRegistryKey();
    }

    @Override
    public @Nullable Identifier getId(T value) {
        return this.getForgeRegistry().getKey(value);
    }

    @Override
    public Optional<RegistryKey<T>> getKey(T entry) {
        return this.getForgeRegistry().getResourceKey(entry);
    }

    @Override
    public int getRawId(@Nullable T value) {
        throw new UnsupportedOperationException("Forge registries do not have raw IDs associated with its entries");
    }

    @Override
    public @Nullable T get(int index) {
        throw new UnsupportedOperationException("Forge registries do not have raw IDs associated with its entries");
    }

    @Override
    public int size() {
        return this.getForgeRegistry().getEntries().size();
    }

    @Override
    public @Nullable T get(@Nullable RegistryKey<T> key) {
        if (key == null) {
            return null;
        }
        return this.getForgeRegistry().getValue(key.getValue());
    }

    @Override
    public @Nullable T get(@Nullable Identifier id) {
        return this.getForgeRegistry().getValue(id);
    }

    @Override
    public Lifecycle getEntryLifecycle(T entry) {
        return Lifecycle.stable();
    }

    @Override
    public Lifecycle getLifecycle() {
        return Lifecycle.stable();
    }

    @Override
    public Set<Identifier> getIds() {
        return this.getForgeRegistry().getKeys();
    }

    @Override
    public Set<Map.Entry<RegistryKey<T>, T>> getEntrySet() {
        return this.getForgeRegistry().getEntries();
    }

    @Override
    public Set<RegistryKey<T>> getKeys() {
        return this.getForgeRegistry().getEntries().stream()
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
    }

    @Override
    public Optional<RegistryEntry.Reference<T>> getRandom(Random random) {
        if (this.size() == 0) {
            return Optional.empty();
        }

        Collection<T> values = this.getForgeRegistry().getValues();
        int index = random.nextInt(values.size());
        return Optional.of(RegistryEntry.Reference.intrusive(
            this.wrapper,
            values.stream().skip(index).findAny().orElseThrow()
        ));
    }

    @Override
    public boolean containsId(Identifier id) {
        return this.getForgeRegistry().containsKey(id);
    }

    @Override
    public boolean contains(RegistryKey<T> key) {
        return this.getForgeRegistry().containsKey(key.getValue());
    }

    @Override
    public Registry<T> freeze() {
        return this;
    }

    @Override
    public RegistryEntry.Reference<T> createEntry(T value) {
        return RegistryEntry.Reference.intrusive(this.wrapper, value);
    }

    @Override
    public Optional<RegistryEntry.Reference<T>> getEntry(int rawId) {
        throw new UnsupportedOperationException("Forge registries do not have raw IDs associated with its entries");
    }

    @Override
    public Optional<RegistryEntry.Reference<T>> getEntry(RegistryKey<T> key) {
        T value = this.get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(this.createEntry(value));
    }

    @Override
    public RegistryEntry<T> getEntry(T value) {
        return this.createEntry(value);
    }

    @Override
    public Stream<RegistryEntry.Reference<T>> streamEntries() {
        return this.getForgeRegistry().getEntries().stream()
            .map(entry -> this.createEntry(entry.getValue()));
    }

    @Override
    public Optional<RegistryEntryList.Named<T>> getEntryList(TagKey<T> tag) {
        return Optional.of(RegistryEntryList.of(this.wrapper, tag));
    }

    @Override
    public RegistryEntryList.Named<T> getOrCreateEntryList(TagKey<T> tag) {
        return RegistryEntryList.of(this.wrapper, tag);
    }

    @Override
    public Stream<Pair<TagKey<T>, RegistryEntryList.Named<T>>> streamTagsAndEntries() {
        return Objects.requireNonNull(this.getForgeRegistry().tags())
            .getTagNames()
            .map(tag -> Pair.of(tag, this.getEntryList(tag).orElseThrow()));
    }

    @Override
    public Stream<TagKey<T>> streamTags() {
        return Objects.requireNonNull(this.getForgeRegistry().tags()).getTagNames();
    }

    @Override
    public void clearTags() {
    }

    @Override
    public void populateTags(Map<TagKey<T>, List<RegistryEntry<T>>> tagEntries) {
    }

    @Override
    public RegistryEntryOwner<T> getEntryOwner() {
        return this.wrapper;
    }

    @Override
    public RegistryWrapper.Impl<T> getReadOnlyWrapper() {
        return this.wrapper;
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return this.getForgeRegistry().getValues().iterator();
    }
}
*///?}