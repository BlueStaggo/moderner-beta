package mod.bluestaggo.modernerbeta.compat;

import mod.bluestaggo.modernerbeta.compat.levelgen.ValkyrienSkiesCompat;
import mod.bluestaggo.modernerbeta.compat.levelgen.LevelGenCompatHelper;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class ModCompat {
    public static Set<LevelGenCompatHelper> worldGenHelpers = new HashSet<>();
    private static boolean compatChecked;

    public static void initialise(Function<String, Boolean> modPresenceFunction) {
        if (compatChecked)
            throw new IllegalStateException("Tried to initialise mod compatibility workarounds twice!");

        if (modPresenceFunction.apply("valkyrienskies"))
            worldGenHelpers.add(new ValkyrienSkiesCompat());

        compatChecked = true;
    }

    public static boolean skipGeneratingChunk(int x, int z) {
        if (worldGenHelpers.isEmpty())
            return false;

        for (LevelGenCompatHelper helper : worldGenHelpers) {
            if (helper.skipGeneratingChunk(x, z))
                return true;
        }

        return false;
    }
}
