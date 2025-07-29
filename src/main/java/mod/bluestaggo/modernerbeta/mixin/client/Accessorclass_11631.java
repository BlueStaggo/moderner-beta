//? if >=1.21.9 {
/*package mod.bluestaggo.modernerbeta.mixin.client;

import net.minecraft.class_11631;
import net.minecraft.class_11632;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(class_11631.class)
public interface Accessorclass_11631 {
    @Invoker("method_72763")
    static Identifier invokeRegister(Identifier identifier, class_11632 arg) {
        throw new IllegalStateException("Failed to mixin.");
    }
}
*///?}