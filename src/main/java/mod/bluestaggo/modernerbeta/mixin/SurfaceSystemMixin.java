package mod.bluestaggo.modernerbeta.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mod.bluestaggo.modernerbeta.api.level.biome.BiomeResolverBlock;
import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaSurfaceSystem;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionRule;
import mod.bluestaggo.modernerbeta.level.biome.injection.InjectionNeeds;
import mod.bluestaggo.modernerbeta.level.chunk.surface.LegacyBadlandsBands;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.Noise3DSettings;
import mod.bluestaggo.modernerbeta.settings.component.SurfaceProperties;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Function;

//~ if >=26.3 'SurfaceSystem' -> 'net.minecraft.world.level.levelgen.material.MaterialSystem'
@Mixin(SurfaceSystem.class)
public class SurfaceSystemMixin implements ModernBetaSurfaceSystem {
    @Unique private ChunkProvider modernerBeta$chunkProvider;
    @Unique private ModernBetaBiomeSource modernerBeta$biomeSource;
    @Unique private LegacyBadlandsBands modernerBeta$badlandsBands;
    @Unique private SurfaceProperties modernerBeta$surfaceProperties;
    @Unique private final ThreadLocal<RandomSource> modernerBeta$surfaceRandom = new ThreadLocal<>();

    @Override
    public void modernerBeta$setupChunkContext(ChunkProvider chunkProvider) {
        if (this.modernerBeta$chunkProvider == chunkProvider)
            return;

        this.modernerBeta$chunkProvider = chunkProvider;

        ModernBetaSettings chunkSettings = chunkProvider.getChunkSettings();
        SurfaceProperties surface = chunkSettings.getOrDefault(SettingsComponentTypes.SURFACE_PROPERTIES);
        Noise3DSettings noise = chunkSettings.getOrDefault(SettingsComponentTypes.NOISE_3D_SETTINGS);
        this.modernerBeta$surfaceProperties = surface;
        this.modernerBeta$badlandsBands = surface.legacyBadlandsBands()
            ? new LegacyBadlandsBands(chunkProvider.getSeed(), noise.pocketEditionRng())
            : null;
    }

    @Override
    public void modernerBeta$setupBiomeContext(ModernBetaBiomeSource biomeSource) {
        this.modernerBeta$biomeSource = biomeSource;
    }

    @Override
    public void modernerBeta$beforeSurfaceBuild(ChunkAccess chunk) {
        ChunkPos chunkPos = chunk.getPos();
        RandomSource surfaceRandom = this.modernerBeta$chunkProvider.createSurfaceRandom(chunkPos.x(), chunkPos.z());
        this.modernerBeta$surfaceRandom.set(surfaceRandom);
    }

    @Override
    public ChunkProvider modernerBeta$getContext() {
        return this.modernerBeta$chunkProvider;
    }

    @Inject(method = "topMaterial", at = @At("HEAD"))
    private void setupTopMaterialRandom(
        CallbackInfoReturnable<Optional<BlockState>> cir,
        @Local(argsOnly = true) ChunkAccess protoChunk
    ) {
        if (this.modernerBeta$chunkProvider == null)
            return;

        this.modernerBeta$beforeSurfaceBuild(protoChunk);
    }

    @ModifyArg(
        method = "buildSurface",
        at = @At(
            value = "INVOKE",
            //~ if >=26.3 'MaterialRules$Context' -> 'material/MaterialRuleContext', 'SurfaceSystem' -> 'material/MaterialSystem'
            target = "Lnet/minecraft/world/level/levelgen/MaterialRules$Context;<init>(Lnet/minecraft/world/level/levelgen/SurfaceSystem;Lnet/minecraft/world/level/levelgen/RandomState;"
                    //? if >=26.3 {
                    /*+ "Lnet/minecraft/world/level/levelgen/densityfunction/DensityVolume;Lnet/minecraft/world/level/levelgen/densityfunction/DensitySamplerSet;"
                    *///? } else {
                    + "Lnet/minecraft/world/level/chunk/ChunkAccess;"
                    //? }
                    //? if <26.3
                    + "Lnet/minecraft/world/level/levelgen/NoiseChunk;"
                    + "Ljava/util/function/Function;"
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
        if (this.modernerBeta$biomeSource != null &&
                this.modernerBeta$biomeSource.getBiomeProvider() instanceof BiomeResolverBlock) {
            return pos ->
                this.modernerBeta$biomeSource.getBiomeInjectionHandler().getBiomeAtBlock(
                    protoChunk,
                    this.modernerBeta$biomeSource.getBiomeProvider(),
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
        if (this.modernerBeta$biomeSource != null &&
                this.modernerBeta$biomeSource.getBiomeProvider() instanceof BiomeResolverBlock) {
            return this.modernerBeta$biomeSource.getBiomeInjectionHandler().getBiomeAtBlock(
                protoChunk,
                this.modernerBeta$biomeSource.getBiomeProvider(),
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

        RandomSource rand = this.modernerBeta$surfaceRandom.get();

        //to get random values to match up with what they're supposed to be
        if (this.modernerBeta$surfaceProperties.enableBeaches()) {
            rand.consumeCount(2 * 2);
        }

        //because we cannot just blindly consumeCount for nextInt.
        if (this.modernerBeta$surfaceProperties.bedrockHoles()) {
            rand.nextInt(6);
        } else {
            rand.nextInt(5);
        }

        int surfaceDepth = this.modernerBeta$chunkProvider.getSurfaceDepth(rand, blockX, blockZ);
        cir.setReturnValue(surfaceDepth);
    }

    @Inject(method = "getSurfaceSecondary", at = @At("HEAD"), cancellable = true)
    private void useMBRandomDepth(int blockX, int blockZ, CallbackInfoReturnable<Double> cir) {
        if (this.modernerBeta$chunkProvider == null)
            return;

        RandomSource rand = this.modernerBeta$surfaceRandom.get();
        int depth = rand.nextInt(4);
        cir.setReturnValue((depth / 3.0) * 2.0 - 1.0);
    }

    @Inject(method = "getBand", at = @At("HEAD"), cancellable = true)
    private void useLegacyBadlandsBands(int x, int y, int z, CallbackInfoReturnable<BlockState> cir) {
        if (this.modernerBeta$badlandsBands != null)
            cir.setReturnValue(this.modernerBeta$badlandsBands.sample(x, y, z));
    }
}
