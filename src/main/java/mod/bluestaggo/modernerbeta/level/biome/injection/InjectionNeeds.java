package mod.bluestaggo.modernerbeta.level.biome.injection;

import java.util.EnumSet;

public enum InjectionNeeds {
    BIOMES,
    HEIGHTS;

    public static EnumSet<InjectionNeeds> all() {
        return EnumSet.allOf(InjectionNeeds.class);
    }

    public static EnumSet<InjectionNeeds> cheapToFulfill() {
        return EnumSet.of(BIOMES);
    }
}
