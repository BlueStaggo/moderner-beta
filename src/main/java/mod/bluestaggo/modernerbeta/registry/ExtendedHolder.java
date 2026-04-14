//~dotLocation
package mod.bluestaggo.modernerbeta.registry;

import com.mojang.datafixers.util.Either;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public record ExtendedHolder<T>(Holder<T> base, String ext, boolean weak) implements Holder<T> {
    @Override
    public T value() {
        return this.base.value();
    }

    @Override
    public boolean isBound() {
        return this.base.isBound();
    }

    //? if >=26.1 {
    /*@Override
    public boolean areComponentsBound() {
        return this.base.areComponentsBound();
    }
    *///? }

    @Override
    public boolean is(ResourceLocation key) {
        return this.base.is(key);
    }

    @Override
    public boolean is(ResourceKey<T> key) {
        return this.base.is(key);
    }

    @Override
    public boolean is(Predicate<ResourceKey<T>> predicate) {
        return this.base.is(predicate);
    }

    @Override
    public boolean is(TagKey<T> tag) {
        return this.base.is(tag);
    }

    //? if >=1.21 {
    @SuppressWarnings("deprecation")
    @Override
    public boolean is(Holder<T> holder) {
        return this.base.is(holder);
    }
    //? }

    @SuppressWarnings("deprecation")
    public boolean is(ExtendedHolder<T> holder) {
        return this.base.is(holder.base) && (this.weak || holder.weak || Objects.equals(this.ext, holder.ext));
    }

    @Override
    public Stream<TagKey<T>> tags() {
        return this.base.tags();
    }

    //? if >=26.1 {
    /*@Override
    public net.minecraft.core.component.DataComponentMap components() {
        return this.base.components();
    }
    *///? }

    @Override
    public Either<ResourceKey<T>, T> unwrap() {
        return this.base.unwrap();
    }

    @Override
    public Optional<ResourceKey<T>> unwrapKey() {
        return this.base.unwrapKey();
    }

    public Optional<ExtendedIdentifier> unwrapExtendedKey() {
        Optional<ResourceKey<T>> optional = this.base.unwrapKey();
        return optional.map(key ->
                new ExtendedIdentifier(key.location(), this.ext, this.weak));
    }

    @Override
    public Kind kind() {
        return this.base.kind();
    }

    @Override
    public boolean canSerializeIn(HolderOwner<T> registry) {
        return this.base.canSerializeIn(registry);
    }

    @Override
    public int hashCode() {
        return this.base.hashCode();
    }
}
