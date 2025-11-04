package mod.bluestaggo.modernerbeta.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;

public class CompoundTagBuilder {
    private final CompoundTag compound;
    
    public CompoundTagBuilder() {
        this.compound = new CompoundTag();
    }
    
    public CompoundTagBuilder(CompoundTag initial) {
        this.compound = initial.copy();
    }

    public CompoundTagBuilder putResourceLocation(String key, ResourceLocation value) {
        this.compound.putString(key, value.toString());

        return this;
    }
    
    public CompoundTagBuilder putString(String key, String value) {
        this.compound.putString(key, value);
        
        return this;
    }
    
    public CompoundTagBuilder putInt(String key, int value) {
        this.compound.putInt(key, value);
        
        return this;
    }
    
    public CompoundTagBuilder putBoolean(String key, boolean value) {
        this.compound.putBoolean(key, value);
        
        return this;
    }
    
    public CompoundTagBuilder putFloat(String key, float value) {
        this.compound.putFloat(key, value);
        
        return this;
    }
    
    public CompoundTagBuilder putDouble(String key, double value) {
        this.compound.putDouble(key, value);
        
        return this;
    }
    
    public CompoundTagBuilder putList(String key, ListTag list) {
        this.compound.put(key, list);
        
        return this;
    }
    
    public CompoundTagBuilder putCompound(String key, CompoundTag compound) {
        this.compound.put(key, compound);
        
        return this;
    }
    
    public CompoundTagBuilder putBuilder(CompoundTagBuilder builder) {
        builder.compound./*? >=1.21.5 {*/keySet/*?} else {*//*getAllKeys*//*?}*/()
                .forEach(key -> this.compound.put(key, compound.get(key)));
        
        return this;
    }
    
    public CompoundTag build() {
        return this.compound;
    }
}
