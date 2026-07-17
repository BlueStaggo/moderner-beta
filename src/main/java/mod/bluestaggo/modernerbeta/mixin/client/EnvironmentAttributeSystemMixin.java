//? if >=1.21.11 {
package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.client.color.SkyColorSampler;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

//? if >=26.3 {
/*@Mixin(EnvironmentAttributeSystem.Builder.class)
*///? } else {
@Mixin(EnvironmentAttributeSystem.class)
//? }
public abstract class EnvironmentAttributeSystemMixin {
    @Inject(
        method = "addDefaultLayers",
        at = @At(
            value = "INVOKE",
            //? if >=26.3 {
            /*target = "Lnet/minecraft/world/attribute/EnvironmentAttributeSystem$Builder;addStaticLayers(Lnet/minecraft/world/level/LevelAccessor;)Lnet/minecraft/world/attribute/EnvironmentAttributeSystem$Builder;",
            *///? } else {
            target = "Lnet/minecraft/world/attribute/EnvironmentAttributeSystem;addBiomeLayer(Lnet/minecraft/world/attribute/EnvironmentAttributeSystem$Builder;Lnet/minecraft/core/HolderLookup;Lnet/minecraft/world/level/biome/BiomeManager;)V",
            //? }
            shift = At.Shift.AFTER
        )
    )
    private /*? <26.3 {*/ static /*? }*/ void addSkyColorLayer(
        //? if <26.3
        EnvironmentAttributeSystem.Builder builder,
        Level level,
        //? if >=26.3 {
        /*org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<EnvironmentAttributeSystem.Builder> cir
        *///? } else {
        org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci
        //? }
    ) {
        //? if >=26.3
        //EnvironmentAttributeSystem.Builder builder = (EnvironmentAttributeSystem.Builder) (Object) this;

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