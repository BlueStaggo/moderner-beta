package mod.bluestaggo.modernerbeta.mixin;

import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProviderNoise;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaSurfaceSystem;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkHeightmap;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.SurfaceSystem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.level.levelgen.SurfaceRules$Context")
public class SurfaceRulesContextMixin {
    @Shadow @Final private SurfaceSystem system;
    @Shadow @Final private ChunkAccess chunk;

    @Shadow private int blockX;
    @Shadow private int blockZ;

    @Shadow private int surfaceDepth;

    @Inject(method = "getMinSurfaceLevel", at = @At("HEAD"), cancellable = true)
    private void usePreciseSurfaceLevel(CallbackInfoReturnable<Integer> cir) {
        if (!(system instanceof ModernBetaSurfaceSystem mbSurfaceSystem))
            return;

        ChunkProvider chunkContext = mbSurfaceSystem.modernerBeta$getContext();
        if (chunkContext == null)
            return;

        LevelHeightAccessor heightAccessor = chunk.getHeightAccessorForGeneration();
        int blockHeight = 0;

        if (chunkContext instanceof ChunkProviderNoise noiseChunkProvider) {
            blockHeight = noiseChunkProvider.getHeight(heightAccessor, blockX, blockZ, ChunkHeightmap.Type.SURFACE_FLOOR);
        } else {
            blockHeight = chunkContext.getHeight(heightAccessor, blockX, blockZ, Heightmap.Types.OCEAN_FLOOR_WG);
        }

        cir.setReturnValue(blockHeight + surfaceDepth - 8);
    }
}
