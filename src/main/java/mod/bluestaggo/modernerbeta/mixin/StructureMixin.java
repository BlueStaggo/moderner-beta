package mod.bluestaggo.modernerbeta.mixin;

import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionRule;
import mod.bluestaggo.modernerbeta.level.biome.injection.InjectionNeeds;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaChunkGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationStub;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Structure.class)
public abstract class StructureMixin {
    @Inject(method = "isValidBiome", at = @At("HEAD"), cancellable = true)
    private static void injectIsValidBiome(GenerationStub result, GenerationContext context, CallbackInfoReturnable<Boolean> info) {
        BlockPos blockPos = result.position();
        
        if (context.chunkGenerator() instanceof ModernBetaChunkGenerator chunkGenerator) {
            if (chunkGenerator.getBiomeInjector() != null) {
                Holder<Biome> biome = chunkGenerator.getBiomeInjector().getBiomeAtBlock(
                    context.heightAccessor(),
                    blockPos.getX(),
                    blockPos.getY(),
                    blockPos.getZ(),
                    context.randomState().sampler(),
                    BiomeInjectionRule.Step.ALL,
                    InjectionNeeds.all()
                );
                
                boolean isBiomeValid = context.validBiome().test(biome);
                
                info.setReturnValue(isBiomeValid);
            }
        }
    }
}
