//? if forge {
/*package mod.bluestaggo.modernerbeta.forgelike.mixin;

import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RegistryEntry.Reference.class)
public interface AccessorRegistryEntryReference<T> {
    @Invoker
    void invokeSetValue(T value);
}
*///?}