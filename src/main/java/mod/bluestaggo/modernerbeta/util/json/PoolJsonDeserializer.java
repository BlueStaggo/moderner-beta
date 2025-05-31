package mod.bluestaggo.modernerbeta.util.json;

import com.google.gson.*;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.collection.Weighted;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PoolJsonDeserializer<T> implements JsonDeserializer<Pool<T>> {
    private final Class<T> itemType;

    public PoolJsonDeserializer(Class<T> itemType) {
        this.itemType = itemType;
    }

    @Override
    public Pool<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray jsonArray = json.getAsJsonArray();
        List<Weighted<T>> pool = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            JsonObject weightedObject = jsonElement.getAsJsonObject();
            pool.add(new Weighted<>(
                context.deserialize(weightedObject.get("value"), this.itemType),
                weightedObject.get("weight").getAsInt()
            ));
        }
        return Pool.of(pool);
    }
}
