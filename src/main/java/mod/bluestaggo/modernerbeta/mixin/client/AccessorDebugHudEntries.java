//? if >=1.21.9 {
/*package mod.bluestaggo.modernerbeta.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.debug.DebugHudEntries;
import net.minecraft.client.gui.hud.debug.DebugHudEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Environment(EnvType.CLIENT)
@Mixin(DebugHudEntries.class)
public interface AccessorDebugHudEntries {
    @Invoker("register")
    static Identifier invokeRegister(Identifier identifier, DebugHudEntry arg) {
        throw new IllegalStateException("Failed to mixin.");
    }
}
*///?}