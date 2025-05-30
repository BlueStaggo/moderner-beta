package mod.bluestaggo.modernerbeta.world.chunk.provider.indev;

import net.minecraft.util.StringIdentifiable;

public enum IndevTheme implements StringIdentifiable {
    NORMAL("normal"),
    HELL("hell"),
    PARADISE("paradise"),
    WOODS("woods");
    
    private final String id;
    
    IndevTheme(String id) {
        this.id = id;
    }

    @Override
    public String asString() {
        return this.id;
    }
}