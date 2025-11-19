package mod.bluestaggo.modernerbeta.level.chunk.provider.indev;

import net.minecraft.util.StringRepresentable;

public enum IndevTheme implements StringRepresentable {
    NORMAL("normal"),
    HELL("hell"),
    PARADISE("paradise"),
    WOODS("woods");
    
    private final String id;
    
    IndevTheme(String id) {
        this.id = id;
    }

    @Override
    public String getSerializedName() {
        return this.id;
    }
}