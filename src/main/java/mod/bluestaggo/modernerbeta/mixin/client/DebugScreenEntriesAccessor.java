//? if >=1.21.9 {
package mod.bluestaggo.modernerbeta.mixin.client;

import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DebugScreenEntries.class)
public interface DebugScreenEntriesAccessor {
    @Invoker("register")
    static Identifier invokeRegister(Identifier identifier, DebugScreenEntry arg) {
        throw new IllegalStateException("Failed to mixin.");
    }
}
//?}
