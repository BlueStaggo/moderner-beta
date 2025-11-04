//? if >=1.21.11 {
/*package mod.bluestaggo.modernerbeta.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.bluestaggo.modernerbeta.client.color.SkyColorSampler;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.SkyRenderState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SkyRenderer.class)
public class SkyRendererMixin {
    @WrapOperation(
        method = "extractRenderState",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/renderer/state/SkyRenderState;skyColor:I",
            opcode = Opcodes.PUTFIELD
        )
    )
    private void injectSkyColor(
        SkyRenderState instance,
        int value,
        Operation<Void> original,
        ClientLevel clientLevel,
        float f,
        Camera camera,
        SkyRenderState skyRenderState
    ) {
        SkyColorSampler sampler = SkyColorSampler.INSTANCE;
        if (sampler.useSkyColor()) {
            original.call(instance, sampler.getSkyColor(camera.position()));
            return;
        }

        original.call(instance, value);
    }
}
*///? }