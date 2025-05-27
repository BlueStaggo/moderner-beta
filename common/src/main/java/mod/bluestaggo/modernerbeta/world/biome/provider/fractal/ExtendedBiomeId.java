package mod.bluestaggo.modernerbeta.world.biome.provider.fractal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.*;
import java.util.stream.Collectors;

public record ExtendedBiomeId(Identifier baseId, String ext, boolean weak) {
    public static final Codec<ExtendedBiomeId> CODEC = Codec.STRING.comapFlatMap(ExtendedBiomeId::validate, ExtendedBiomeId::toString);

    public static final String TRANSLATION_KEY = "createWorld.customize.modern_beta.settings.preview.extended_biome_id";

    public static final ExtendedBiomeId
        NULL = of(BiomeKeys.THE_VOID, "null"),
        OCEAN = of(BiomeKeys.OCEAN),
        DEEP_OCEAN = of(BiomeKeys.DEEP_OCEAN),
        PLAINS = of(BiomeKeys.PLAINS),
        RIVER = of(BiomeKeys.RIVER),
        FROZEN_OCEAN = of(BiomeKeys.FROZEN_OCEAN),
        DEEP_FROZEN_OCEAN = of(BiomeKeys.DEEP_FROZEN_OCEAN),
        SNOWY_PLAINS = of(BiomeKeys.SNOWY_PLAINS),
        FROZEN_RIVER = of(BiomeKeys.FROZEN_RIVER),
        BEACH = of(BiomeKeys.BEACH),
        STONY_SHORE = of(BiomeKeys.STONY_SHORE),
        MUSHROOM_ISLAND = of(BiomeKeys.MUSHROOM_FIELDS),
        MUSHROOM_SHORE = of(BiomeKeys.MUSHROOM_FIELDS, "shore"),
        RIVER_REGION_A = ExtendedBiomeId.RIVER.withExt("region_a"),
        RIVER_REGION_B = ExtendedBiomeId.RIVER.withExt("region_b");

    public static ExtendedBiomeId of(String id) {
        return validate(id).getOrThrow();
    }

    public static ExtendedBiomeId of(String baseId, String ext) {
        boolean weak = false;
        if (baseId.startsWith("~")) {
            weak = true;
            ext = "";
            baseId = baseId.substring(1);
        }
        return new ExtendedBiomeId(Identifier.of(baseId), ext, weak);
    }

    public static ExtendedBiomeId of(Identifier baseId) {
        return new ExtendedBiomeId(baseId, "", false);
    }

    public static ExtendedBiomeId of(Identifier baseId, String ext) {
        if (ext == null) {
            ext = "";
        }
        return new ExtendedBiomeId(baseId, ext, false);
    }

    public static ExtendedBiomeId ofWeak(Identifier baseId) {
        return new ExtendedBiomeId(baseId, "", true);
    }

    public static ExtendedBiomeId of(RegistryKey<Biome> baseId) {
        return new ExtendedBiomeId(baseId.getValue(), "", false);
    }

    public static ExtendedBiomeId of(RegistryKey<Biome> baseId, String ext) {
        if (ext == null) {
            ext = "";
        }
        return new ExtendedBiomeId(baseId.getValue(), ext, false);
    }

    public static ExtendedBiomeId ofWeak(RegistryKey<Biome> baseId) {
        return new ExtendedBiomeId(baseId.getValue(), "", true);
    }

    public static List<ExtendedBiomeId> listOf(String... ids) {
        return Arrays.stream(ids).map(ExtendedBiomeId::of).toList();
    }

    public static Set<ExtendedBiomeId> setOf(String... ids) {
        return Arrays.stream(ids).map(ExtendedBiomeId::of).collect(Collectors.toSet());
    }

    public ExtendedBiomeId withExt(String ext) {
        if (ext == null) {
            ext = "";
        }
        return new ExtendedBiomeId(this.baseId, ext, false);
    }

    public ExtendedBiomeId setWeak() {
        return new ExtendedBiomeId(this.baseId, "", true);
    }

    public ExtendedBiomeId setStrong() {
        return new ExtendedBiomeId(this.baseId, this.ext, false);
    }

    public boolean isOf(RegistryKey<Biome> biome) {
        return this.baseId.equals(biome.getValue());
    }

    public boolean isOf(Identifier biome) {
        return this.baseId.equals(biome);
    }

    public Map.Entry<ExtendedBiomeId, ExtendedBiomeId> mapTo(String id) {
        ExtendedBiomeId next;
        if (!id.isEmpty() && id.charAt(0) == '*') {
            next = this.withExt(id.substring(1));
        } else {
            next = ExtendedBiomeId.of(id);
        }

        return Map.entry(this, next);
    }

    @Override
    public String toString() {
        String name = this.baseId.toString();
        if (this.ext != null && !this.ext.isEmpty()) {
            name += "*" + this.ext;
        }
        if (this.weak) {
            name = "~" + name;
        }
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ExtendedBiomeId that = (ExtendedBiomeId) o;
        return Objects.equals(this.baseId, that.baseId)
            && (Objects.equals(this.ext, that.ext) || this.weak || that.weak);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.baseId, this.ext, this.weak);
    }

    public static DataResult<ExtendedBiomeId> validate(String string) {
        boolean weak = !string.isEmpty() && string.charAt(0) == '~';
        if (weak) {
            string = string.substring(1);
        }

        int asterisk = string.indexOf('*');
        String ext = asterisk == -1 ? "" : string.substring(asterisk + 1);
        if (asterisk != -1) {
            string = string.substring(0, asterisk);
        }

        return Identifier.validate(string).flatMap(id -> DataResult.success(new ExtendedBiomeId(id, ext, weak)));
    }
}
