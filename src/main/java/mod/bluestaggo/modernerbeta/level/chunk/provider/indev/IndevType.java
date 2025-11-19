package mod.bluestaggo.modernerbeta.level.chunk.provider.indev;

import net.minecraft.util.StringRepresentable;

public enum IndevType implements StringRepresentable {
    ISLAND("island"),
    FLOATING("floating"),
    INLAND("inland"),
    CLASSIC("classic");
    
    private final String id;
    
    IndevType(String id) {
        this.id = id;
    }

    @Override
    public String getSerializedName() {
        return this.id;
    }
}