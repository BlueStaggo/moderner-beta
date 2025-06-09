package mod.bluestaggo.modernerbeta.world.biome.provider.fractal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record ExtendedBiomeId(Identifier baseId, String ext, boolean weak) {
    public static final Codec<ExtendedBiomeId> CODEC = Codec.STRING.comapFlatMap(ExtendedBiomeId::validate, ExtendedBiomeId::toString);

    public static final String TRANSLATION_KEY = "createWorld.customize.modern_beta.settings.preview.extended_biome_id";

    public static final ExtendedBiomeId
        OCEAN = of(BiomeKeys.OCEAN),
        DEEP_OCEAN = of(BiomeKeys.DEEP_OCEAN),
        PLAINS = of(BiomeKeys.PLAINS),
        RIVER = of(BiomeKeys.RIVER),
        FROZEN_OCEAN = of(BiomeKeys.FROZEN_OCEAN),
        SNOWY_PLAINS = of(BiomeKeys.SNOWY_PLAINS),
        FROZEN_RIVER = of(BiomeKeys.FROZEN_RIVER),
        BEACH = of(BiomeKeys.BEACH),
        MUSHROOM_ISLAND = of(BiomeKeys.MUSHROOM_FIELDS),
        MUSHROOM_SHORE = of(BiomeKeys.MUSHROOM_FIELDS, "shore"),
        CLIMATE_WARM = of(BiomeKeys.DESERT, "climate"),
        CLIMATE_TEMPERATE = of(BiomeKeys.PLAINS, "climate"),
        CLIMATE_COOL = of(BiomeKeys.TAIGA, "climate"),
        CLIMATE_SNOWY = of(BiomeKeys.SNOWY_PLAINS, "climate"),
        RIVER_REGION_A = ExtendedBiomeId.RIVER.withExt("region_a"),
        RIVER_REGION_B = ExtendedBiomeId.RIVER.withExt("region_b"),
        RANDOM = of(BiomeKeys.THE_VOID, "mutation"),
        WARM_OCEAN = of(BiomeKeys.WARM_OCEAN),
        LUKEWARM_OCEAN = of(BiomeKeys.LUKEWARM_OCEAN),
        COLD_OCEAN = of(BiomeKeys.COLD_OCEAN),
        DEEP_LUKEWARM_OCEAN = of(BiomeKeys.DEEP_LUKEWARM_OCEAN),
        DEEP_COLD_OCEAN = of(BiomeKeys.DEEP_COLD_OCEAN),
        DEEP_FROZEN_OCEAN = of(BiomeKeys.DEEP_FROZEN_OCEAN),
        NULL = of(BiomeKeys.THE_VOID, "null");
    public static final List<ExtendedBiomeId>
        CLIMATE_WARM_RARE = rareClimate(BiomeKeys.BADLANDS),
        CLIMATE_TEMPERATE_RARE = rareClimate(BiomeKeys.JUNGLE),
        CLIMATE_COOL_RARE = rareClimate(BiomeKeys.OLD_GROWTH_PINE_TAIGA),
        CLIMATE_SNOWY_RARE = rareClimate(BiomeKeys.ICE_SPIKES);

    public static ExtendedBiomeId of(String id) {
        return VersionCompat.getOrThrow(validate(id));
    }

    public static ExtendedBiomeId of(String baseId, String ext) {
        boolean weak = false;
        if (baseId.startsWith("~")) {
            weak = true;
            ext = "";
            baseId = baseId.substring(1);
        }
        return new ExtendedBiomeId(VersionCompat.id(baseId), ext, weak);
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

    private static List<ExtendedBiomeId> rareClimate(RegistryKey<Biome> baseId) {
        return IntStream.range(0, 15)
            .mapToObj(i -> ExtendedBiomeId.of(baseId, "climate_" + i))
            .toList();
    }

    public ExtendedBiomeId withExt(String ext) {
        if (ext == null) {
            ext = "";
        }
        return new ExtendedBiomeId(this.baseId, ext, false);
    }

    public ExtendedBiomeId asWeak() {
        return new ExtendedBiomeId(this.baseId, "", true);
    }

    public ExtendedBiomeId asStrong() {
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
        ExtendedBiomeId that = (ExtendedBiomeId) o;
        return Objects.equals(this.baseId, that.baseId)
            && (this.weak || that.weak || Objects.equals(this.ext, that.ext));
    }

    @Override
    public int hashCode() {
        return this.baseId.hashCode();
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
