//? if >=1.21.9 {
/*package mod.bluestaggo.modernerbeta.mixin.client;

import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DebugScreenEntries.class)
public interface DebugScreenEntriesAccessor {
    @Invoker("register")
    static ResourceLocation invokeRegister(ResourceLocation identifier, DebugScreenEntry arg) {
        throw new IllegalStateException("Failed to mixin.");
    }
}
*///?}
