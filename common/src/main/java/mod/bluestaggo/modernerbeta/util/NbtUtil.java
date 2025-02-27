package mod.bluestaggo.modernerbeta.util;

import net.minecraft.nbt.*;

public class NbtUtil {
    /*
     * Helper methods for reading primitive values from NbtCompound objects
     */
    
    public static String readStringOrThrow(String key, NbtCompound tag) {
        return tag.getString(key)
                .orElseThrow(() -> new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key));
    }
    
    public static String readString(String key, NbtCompound tag, String alternate) {
        return tag.getString(key).orElse(alternate);
    }
    
    public static int readIntOrThrow(String key, NbtCompound tag) {
        return tag.getInt(key)
                .orElseThrow(() -> new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key));
    }
    
    public static int readInt(String key, NbtCompound tag, int alternate) {
        return tag.getInt(key).orElse(alternate);
    }
    
    public static float readFloatOrThrow(String key, NbtCompound tag) {
        return tag.getFloat(key)
                .orElseThrow(() -> new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key));
    }
    
    public static float readFloat(String key, NbtCompound tag, float alternate) {
        return tag.getFloat(key).orElse(alternate);
    }
    
    public static double readDoubleOrThrow(String key, NbtCompound tag) {
        return tag.getDouble(key)
                .orElseThrow(() -> new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key));
    }
    
    public static double readDouble(String key, NbtCompound tag, double alternate) {
        return tag.getDouble(key).orElse(alternate);
    }
    
    public static boolean readBooleanOrThrow(String key, NbtCompound tag) {
        return tag.getBoolean(key)
                .orElseThrow(() -> new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key));
    }
    
    public static boolean readBoolean(String key, NbtCompound tag, boolean alternate) {
        return tag.getBoolean(key).orElse(alternate);
    }
    
    public static NbtCompound readCompoundOrThrow(String key, NbtCompound tag) {
        return tag.getCompound(key)
                .orElseThrow(() -> new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key));
    }
    
    public static NbtCompound readCompound(String key, NbtCompound tag, NbtCompound alternate) {
        return tag.getCompound(key).orElse(alternate);
    }
    
    public static NbtList readListOrThrow(String key, NbtCompound tag) {
        return tag.getList(key)
                .orElseThrow(() -> new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key));
    }
    
    public static NbtList readList(String key, NbtCompound tag, NbtList alternate) {
        return tag.getList(key).orElse(alternate);
    }
    
    /*
     * Conversion methods for extracting primitive values from NbtElement objects
     */
    
    public static String toStringOrThrow(NbtElement element) {
        return element.method_68658()
                .orElseThrow(() -> new IllegalArgumentException("[Modern Beta] NBT element is not a string! Type:" + element.getType()));
    }
    
    public static String toString(NbtElement element, String alternate) {
        return element.method_68658().orElse(alternate);
    }
    
    public static int toIntOrThrow(NbtElement element) {
        return element.method_68602()
                .orElseThrow(() -> new IllegalArgumentException("[Modern Beta] NBT element is not an int! Type: " + element.getType()));
    }
    
    public static int toInt(NbtElement element, int alternate) {
        return element.method_68602().orElse(alternate);
    }
    
    public static float toFloatOrThrow(NbtElement element) {
        return element.method_68604()
                .orElseThrow(() -> new IllegalArgumentException("[Modern Beta] NBT element is not a float! Type: " + element.getType()));
    }
    
    public static float toFloat(NbtElement element, float alternate) {
        return element.method_68604().orElse(alternate);
    }
    
    public static double toDoubleOrThrow(NbtElement element) {
        return element.method_68605()
                .orElseThrow(() -> new IllegalArgumentException("[Modern Beta] NBT element is not a double! Type: " + element.getType()));
    }
    
    public static double toDouble(NbtElement element, double alternate) {
        return element.method_68605().orElse(alternate);
    }
    
    public static boolean toBooleanOrThrow(NbtElement element) {
        return element.method_68606()
                .orElseThrow(() -> new IllegalArgumentException("[Modern Beta] NBT element is not a boolean! Type: " + element.getType()));
    }
    
    public static boolean toBoolean(NbtElement element, boolean alternate) {
        return element.method_68606().orElse(alternate);
    }
    
    public static NbtCompound toCompoundOrThrow(NbtElement element) {
        return element.method_68571()
                .orElseThrow(() -> new IllegalArgumentException("[Modern Beta] NBT element is not a compound! Type: " + element.getType()));
    }
    
    public static NbtCompound toCompound(NbtElement element, NbtCompound alternate) {
        return element.method_68571().orElse(alternate);
    }
    
    public static NbtList toListOrThrow(NbtElement element) {
        return element.method_68592()
                .orElseThrow(() -> new IllegalArgumentException("[Modern Beta] NBT element is not a list! Type: " + element.getType()));
    }
}
