package mod.bluestaggo.modernerbeta.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaSurfaceSystem;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionRule;
import mod.bluestaggo.modernerbeta.level.biome.injection.InjectionNeeds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Function;

@Mixin(SurfaceSystem.class)
public class SurfaceSystemMixin implements ModernBetaSurfaceSystem {
    @Unique private ChunkProvider modernerBeta$chunkProvider;
    @Unique private ModernBetaBiomeSource modernerBeta$biomeProvider;
    @Unique private final ThreadLocal<RandomSource> modernerBeta$surfaceRandom = new ThreadLocal<>();

    @Override
    public void modernerBeta$setupChunkContext(ChunkProvider chunkProvider) {
        this.modernerBeta$chunkProvider = chunkProvider;
    }

    @Override
    public void modernerBeta$setupBiomeContext(ModernBetaBiomeSource biomeSource) {
        this.modernerBeta$biomeProvider = biomeSource;
    }

    @Override
    public ChunkProvider modernerBeta$getContext() {
        return this.modernerBeta$chunkProvider;
    }

    @Inject(method = "buildSurface", at = @At("HEAD"))
    private void setupSurfaceRandom(
        CallbackInfo ci,
        @Local(argsOnly = true) ChunkAccess protoChunk
    ) {
        if (this.modernerBeta$chunkProvider == null)
            return;

        ChunkPos chunkPos = protoChunk.getPos();
        RandomSource surfaceRandom = this.modernerBeta$chunkProvider.createSurfaceRandom(chunkPos.x(), chunkPos.z());
        this.modernerBeta$surfaceRandom.set(surfaceRandom);
    }

    @Inject(method = "topMaterial", at = @At("HEAD"))
    private void setupTopMaterialRandom(
        CallbackInfoReturnable<Optional<BlockState>> cir,
        @Local(argsOnly = true) ChunkAccess protoChunk
    ) {
        if (this.modernerBeta$chunkProvider == null)
            return;

        ChunkPos chunkPos = protoChunk.getPos();
        RandomSource surfaceRandom = this.modernerBeta$chunkProvider.createSurfaceRandom(chunkPos.x(), chunkPos.z());
        this.modernerBeta$surfaceRandom.set(surfaceRandom);
    }

    @ModifyArg(
        method = "buildSurface",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/levelgen/SurfaceRules$Context;<init>(Lnet/minecraft/world/level/levelgen/SurfaceSystem;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/levelgen/NoiseChunk;Ljava/util/function/Function;"
                    //? if <26.2
                    + "Lnet/minecraft/core/Registry;"
                    + "Lnet/minecraft/world/level/levelgen/WorldGenerationContext;"
                    //? if >=26.2
                    //+ "Ljava/util/Set;"
                    + ")V"
        ),
        index = 4
    )
    private Function<BlockPos, Holder<Biome>> replaceBiomeGetterForMB(
        Function<BlockPos, Holder<Biome>> biomeGetter,
        @Local(argsOnly = true) BiomeManager biomeManager,
        @Local(argsOnly = true) ChunkAccess protoChunk
    ) {
        if (this.modernerBeta$biomeProvider != null) {
            return pos ->
                this.modernerBeta$biomeProvider.getBiomeInjectionHandler().getBiomeAtBlock(
                    protoChunk,
                    this.modernerBeta$biomeProvider.getBiomeProvider(),
                    ((BiomeManagerAccessor) biomeManager).getBiomeZoomSeed(),
                    pos.getX(), pos.getY(), pos.getZ(),
                    BiomeInjectionRule.Step.PRE,
                    InjectionNeeds.all(),
                    true
                );
        }

        return biomeGetter;
    }

    @WrapOperation(
        method = "buildSurface",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/biome/BiomeManager;getBiome(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/Holder;"
        )
    )
    private Holder<Biome> getProperBiomeForMB(
        BiomeManager instance,
        BlockPos pos,
        Operation<Holder<Biome>> original,
        @Local(argsOnly = true) ChunkAccess protoChunk
    ) {
        if (this.modernerBeta$biomeProvider != null) {
            return this.modernerBeta$biomeProvider.getBiomeInjectionHandler().getBiomeAtBlock(
                protoChunk,
                this.modernerBeta$biomeProvider.getBiomeProvider(),
                ((BiomeManagerAccessor) instance).getBiomeZoomSeed(),
                pos.getX(), pos.getY(), pos.getZ(),
                BiomeInjectionRule.Step.PRE,
                InjectionNeeds.all(),
                true
            );
        }

        return original.call(instance, pos);
    }

    @Inject(method = "getSurfaceDepth", at = @At("HEAD"), cancellable = true)
    private void useMBSurfaceDepth(int blockX, int blockZ, CallbackInfoReturnable<Integer> cir) {
        if (this.modernerBeta$chunkProvider == null)
            return;

        int surfaceDepth = this.modernerBeta$chunkProvider
                .getSurfaceDepth(this.modernerBeta$surfaceRandom.get(), blockX, blockZ);
        cir.setReturnValue(surfaceDepth);
    }
}
