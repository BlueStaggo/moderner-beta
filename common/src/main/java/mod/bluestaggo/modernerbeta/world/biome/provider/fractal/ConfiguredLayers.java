package mod.bluestaggo.modernerbeta.world.biome.provider.fractal;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.ListBuilder;

import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ConfiguredLayers {
    public static final Codec<ConfiguredLayers> CODEC = Codec.of(ConfiguredLayers::encode, ConfiguredLayers::decode, "ConfiguredLayersCodec");

    private final List<ConfiguredLayer> configuredLayers;
    private final Layer finalLayer;

    private static <T> DataResult<T> encode(ConfiguredLayers layers, DynamicOps<T> ops, T prefix) {
        ListBuilder<T> listBuilder = ops.listBuilder();
        for (ConfiguredLayer layer : layers.configuredLayers) {
            listBuilder.add(layer, ConfiguredLayers::encodeConfiguredLayer);
        }
        return listBuilder.build(prefix);
    }

    private static <T> DataResult<T> encodeConfiguredLayer(ConfiguredLayer clayer, DynamicOps<T> clops, T clprefix) {
        return Layer.TYPE_CODEC.encode(clayer.layer(), clops, clprefix).flatMap(layer -> {
            var clayerBuilder = clops.mapBuilder()
                .add("id", clops.createString(clayer.id()));
            if (!clayer.parent().isEmpty()) {
                clayerBuilder.add("parent", clops.createString(clayer.parent()));
            }
            return clayerBuilder.build(layer);
        });
    }

    private static <T> DataResult<Pair<ConfiguredLayers, T>> decode(DynamicOps<T> ops, T input) {
        var listResult = ops.getList(input);
        if (listResult.isError()) {
            return DataResult.error(() -> "No layers provided");
        }

        DataResult<List<ConfiguredLayer>> layersResult = listResult.flatMap(stream -> {
            AtomicBoolean abandonDecode = new AtomicBoolean(false);
            AtomicReference<String> errorMessage = new AtomicReference<>();
            Map<String, Layer> layerMap = new HashMap<>();
            List<ConfiguredLayer> layers = new ArrayList<>();

            stream.accept(dynamicLayer -> {
                if (abandonDecode.get()) {
                    return;
                }

                var layerResult = Layer.TYPE_CODEC.decode(ops, dynamicLayer);
                var layerError = layerResult.error();
                if (layerError.isPresent()) {
                    abandonDecode.set(true);
                    errorMessage.set(layerError.get().message());
                    return;
                }

                Layer layer = layerResult.result().orElseThrow().getFirst();

                var parentResult = ops.get(dynamicLayer, "parent").flatMap(ops::getStringValue);

                var idResult = ops.get(dynamicLayer, "id").flatMap(ops::getStringValue);
                var idError = idResult.error();
                if (idError.isPresent()) {
                    abandonDecode.set(true);
                    errorMessage.set(idError.get().message());
                    return;
                }

                String id = idResult.result().orElseThrow();
                layerMap.put(id, layer);
                layers.add(new ConfiguredLayer(id, layer, parentResult.result().orElse("")));
            });

            if (abandonDecode.get()) {
                return DataResult.error(errorMessage::get);
            }
            return DataResult.success(layers);
        });

        return layersResult.flatMap(layers -> {
            if (layers.isEmpty()) {
                return DataResult.error(() -> "No layers provided");
            }
            return DataResult.success(new Pair<>(new ConfiguredLayers(layers), input));
        });
    }

    public ConfiguredLayers(List<ConfiguredLayer> configuredLayers) {
        this.configuredLayers = configuredLayers;
        this.finalLayer = configuredLayers.getLast().layer();

        Map<String, Layer> layerMap = new HashMap<>();
        for (ConfiguredLayer configuredLayer : configuredLayers) {
            if (!configuredLayer.parent().isEmpty()) {
                Layer parent = layerMap.get(configuredLayer.parent());
                if (parent == null) {
                    throw new IllegalArgumentException("Layer of id \"" + configuredLayer.parent() + "\" not yet initialised");
                }
                configuredLayer.layer().parent = parent;
            }
            layerMap.put(configuredLayer.id(), configuredLayer.layer());
        }
    }

    public Layer getFinalLayer() {
        return this.finalLayer;
    }

    public enum JsonSerializer implements com.google.gson.JsonSerializer<ConfiguredLayers> {
        INSTANCE;

        @Override
        public JsonElement serialize(ConfiguredLayers configuredLayers, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonArray array = new JsonArray();
            for (ConfiguredLayer layer : configuredLayers.configuredLayers) {
                array.add(jsonSerializationContext.serialize(layer));
            }
            return array;
        }
    }

    public enum JsonDeserializer implements com.google.gson.JsonDeserializer<ConfiguredLayers> {
        INSTANCE;

        @Override
        public ConfiguredLayers deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            List<ConfiguredLayer> layers = new ArrayList<>();
            for (JsonElement subElement : jsonElement.getAsJsonArray()) {
                layers.add(jsonDeserializationContext.deserialize(subElement, ConfiguredLayer.class));
            }
            return new ConfiguredLayers(layers);
        }
    }
}
