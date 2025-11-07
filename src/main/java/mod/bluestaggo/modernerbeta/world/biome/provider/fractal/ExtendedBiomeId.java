//~dotLocation
package mod.bluestaggo.modernerbeta.world.biome.provider.fractal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record ExtendedBiomeId(ResourceLocation baseId, String ext, boolean weak) {
    public static final Codec<ExtendedBiomeId> CODEC = Codec.STRING.comapFlatMap(ExtendedBiomeId::validate, ExtendedBiomeId::toString);

    public static final String TRANSLATION_KEY = "createWorld.customize.modern_beta.settings.preview.extended_biome_id";

    public static final ExtendedBiomeId
        OCEAN = of(Biomes.OCEAN),
        DEEP_OCEAN = of(Biomes.DEEP_OCEAN),
        PLAINS = of(Biomes.PLAINS),
        RIVER = of(Biomes.RIVER),
        FROZEN_OCEAN = of(Biomes.FROZEN_OCEAN),
        SNOWY_PLAINS = of(Biomes.SNOWY_PLAINS),
        FROZEN_RIVER = of(Biomes.FROZEN_RIVER),
        BEACH = of(Biomes.BEACH),
        MUSHROOM_ISLAND = of(Biomes.MUSHROOM_FIELDS),
        MUSHROOM_SHORE = of(Biomes.MUSHROOM_FIELDS, "shore"),
        CLIMATE_WARM = of(Biomes.DESERT, "climate"),
        CLIMATE_TEMPERATE = of(Biomes.PLAINS, "climate"),
        CLIMATE_COOL = of(Biomes.TAIGA, "climate"),
        CLIMATE_SNOWY = of(Biomes.SNOWY_PLAINS, "climate"),
        RIVER_REGION_A = ExtendedBiomeId.RIVER.withExt("region_a"),
        RIVER_REGION_B = ExtendedBiomeId.RIVER.withExt("region_b"),
        RANDOM = of(Biomes.THE_VOID, "mutation"),
        WARM_OCEAN = of(Biomes.WARM_OCEAN),
        LUKEWARM_OCEAN = of(Biomes.LUKEWARM_OCEAN),
        COLD_OCEAN = of(Biomes.COLD_OCEAN),
        DEEP_LUKEWARM_OCEAN = of(Biomes.DEEP_LUKEWARM_OCEAN),
        DEEP_COLD_OCEAN = of(Biomes.DEEP_COLD_OCEAN),
        DEEP_FROZEN_OCEAN = of(Biomes.DEEP_FROZEN_OCEAN),
        NULL = of(Biomes.THE_VOID, "null");
    public static final List<ExtendedBiomeId>
        CLIMATE_WARM_RARE = rareClimate(Biomes.BADLANDS),
        CLIMATE_TEMPERATE_RARE = rareClimate(Biomes.JUNGLE),
        CLIMATE_COOL_RARE = rareClimate(Biomes.OLD_GROWTH_PINE_TAIGA),
        CLIMATE_SNOWY_RARE = rareClimate(Biomes.ICE_SPIKES);

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

    public static ExtendedBiomeId of(ResourceLocation baseId) {
        return new ExtendedBiomeId(baseId, "", false);
    }

    public static ExtendedBiomeId of(ResourceLocation baseId, String ext) {
        if (ext == null) {
            ext = "";
        }
        return new ExtendedBiomeId(baseId, ext, false);
    }

    public static ExtendedBiomeId ofWeak(ResourceLocation baseId) {
        return new ExtendedBiomeId(baseId, "", true);
    }

    public static ExtendedBiomeId of(ResourceKey<Biome> baseId) {
        return new ExtendedBiomeId(baseId.location(), "", false);
    }

    public static ExtendedBiomeId of(ResourceKey<Biome> baseId, String ext) {
        if (ext == null) {
            ext = "";
        }
        return new ExtendedBiomeId(baseId.location(), ext, false);
    }

    public static ExtendedBiomeId ofWeak(ResourceKey<Biome> baseId) {
        return new ExtendedBiomeId(baseId.location(), "", true);
    }

    public static List<ExtendedBiomeId> listOf(String... ids) {
        return Arrays.stream(ids).map(ExtendedBiomeId::of).toList();
    }

    public static Set<ExtendedBiomeId> setOf(String... ids) {
        return Arrays.stream(ids).map(ExtendedBiomeId::of).collect(Collectors.toSet());
    }

    private static List<ExtendedBiomeId> rareClimate(ResourceKey<Biome> baseId) {
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

    public boolean isOf(ResourceKey<Biome> biome) {
        return this.baseId.equals(biome.location());
    }

    public boolean isOf(ResourceLocation biome) {
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

        return ResourceLocation.read(string).flatMap(id -> DataResult.success(new ExtendedBiomeId(id, ext, weak)));
    }
}
