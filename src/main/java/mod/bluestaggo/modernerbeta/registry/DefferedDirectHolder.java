package mod.bluestaggo.modernerbeta.registry;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DefferedDirectHolder<T> implements Holder<T> {
    private final Supplier<T> supplier;
    private T value;
    //? if >=26.1 {
    /*private final Supplier<net.minecraft.core.component.DataComponentMap> componentSupplier;
    private net.minecraft.core.component.DataComponentMap components;
    *///? }

    private DefferedDirectHolder(
        Supplier<T> supplier
        //? >=26.1
        //, Supplier<net.minecraft.core.component.DataComponentMap> componentSupplier
    ) {
        this.supplier = supplier;
        //? >=26.1
        //this.componentSupplier = componentSupplier;
    }

    public static <T> DefferedDirectHolder<T> of(Supplier<T> supplier) {
        return new DefferedDirectHolder<>(supplier /*? >=26.1 {*//*, () -> net.minecraft.core.component.DataComponentMap.EMPTY*//*?}*/);
    }

    //? if >=26.1 {
    /*public static <T> DefferedDirectHolder<T> of(
        Supplier<T> supplier,
        Supplier<net.minecraft.core.component.DataComponentMap> components
    ) {
        return new DefferedDirectHolder<>(supplier, components);
    }
    *///? }

    private void initIfNull() {
        if (value == null) {
            value = supplier.get();
        }
    }

    @Override
    public T value() {
        initIfNull();
        return value;
    }

    @Override
    public boolean isBound() {
        return true;
    }

    //? if >=26.1 {
    /*@Override
    public boolean areComponentsBound() {
        return true;
    }
    *///? }

    @Override
    public boolean is(ResourceLocation location) {
        return false;
    }

    @Override
    public boolean is(ResourceKey<T> resourceKey) {
        return false;
    }

    @Override
    public boolean is(Predicate<ResourceKey<T>> predicate) {
        return false;
    }

    @Override
    public boolean is(TagKey<T> tagKey) {
        return false;
    }

    //? if >=1.21 {
    @Override
    public boolean is(Holder<T> holder) {
        initIfNull();
        return value.equals(holder.value());
    }
    //? }

    @Override
    public Stream<TagKey<T>> tags() {
        return Stream.empty();
    }

    //? if >=26.1 {
    /*@Override
    public net.minecraft.core.component.DataComponentMap components() {
        if (components == null) {
            components = componentSupplier.get();
        }

        return components;
    }
    *///? }

    @Override
    public Either<ResourceKey<T>, T> unwrap() {
        initIfNull();
        return Either.right(value);
    }

    @Override
    public Optional<ResourceKey<T>> unwrapKey() {
        return Optional.empty();
    }

    @Override
    public Kind kind() {
        return Kind.DIRECT;
    }

    @Override
    public boolean canSerializeIn(HolderOwner<T> owner) {
        return true;
    }
}
