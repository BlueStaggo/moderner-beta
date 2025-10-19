//? if forge {
/*package mod.bluestaggo.modernerbeta.forgelike.mixin;

import net.minecraft.core.Holder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Holder.Reference.class)
public interface HolderReferenceMixin<T> {
    @Invoker
    void invokeBindValue(T value);
}
*///?}
