package mod.bluestaggo.modernerbeta.mixin;

import mod.bluestaggo.modernerbeta.imixin.ModernBetaWorld;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//? if >=1.20.2 {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//?} else {
/*import org.spongepowered.asm.mixin.injection.Redirect;
*///?}

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld implements ModernBetaWorld {
    //? if >=1.20.2 {
    @WrapOperation(
    //?} else {
    /*@Redirect(
    *///?}
        //? if >=1.20.2 {
        method = "tickIceAndSnow",
        //?} else {
        /*method = "tickChunk",
        *///?}
        at = @At(
            value = "INVOKE",
            target = VersionCompat.BIOME_GET_PRECIPITATION_TARGET
        )
    )
    public Biome.Precipitation modifyTickPrecipitation(
        Biome biome, BlockPos blockPos
        /*? if >=1.21.2 {*/, int seaLevel/*?}*/
        /*? if >=1.20.2 {*/, Operation<Biome.Precipitation> original/*?}*/
    ) {
        if (!this.modernerBeta$isModded()) {
            //? if >=1.21.2 {
            return original.call(biome, blockPos, seaLevel);
            //?} else if >=1.20.2 {
            /*return original.call(biome, blockPos);
            *///?} else {
            /*return biome.getPrecipitation(blockPos);
            *///?}
        }
        return this.modernerBeta$samplePrecipitation(biome, blockPos);
    }
}
