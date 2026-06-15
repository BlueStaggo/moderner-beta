package mod.bluestaggo.modernerbeta.client.color.block;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ModernBetaConstantTintSource implements net.minecraft.client.color.block.BlockTintSource {
    private final BlockColorSampler colorSampler;
    private final int constantTint;
    private final int worldConstantTint;
    private final net.minecraft.client.color.block.BlockTintSource elseIf;

    public ModernBetaConstantTintSource(
        BlockColorSampler sampler,
        int constant,
        net.minecraft.client.color.block.BlockTintSource elseIf
    ) {
        this(sampler, constant, constant, elseIf);
    }

    public ModernBetaConstantTintSource(
        BlockColorSampler sampler,
        int constant,
        int worldConstant,
        net.minecraft.client.color.block.BlockTintSource elseIf
    ) {
        this.colorSampler = sampler;
        this.constantTint = constant;
        this.worldConstantTint = worldConstant;
        this.elseIf = elseIf;
    }

    //? if >=26.1
    @Override
    public int color(BlockState state) {
        return this.constantTint;
    }

    @Override
    //? if >=26.1 {
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
    //? } else {
    /*public int getColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) {
    *///? }
        //? if <26.1 {
        /*if (level == null || pos == null) { // Appears to enter here when loading color for inventory block
            return color(state);
        }
        *///? }

        if (this.colorSampler.useBiomeColor()) {
            return this.worldConstantTint;
        }

        //? if >=26.1 {
        return this.elseIf.colorInWorld(state, level, pos);
        //? } else {
        /*return this.elseIf.getColor(state, level, pos, tintIndex);
        *///? }
    }
}
