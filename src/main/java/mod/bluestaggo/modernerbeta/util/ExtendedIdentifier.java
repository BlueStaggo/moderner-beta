//~dotLocation
package mod.bluestaggo.modernerbeta.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public record ExtendedIdentifier(Identifier baseId, String ext, boolean weak) {
    public static final Codec<ExtendedIdentifier> CODEC = Codec.STRING.comapFlatMap(ExtendedIdentifier::validate, ExtendedIdentifier::toString);

    public static ExtendedIdentifier of(String id) {
        return VersionCompat.getOrThrow(validate(id));
    }

    public static ExtendedIdentifier of(String baseId, String ext) {
        boolean weak = false;
        if (baseId.startsWith("~")) {
            weak = true;
            ext = "";
            baseId = baseId.substring(1);
        }
        return new ExtendedIdentifier(VersionCompat.id(baseId), ext, weak);
    }

    public static ExtendedIdentifier of(Identifier baseId) {
        return new ExtendedIdentifier(baseId, "", false);
    }

    public static ExtendedIdentifier of(Identifier baseId, String ext) {
        if (ext == null) {
            ext = "";
        }
        return new ExtendedIdentifier(baseId, ext, false);
    }

    public static ExtendedIdentifier ofWeak(Identifier baseId) {
        return new ExtendedIdentifier(baseId, "", true);
    }

    public static <T> ExtendedIdentifier of(ResourceKey<T> baseId) {
        return new ExtendedIdentifier(baseId.identifier(), "", false);
    }

    public static <T> ExtendedIdentifier of(ResourceKey<T> baseId, String ext) {
        if (ext == null) {
            ext = "";
        }
        return new ExtendedIdentifier(baseId.identifier(), ext, false);
    }

    public static <T> ExtendedIdentifier ofWeak(ResourceKey<T> baseId) {
        return new ExtendedIdentifier(baseId.identifier(), "", true);
    }

    public static List<ExtendedIdentifier> listOf(String... ids) {
        return Arrays.stream(ids).map(ExtendedIdentifier::of).toList();
    }

    public static Set<ExtendedIdentifier> setOf(String... ids) {
        return Arrays.stream(ids).map(ExtendedIdentifier::of).collect(Collectors.toSet());
    }

    public ExtendedIdentifier withExt(String ext) {
        if (ext == null) {
            ext = "";
        }
        return new ExtendedIdentifier(this.baseId, ext, false);
    }

    public ExtendedIdentifier asWeak() {
        return new ExtendedIdentifier(this.baseId, "", true);
    }

    public ExtendedIdentifier asStrong() {
        return new ExtendedIdentifier(this.baseId, this.ext, false);
    }

    public <T> boolean isOf(ResourceKey<T> key) {
        return this.baseId.equals(key.identifier());
    }

    public boolean isOf(Identifier id) {
        return this.baseId.equals(id);
    }

    public Map.Entry<ExtendedIdentifier, ExtendedIdentifier> mapTo(String id) {
        ExtendedIdentifier next;
        if (!id.isEmpty() && id.charAt(0) == '*') {
            next = this.withExt(id.substring(1));
        } else {
            next = ExtendedIdentifier.of(id);
        }

        return Map.entry(this, next);
    }

    @Override
    public @NotNull String toString() {
        String name = this.baseId.toString();
        if (this.ext != null && !this.ext.isEmpty()) {
            name += "*" + this.ext;
        }
        if (this.weak) {
            name = "~" + name;
        }
        return name;
    }

    // I know this looks cursed, but this is all to get weak IDs working

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ExtendedIdentifier that = (ExtendedIdentifier) o;
        return Objects.equals(this.baseId, that.baseId)
                && (this.weak || that.weak || Objects.equals(this.ext, that.ext));
    }

    @Override
    public int hashCode() {
        return this.baseId.hashCode();
    }

    public static DataResult<ExtendedIdentifier> validate(String string) {
        boolean weak = !string.isEmpty() && string.charAt(0) == '~';
        if (weak) {
            string = string.substring(1);
        }

        int asterisk = string.indexOf('*');
        String ext = asterisk == -1 ? "" : string.substring(asterisk + 1);
        if (asterisk != -1) {
            string = string.substring(0, asterisk);
        }

        return Identifier.read(string).flatMap(id -> DataResult.success(new ExtendedIdentifier(id, ext, weak)));
    }
}
