package mod.bluestaggo.modernerbeta.util;

import com.mojang.serialization.Codec;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CodecUtil {
    public static <T> Codec<Set<T>> set(Codec<T> elementType) {
        return elementType.listOf().xmap(HashSet::new, ArrayList::new);
    }
}
