package mod.bluestaggo.modernerbeta.world.chunk.provider.indev;

import net.minecraft.util.StringRepresentable;

public enum IndevType implements StringRepresentable {
    ISLAND("island"),
    FLOATING("floating"),
    INLAND("inland");
    
    private final String id;
    
    IndevType(String id) {
        this.id = id;
    }

    @Override
    public String getSerializedName() {
        return this.id;
    }
}