//? if <1.21.11 {
package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.client.color.SkyColorSampler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = ClientLevel.class, priority = 1)
public abstract class ClientLevelMixin {
    @ModifyVariable(method = "getSkyColor", at = @At("STORE"), ordinal = 1)
    private Vec3 injectGetSkyColor(Vec3 originalColor, Vec3 pos, float f2) {
        SkyColorSampler sampler = SkyColorSampler.INSTANCE;

        if (sampler.useSkyColor())
            return SkyColorSampler.INSTANCE.getSkyColor(pos);

        return originalColor;
    }
}
//? }