package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.api.level.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.client.color.block.BlockColorSampler;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeColors.class)
public abstract class BiomeColorsMixin {
    @Inject(method = "getAverageWaterColor", at = @At("HEAD"), cancellable = true)
    private static void injectGetWaterColor(BlockAndTintGetter level, BlockPos pos, CallbackInfoReturnable<Integer> info) {
        if (BlockColorSampler.INSTANCE.useWaterColor()) {
            Clime clime = BlockColorSampler.INSTANCE.getClimateSampler().sample(pos.getX(), pos.getZ());
            info.setReturnValue(BlockColorSampler.INSTANCE.colormapWater.getColor(clime.temp(), clime.rain()));
        }
    }
}
