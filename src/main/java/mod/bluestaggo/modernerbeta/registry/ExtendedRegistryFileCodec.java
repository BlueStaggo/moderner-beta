package mod.bluestaggo.modernerbeta.registry;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;

public class ExtendedRegistryFileCodec<E> implements Codec<ExtendedHolder<E>> {
    private final ResourceKey<? extends Registry<E>> registryKey;
    private final Codec<E> elementCodec;

    public static <E> ExtendedRegistryFileCodec<E> create(ResourceKey<? extends Registry<E>> registryKey, Codec<E> elementCodec) {
        return new ExtendedRegistryFileCodec<>(registryKey, elementCodec);
    }

    private ExtendedRegistryFileCodec(ResourceKey<? extends Registry<E>> registryKey, Codec<E> elementCodec) {
        this.registryKey = registryKey;
        this.elementCodec = elementCodec;
    }

    public <T> DataResult<T> encode(ExtendedHolder<E> input, DynamicOps<T> ops, T prefix) {
        if (!(ops instanceof RegistryOps<?> registryOps)) {
            return DataResult.error(() -> "No registry context available");
        }

        Optional<HolderOwner<E>> maybeOwner = registryOps.owner(this.registryKey);
        if (maybeOwner.isEmpty()) {
            return DataResult.error(() -> "Registry does not exist: " + this.registryKey);
        }

        if (!input.canSerializeIn(maybeOwner.get())) {
            return DataResult.error(() -> "Element " + input + " is not valid in current registry set");
        }

        return input.unwrapExtendedKey()
                .map(id -> ExtendedIdentifier.CODEC.encode(id, ops, prefix))
                .orElseGet(() -> DataResult.error(() -> "Holder has no key."));
    }

    @Override
    public <T> DataResult<Pair<ExtendedHolder<E>, T>> decode(DynamicOps<T> ops, T input) {
        if (!(ops instanceof RegistryOps<?> registryOps)) {
            return DataResult.error(() -> "No registry context available");
        }

        Optional<HolderGetter<E>> maybeLookup = registryOps.getter(this.registryKey);
        if (maybeLookup.isEmpty()) {
            return DataResult.error(() -> "Registry does not exist: " + this.registryKey);
        }

        HolderGetter<E> lookup = maybeLookup.get();
        DataResult<Pair<ExtendedIdentifier, T>> decoded = ExtendedIdentifier.CODEC.decode(ops, input);
        if (decoded.result().isEmpty()) {
            return DataResult.error(() -> "Value is not extended location");
        }

        Pair<ExtendedIdentifier, T> pair = decoded.result().get();
        ExtendedIdentifier id = pair.getFirst();

        ResourceKey<E> elementKey = ResourceKey.create(this.registryKey, id.baseId());
        return (lookup.get(elementKey)
            .map(ref -> new ExtendedHolder<>(ref, id.ext(), id.weak()))
            .map(DataResult::success)
            .orElseGet(() -> DataResult.error(() -> "Failed to get element " + elementKey)))
            .map(h -> Pair.of(h, pair.getSecond()))
            .setLifecycle(Lifecycle.stable());
    }

    public String toString() {
        return "ExtendedRegistryFileCodec[" + this.registryKey + " " + this.elementCodec + "]";
    }
}