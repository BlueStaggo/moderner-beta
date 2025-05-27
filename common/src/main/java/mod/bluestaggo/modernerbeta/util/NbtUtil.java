package mod.bluestaggo.modernerbeta.util;

import net.minecraft.nbt.*;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class NbtUtil {
    /*
     * Helper methods for reading primitive values from NbtCompound objects
     */
    
    public static Identifier readIdentifierOrThrow(String key, NbtCompound tag) {
        String str = tag.getString(key)
                .orElseThrow(() -> new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key));

        return Identifier.of(str);
    }
    
    public static Identifier readIdentifier(String key, NbtCompound tag, Identifier alternate) {
        return tag.getString(key).map(Identifier::of).orElse(alternate);
    }

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
        return element.asString()
                .orElseThrow(() -> new IllegalArgumentException("[Modern Beta] NBT element is not a string! Type:" + element.getType()));
    }
    
    public static String toString(NbtElement element, String alternate) {
        return element.asString().orElse(alternate);
    }
    
    public static int toIntOrThrow(NbtElement element) {
        return element.asInt()
                .orElseThrow(() -> new IllegalArgumentException("[Modern Beta] NBT element is not an int! Type: " + element.getType()));
    }
    
    public static int toInt(NbtElement element, int alternate) {
        return element.asInt().orElse(alternate);
    }
    
    public static float toFloatOrThrow(NbtElement element) {
        return element.asFloat()
                .orElseThrow(() -> new IllegalArgumentException("[Modern Beta] NBT element is not a float! Type: " + element.getType()));
    }
    
    public static float toFloat(NbtElement element, float alternate) {
        return element.asFloat().orElse(alternate);
    }
    
    public static double toDoubleOrThrow(NbtElement element) {
        return element.asDouble()
                .orElseThrow(() -> new IllegalArgumentException("[Modern Beta] NBT element is not a double! Type: " + element.getType()));
    }
    
    public static double toDouble(NbtElement element, double alternate) {
        return element.asDouble().orElse(alternate);
    }
    
    public static boolean toBooleanOrThrow(NbtElement element) {
        return element.asBoolean()
                .orElseThrow(() -> new IllegalArgumentException("[Modern Beta] NBT element is not a boolean! Type: " + element.getType()));
    }
    
    public static boolean toBoolean(NbtElement element, boolean alternate) {
        return element.asBoolean().orElse(alternate);
    }
    
    public static NbtCompound toCompoundOrThrow(NbtElement element) {
        return element.asCompound()
                .orElseThrow(() -> new IllegalArgumentException("[Modern Beta] NBT element is not a compound! Type: " + element.getType()));
    }
    
    public static NbtCompound toCompound(NbtElement element, NbtCompound alternate) {
        return element.asCompound().orElse(alternate);
    }
    
    public static NbtList toListOrThrow(NbtElement element) {
        return element.asNbtList()
                .orElseThrow(() -> new IllegalArgumentException("[Modern Beta] NBT element is not a list! Type: " + element.getType()));
    }
}
