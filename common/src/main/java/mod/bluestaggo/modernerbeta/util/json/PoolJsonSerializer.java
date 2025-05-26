package mod.bluestaggo.modernerbeta.util.json;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.collection.Weighted;

import java.lang.reflect.Type;
import java.util.List;

public class PoolJsonSerializer<T> implements JsonSerializer<Pool<T>> {
    @Override
    public JsonElement serialize(Pool<T> src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.getEntries(), new TypeToken<List<Weighted<T>>>() {}.getRawType());
    }
}
