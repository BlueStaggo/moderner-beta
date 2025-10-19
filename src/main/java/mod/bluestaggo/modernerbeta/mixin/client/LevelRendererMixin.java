//? if <1.21.2 {
/*package mod.bluestaggo.modernerbeta.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaLevel;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow private ClientLevel level;

    @WrapOperation(
        method = {
            "renderSnowAndRain",
            "tickRain"
        },
        at = @At(
            value = "INVOKE",
            target = VersionCompat.BIOME_GET_PRECIPITATION_TARGET
        )
    )
    public Biome.Precipitation modifyRenderedPrecipitation(Biome biome, BlockPos blockPos, Operation<Biome.Precipitation> original) {
        ModernBetaLevel level = (ModernBetaLevel)this.level;

        if (!level.modernerBeta$isModded()) {
            return original.call(biome, blockPos);
        }

        return level.modernerBeta$samplePrecipitation(biome, blockPos);
    }
}
*///?}
