//? if <1.21.2 {
/*package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.imixin.ModernBetaWorld;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

//? if >=1.20.2 {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//?} else {
/^import org.spongepowered.asm.mixin.injection.Redirect;
^///?}

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {
    @Shadow private ClientWorld world;

    //? if >=1.20.2 {
    @WrapOperation(
    //?} else {
    /^@Redirect(
    ^///?}
        method = {
            "renderWeather",
            "tickRainSplashing"
        },
        at = @At(
            value = "INVOKE",
            target = VersionCompat.BIOME_GET_PRECIPITATION_TARGET
        )
    )
    public Biome.Precipitation modifyRenderedPrecipitation(
        Biome biome, BlockPos blockPos
        /^? if >=1.20.2 {^/, Operation<Biome.Precipitation> original/^?}^/
    ) {
        ModernBetaWorld world = (ModernBetaWorld)this.world;

        if (!world.modernerBeta$isModded()) {
            //? if >=1.20.2 {
            return original.call(biome, blockPos);
            //?} else {
            /^return biome.getPrecipitation(blockPos);
            ^///?}
        }
        return world.modernerBeta$samplePrecipitation(biome, blockPos);
    }
}
*///?}