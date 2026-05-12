package mod.bluestaggo.modernerbeta.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaSurfaceSystem;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionRule;
import mod.bluestaggo.modernerbeta.level.biome.injection.InjectionNeeds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
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
        //? if <26.2
        net.minecraft.core.Registry<Biome> biomes,
        boolean useLegacyRandomSource,
        WorldGenerationContext context,
        ChunkAccess chunk,
        NoiseChunk noiseChunk,
        SurfaceRules.RuleSource ruleSource,
        //? if >=26.2
        //java.util.Set<Holder<Biome>> possibleBiomes,
        CallbackInfo ci
    ) {
        if (this.modernerBeta$chunkProvider == null)
            return;

        ChunkPos chunkPos = chunk.getPos();
        Random surfaceRandom = this.modernerBeta$chunkProvider.createSurfaceRandom(chunkPos.x, chunkPos.z);
        this.modernerBeta$surfaceRandom.set(surfaceRandom);
    }

    @Inject(method = "topMaterial", at = @At("HEAD"))
    private void setupTopMaterialRandom(
        SurfaceRules.RuleSource rule,
        CarvingContext context,
        Function<BlockPos, Holder<Biome>> biomeGetter,
        ChunkAccess chunk,
        NoiseChunk noiseChunk,
        BlockPos pos,
        boolean hasFluid,
        CallbackInfoReturnable<Optional<BlockState>> cir
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
            target = "(Lnet/minecraft/world/level/levelgen/SurfaceSystem;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/levelgen/NoiseChunk;Ljava/util/function/Function;"
                    //? if <26.2
                    + "Lnet/minecraft/core/Registry;"
                    + "Lnet/minecraft/world/level/levelgen/WorldGenerationContext;"
                    //? if >=26.2
                    //+ "Ljava/util/Set;"
                    + ")Lnet/minecraft/world/level/levelgen/SurfaceRules$Context;"
        )
    )
    private @Coerce Object replaceBiomeGetterForMB(
        SurfaceSystem system,
        RandomState randomState,
        ChunkAccess chunk,
        NoiseChunk noiseChunk,
        Function<BlockPos, Holder<Biome>> biomeGetter,
        //? if <26.2
        net.minecraft.core.Registry<Biome> biomes,
        WorldGenerationContext context,
        //? if >=26.2
        //java.util.Set<Holder<Biome>> possibleBiomes,
        Operation<Object> original,
        RandomState randomState2,
        BiomeManager biomeManager,
        //? if <26.2
        net.minecraft.core.Registry<Biome> biomes2,
        boolean useLegacyRandomSource,
        WorldGenerationContext context2,
        ChunkAccess chunk2,
        NoiseChunk noiseChunk2,
        SurfaceRules.RuleSource ruleSource
        //? if >=26.2
        //, java.util.Set<Holder<Biome>> possibleBiomes2
    ) {
        //TODO: this needs to handle biome injection better (?)
        if (this.modernerBeta$biomeProvider != null/* &&
                this.modernerBeta$biomeProvider instanceof BiomeResolverBlock biomeResolver*/) {
            biomeGetter = pos ->
                this.modernerBeta$biomeProvider.getBiomeInjectionHandler().getBiomeAtBlock(
                    chunk,
                    this.modernerBeta$biomeProvider.getBiomeProvider(),
                    ((BiomeManagerAccessor) biomeManager).getBiomeZoomSeed(),
                    pos.getX(), pos.getY(), pos.getZ(),
                    BiomeInjectionRule.Step.PRE,
                    InjectionNeeds.all(),
                    true
                );
        }

        return original.call(system, randomState, chunk, noiseChunk, biomeGetter, /*? <26.2 {*/ biomes, /*? }*/ context /*? >=26.2 {*//*, possibleBiomes *//*? }*/);
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
        //? if <26.2
        net.minecraft.core.Registry<Biome> biomes,
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
