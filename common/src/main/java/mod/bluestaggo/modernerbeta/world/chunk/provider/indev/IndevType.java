package mod.bluestaggo.modernerbeta.world.chunk.provider.indev;

import net.minecraft.util.StringIdentifiable;

public enum IndevType implements StringIdentifiable {
    ISLAND("island"),
    FLOATING("floating"),
    INLAND("inland");
    
    private final String id;
    
    IndevType(String id) {
        this.id = id;
    }

    @Override
    public String asString() {
        return this.id;
    }
}