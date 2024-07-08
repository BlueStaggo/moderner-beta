package mod.bluestaggo.modernerbeta.settings;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.util.NbtReader;
import net.minecraft.nbt.NbtCompound;
import org.slf4j.event.Level;

public interface ModernBetaSettings {
    NbtCompound toCompound();
    
    public static void datafix(String tag, NbtReader reader, Runnable datafixer) {
        if (reader.contains(tag)) {
            ModernerBeta.log(Level.INFO, String.format("Found old setting '%s', fixing..", tag));
            datafixer.run();
        }
    }
}
