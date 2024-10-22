package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.api.world.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.client.color.BlockColorSampler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(BiomeColors.class)
public abstract class MixinBiomeColors {
    @Inject(method = "getWaterColor", at = @At("HEAD"), cancellable = true)
    private static void injectGetWaterColor(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> info) {
        if (BlockColorSampler.INSTANCE.useWaterColor()) {
            Clime clime = BlockColorSampler.INSTANCE.getClimateSampler().get().sample(pos.getX(), pos.getZ());
            
            info.setReturnValue(BlockColorSampler.INSTANCE.colormapWater.getColor(clime.temp(), clime.rain()));
        }
    }
}
