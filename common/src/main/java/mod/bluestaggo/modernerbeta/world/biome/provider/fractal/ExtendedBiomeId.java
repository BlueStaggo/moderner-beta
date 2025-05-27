package mod.bluestaggo.modernerbeta.world.biome.provider.fractal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.Arrays;
import java.util.List;

public record ExtendedBiomeId(Identifier baseId, String ext) {
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
        RIVER_REGION_B = ExtendedBiomeId.RIVER.withExt("region_b"),
        RIVER_NULL = ExtendedBiomeId.RIVER.withExt("null");

    public static ExtendedBiomeId of(String id) {
        return validate(id).getOrThrow();
    }

    public static ExtendedBiomeId of(String baseId, String ext) {
        return new ExtendedBiomeId(Identifier.of(baseId), ext);
    }

    public static ExtendedBiomeId of(Identifier baseId) {
        return new ExtendedBiomeId(baseId, "");
    }

    public static ExtendedBiomeId of(Identifier baseId, String ext) {
        if (ext == null) {
            ext = "";
        }
        return new ExtendedBiomeId(baseId, ext);
    }

    public static ExtendedBiomeId of(RegistryKey<Biome> baseId) {
        return new ExtendedBiomeId(baseId.getValue(), "");
    }

    public static ExtendedBiomeId of(RegistryKey<Biome> baseId, String ext) {
        if (ext == null) {
            ext = "";
        }
        return new ExtendedBiomeId(baseId.getValue(), ext);
    }

    public static List<ExtendedBiomeId> listOf(String... ids) {
        return Arrays.stream(ids).map(ExtendedBiomeId::of).toList();
    }

    public ExtendedBiomeId withExt(String ext) {
        return ExtendedBiomeId.of(this.baseId, ext);
    }

    public boolean isOf(RegistryKey<Biome> biome) {
        return this.baseId.equals(biome.getValue());
    }

    public boolean isOf(Identifier biome) {
        return this.baseId.equals(biome);
    }

    @Override
    public String toString() {
        if (this.ext == null || this.ext.isEmpty()) {
            return this.baseId.toString();
        } else {
            return this.baseId.toString() + "*" + this.ext;
        }
    }

    public static DataResult<ExtendedBiomeId> validate(String string) {
        int asterisk = string.indexOf('*');
        String ext = asterisk == -1 ? "" : string.substring(asterisk + 1);

        if (asterisk != -1) {
            string = string.substring(0, asterisk);
        }

        return Identifier.validate(string).flatMap(id -> DataResult.success(new ExtendedBiomeId(id, ext)));
    }
}
