//? if >=1.21.11 {
package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.client.color.SkyColorSampler;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnvironmentAttributeSystem.class)
public abstract class EnvironmentAttributeSystemMixin {
    @Inject(
        method = "addDefaultLayers",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/attribute/EnvironmentAttributeSystem;addBiomeLayer(Lnet/minecraft/world/attribute/EnvironmentAttributeSystem$Builder;Lnet/minecraft/core/HolderLookup;Lnet/minecraft/world/level/biome/BiomeManager;)V",
            shift = At.Shift.AFTER
        )
    )
    private static void addSkyColorLayer(EnvironmentAttributeSystem.Builder builder, Level level, CallbackInfo ci) {
        builder.addPositionalLayer(EnvironmentAttributes.SKY_COLOR, (color, pos, interpolator) -> {
            SkyColorSampler sampler = SkyColorSampler.INSTANCE;
            if (sampler.useSkyColor()) {
                return sampler.getSkyColor(pos);
            }

            return color;
        });
    }
}
//? }