//~dotLocation
package mod.bluestaggo.modernerbeta.registry;

import com.mojang.datafixers.util.Either;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public record ExtendedHolder<T>(Holder<T> base, String ext, boolean weak) implements Holder<T> {
    public ExtendedHolder(Holder<T> base) {
        this(base, "", false);
    }

    public ExtendedHolder(Holder<T> base, ExtendedIdentifier identifier) {
        this(base, identifier.ext(), identifier.weak());
    }

    public ExtendedHolder {
        Objects.requireNonNull(base);
        ext = ext == null ? "" : ext;
    }

    public ExtendedIdentifier extendedId() {
        return this.unwrapKey()
            .map(key -> new ExtendedIdentifier(key.identifier(), this.ext, this.weak))
            .orElseThrow();
    }

    public boolean is(ExtendedIdentifier identifier) {
        return this.base.is(identifier.baseId())
            && (this.weak || identifier.weak() || this.ext.equals(identifier.ext()));
    }

    public boolean is(ExtendedHolder<T> holder) {
        return this.hasSameBase(holder.base)
            && (this.weak || holder.weak || this.ext.equals(holder.ext));
    }

    public ExtendedHolder<T> withExt(String ext) {
        return new ExtendedHolder<>(this.base, ext, false);
    }

    @Override
    public T value() {
        return this.base.value();
    }

    @Override
    public boolean isBound() {
        return this.base.isBound();
    }

    //? if >=26.1 {
    @Override
    public boolean areComponentsBound() {
        return this.base.areComponentsBound();
    }
    //? }

    @Override
    public boolean is(Identifier key) {
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
        if (holder instanceof ExtendedHolder<?> extendedHolder) {
            return this.hasSameBase(extendedHolder.base);
        }
        return this.base.is(holder);
    }
    //? }

    @Override
    public Stream<TagKey<T>> tags() {
        return this.base.tags();
    }

    //? if >=26.1 {
    @Override
    public net.minecraft.core.component.DataComponentMap components() {
        return this.base.components();
    }
    //? }

    @Override
    public Either<ResourceKey<T>, T> unwrap() {
        return this.base.unwrap();
    }

    @Override
    public Optional<ResourceKey<T>> unwrapKey() {
        return this.base.unwrapKey();
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
    public String toString() {
        return this.unwrapKey()
            .map(key -> new ExtendedIdentifier(key.identifier(), this.ext, this.weak).toString())
            .orElseGet(this.base::toString);
    }

    private boolean hasSameBase(Holder<?> holder) {
        Optional<?> key = this.base.unwrapKey();
        return key.isPresent() ? key.equals(holder.unwrapKey()) : this.base == holder;
    }
}
