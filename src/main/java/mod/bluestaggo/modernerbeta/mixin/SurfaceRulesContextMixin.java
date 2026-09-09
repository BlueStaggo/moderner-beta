package mod.bluestaggo.modernerbeta.mixin;

import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaSurfaceContext;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaSurfaceSystem;
//? if >=26.3 {
/*import net.minecraft.world.level.levelgen.material.MaterialRuleContext;
import net.minecraft.world.level.levelgen.material.MaterialSystem;
*///? } else {
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.SurfaceSystem;
//? }
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SurfaceRules.Context.class)
public class SurfaceRulesContextMixin implements ModernBetaSurfaceContext {
    //~ if >=26.3 'SurfaceSystem' -> 'MaterialSystem'
    @Shadow @Final private SurfaceSystem system;
    //? if <26.3 {
    @Shadow @Final private NoiseChunk noiseChunk;

    @Shadow private int blockX;
    @Shadow private int blockZ;
    //? }

    @Shadow private int surfaceDepth;
    @Shadow private int blockY;
    @Shadow private int stoneDepthAbove;

    @Override
    public ChunkProvider modernerBeta$getChunkProvider() {
        return system instanceof ModernBetaSurfaceSystem surfaceSystem
            ? surfaceSystem.modernerBeta$getContext()
            : null;
    }

    @Override
    public int modernerBeta$getBlockY() {
        return this.blockY;
    }

    @Override
    public int modernerBeta$getStoneDepthAbove() {
        return this.stoneDepthAbove;
    }

    @Override
    public int modernerBeta$getSurfaceDepth() {
        return this.surfaceDepth;
    }

    //? if <26.3 {
    @Inject(method = "getMinSurfaceLevel", at = @At("HEAD"), cancellable = true)
    private void usePreciseSurfaceLevel(CallbackInfoReturnable<Integer> cir) {
        if (!(system instanceof ModernBetaSurfaceSystem mbSurfaceSystem))
            return;

        if (mbSurfaceSystem.modernerBeta$getContext() == null)
            return;

        //bypass all interpolation stuff if in Moderner Beta
        //~ if >=26.3 'preliminarySurfaceLevel' -> 'computePreliminarySurfaceLevel'
        int blockHeight = noiseChunk.preliminarySurfaceLevel(blockX, blockZ);
        cir.setReturnValue(blockHeight + surfaceDepth - 8);
    }
    //? }
}
