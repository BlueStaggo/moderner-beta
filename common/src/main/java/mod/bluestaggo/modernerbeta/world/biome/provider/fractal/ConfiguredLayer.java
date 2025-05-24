package mod.bluestaggo.modernerbeta.world.biome.provider.fractal;

import com.google.gson.*;
import mod.bluestaggo.modernerbeta.api.registry.ModernBetaBuiltInRegistries;

import java.lang.reflect.Type;

public record ConfiguredLayer(String id, Layer layer, String parent) {
    public ConfiguredLayer(String id, Layer layer) {
        this(id, layer, "");
    }

    public enum JsonSerializer implements com.google.gson.JsonSerializer<ConfiguredLayer> {
        INSTANCE;

        @Override
        public JsonElement serialize(ConfiguredLayer configuredLayer, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject object = jsonSerializationContext.serialize(configuredLayer.layer).getAsJsonObject();
            object.addProperty("id", configuredLayer.id);
            if (!configuredLayer.parent.isEmpty()) {
                object.addProperty("parent", configuredLayer.parent);
            }
            object.addProperty("type", ModernBetaBuiltInRegistries.FRACTAL_LAYER.getKey(configuredLayer.layer.getType()));
            return object;
        }
    }

    public enum JsonDeserializer implements com.google.gson.JsonDeserializer<ConfiguredLayer> {
        INSTANCE;

        @Override
        public ConfiguredLayer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject object = jsonElement.getAsJsonObject();
            String layerTypeId = object.get("type").getAsString();
            LayerType<?> layerType = ModernBetaBuiltInRegistries.FRACTAL_LAYER.get(layerTypeId);
            Layer layer = jsonDeserializationContext.deserialize(object, layerType.layerClass());

            return new ConfiguredLayer(
                object.get("id").getAsString(),
                layer,
                object.has("parent") ? object.get("parent").getAsString() : ""
            );
        }
    }
}
