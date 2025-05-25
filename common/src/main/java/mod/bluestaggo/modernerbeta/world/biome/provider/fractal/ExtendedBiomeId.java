package mod.bluestaggo.modernerbeta.world.biome.provider.fractal;

import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public record ExtendedBiomeId(Identifier baseId, String ext) {
    public static final Codec<ExtendedBiomeId> CODEC = Codec.STRING.comapFlatMap(ExtendedBiomeId::validate, ExtendedBiomeId::toString);

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

    public static List<ExtendedBiomeId> listOf(String... ids) {
        return Arrays.stream(ids).map(ExtendedBiomeId::of).toList();
    }

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

    public enum JsonSerializer implements com.google.gson.JsonSerializer<ExtendedBiomeId> {
        INSTANCE;

        @Override
        public JsonElement serialize(ExtendedBiomeId extendedBiomeId, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(extendedBiomeId.toString());
        }
    }

    public enum JsonDeserializer implements com.google.gson.JsonDeserializer<ExtendedBiomeId> {
        INSTANCE;

        @Override
        public ExtendedBiomeId deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return ExtendedBiomeId.of(jsonElement.getAsString());
        }
    }
}
