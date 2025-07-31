//? if >=1.21.9 {
/*package mod.bluestaggo.modernerbeta.mixin.client;

import net.minecraft.client.gui.hud.debug.DebugHudEntries;
import net.minecraft.client.gui.hud.debug.DebugHudEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DebugHudEntries.class)
public interface AccessorDebugHudEntries {
    @Invoker("register")
    static Identifier invokeRegister(Identifier identifier, DebugHudEntry arg) {
        throw new IllegalStateException("Failed to mixin.");
    }
}
*///?}