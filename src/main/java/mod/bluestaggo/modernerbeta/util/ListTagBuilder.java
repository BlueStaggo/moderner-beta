package mod.bluestaggo.modernerbeta.util;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class ListTagBuilder {
    private final ListTag list;
    private int index;
    
    public ListTagBuilder() {
        this.list = new ListTag();
        this.index = 0;
    }
    
    public ListTagBuilder add(Tag element) {
        this.list.add(this.index, element);
        index++;
        
        return this;
    }
    
    public ListTag build() {
        return this.list;
    }
}
