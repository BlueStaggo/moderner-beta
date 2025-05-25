package mod.bluestaggo.modernerbeta.world.biome.provider.fractal;

import com.google.gson.*;
import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.api.registry.ModernBetaBuiltInRegistries;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class ConfiguredLayers {
    public static final Codec<ConfiguredLayers> CODEC = Layer.TYPE_CODEC.listOf().xmap(ConfiguredLayers::new, configuredLayers -> configuredLayers.layers);

    private final List<Layer> layers;
    private final Layer finalLayer;

    public ConfiguredLayers(List<Layer> layers) {
        this.layers = layers;
        this.finalLayer = layers.get(layers.size() - 1);

        Map<String, Layer> layerMap = new HashMap<>();
        AtomicInteger index = new AtomicInteger();
        Function<String, Layer> layerMapAccessor = id -> {
            Layer layer = layerMap.get(id);
            if (layer != null) {
                return layer;
            }
            throw new IllegalArgumentException("Layer of id \"" + id + "\" not present at layer at index \"" + index.get() + "\"");
        };

        for (Layer layer : layers) {
            layer.configure(layerMapAccessor);
            layerMap.put(layer.id, layer);
            index.getAndIncrement();
        }
    }

    public Layer getFinalLayer() {
        return this.finalLayer;
    }

    public enum JsonSerializer implements com.google.gson.JsonSerializer<ConfiguredLayers> {
        INSTANCE;

        @Override
        public JsonElement serialize(ConfiguredLayers configuredLayers, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonArray jsonArray = new JsonArray();
            for (Layer layer : configuredLayers.layers) {
                JsonObject jsonLayer = jsonSerializationContext.serialize(layer).getAsJsonObject();
                jsonLayer.addProperty("type", ModernBetaBuiltInRegistries.FRACTAL_LAYER.getKey(layer.getType()));
                jsonArray.add(jsonLayer);
            }
            return jsonArray;
        }
    }

    public enum JsonDeserializer implements com.google.gson.JsonDeserializer<ConfiguredLayers> {
        INSTANCE;

        @Override
        public ConfiguredLayers deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            List<Layer> layers = new ArrayList<>();
            for (JsonElement subElement : jsonElement.getAsJsonArray()) {
                JsonObject object = subElement.getAsJsonObject();
                String layerTypeId = object.get("type").getAsString();
                LayerType<?> layerType = ModernBetaBuiltInRegistries.FRACTAL_LAYER.get(layerTypeId);
                Layer layer = jsonDeserializationContext.deserialize(object, layerType.layerClass());
                layers.add(layer);
            }
            return new ConfiguredLayers(layers);
        }
    }
}
