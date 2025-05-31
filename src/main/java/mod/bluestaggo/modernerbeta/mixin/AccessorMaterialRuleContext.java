package mod.bluestaggo.modernerbeta.mixin;

import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MaterialRules.MaterialRuleContext.class)
public interface AccessorMaterialRuleContext {
    @Accessor
    ChunkNoiseSampler getChunkNoiseSampler();

    @Accessor
    int getBlockY();

    @Accessor
    HeightContext getHeightContext();

    @Accessor
    int getRunDepth();

    @Accessor
    int getFluidHeight();
}
