package mod.bluestaggo.modernerbeta.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.bluestaggo.modernerbeta.api.level.biome.BiomeResolverBlock;
import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaSurfaceSystem;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionRule;
import mod.bluestaggo.modernerbeta.level.biome.injection.InjectionNeeds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;
import java.util.function.Function;

@Mixin(SurfaceSystem.class)
public class SurfaceSystemMixin implements ModernBetaSurfaceSystem {
    @Unique private ChunkProvider modernerBeta$chunkProvider;
    @Unique private ModernBetaBiomeSource modernerBeta$biomeProvider;
    @Unique private final ThreadLocal<Random> modernerBeta$surfaceRandom = new ThreadLocal<>();

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
        RandomState randomState,
        BiomeManager biomeManager,
        Registry<Biome> biomes,
        boolean useLegacyRandomSource,
        WorldGenerationContext context,
        ChunkAccess chunk,
        NoiseChunk noiseChunk,
        SurfaceRules.RuleSource ruleSource,
        CallbackInfo ci
    ) {
        if (this.modernerBeta$chunkProvider == null)
            return;

        ChunkPos chunkPos = chunk.getPos();
        Random surfaceRandom = this.modernerBeta$chunkProvider.createSurfaceRandom(chunkPos.x, chunkPos.z);
        this.modernerBeta$surfaceRandom.set(surfaceRandom);
    }

    @WrapOperation(
        method = "buildSurface",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/levelgen/SurfaceSystem;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/levelgen/NoiseChunk;Ljava/util/function/Function;Lnet/minecraft/core/Registry;Lnet/minecraft/world/level/levelgen/WorldGenerationContext;)Lnet/minecraft/world/level/levelgen/SurfaceRules$Context;"
        )
    )
    private @Coerce Object replaceBiomeGetterForMB(
        SurfaceSystem system,
        RandomState randomState,
        ChunkAccess chunk,
        NoiseChunk noiseChunk,
        Function<BlockPos, Holder<Biome>> biomeGetter,
        Registry<Biome> biomes,
        WorldGenerationContext context,
        Operation<Object> original
    ) {
        //TODO: this needs to handle biome injection
        if (this.modernerBeta$biomeProvider != null &&
                this.modernerBeta$biomeProvider instanceof BiomeResolverBlock biomeResolver) {
            biomeGetter = pos ->
                biomeResolver.getBiomeBlock(pos.getX(), pos.getY(), pos.getZ());
        }

        return original.call(system, randomState, chunk, noiseChunk, biomeGetter, biomes, context);
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
        RandomState randomState,
        BiomeManager biomeManager,
        Registry<Biome> biomes,
        boolean useLegacyRandomSource,
        WorldGenerationContext context,
        ChunkAccess chunk,
        NoiseChunk noiseChunk,
        SurfaceRules.RuleSource ruleSource
    ) {
        //TODO: this needs to handle biome injection better (?)
        if (this.modernerBeta$biomeProvider != null/* &&
                this.modernerBeta$biomeProvider instanceof BiomeResolverBlock biomeResolver*/) {
//            return biomeResolver.getBiomeBlock(pos.getX(), pos.getY(), pos.getZ());
            return this.modernerBeta$biomeProvider.getBiomeInjectionHandler().getBiomeAtBlock(
                chunk,
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
    private void useMBSurfaceDepth(int x, int z, CallbackInfoReturnable<Integer> cir) {
        if (this.modernerBeta$chunkProvider == null)
            return;

        int surfaceDepth = this.modernerBeta$chunkProvider
                .getSurfaceDepth(this.modernerBeta$surfaceRandom.get(), x, z);
        cir.setReturnValue(surfaceDepth);
    }
}
