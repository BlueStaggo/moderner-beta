package mod.bluestaggo.modernerbeta.client.color.block;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class GrassTintSource extends GrassBlockTintSource {
    private final boolean doubleTall;

    public GrassTintSource(BlockColorSampler sampler, boolean doubleTall) {
        super(sampler);
        this.doubleTall = doubleTall;
    }

    @Override
    //? if >=26.1 {
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
    //? } else {
    /*public int getColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) {
     *///? }
        if (pos != null && this.colorSampler.useBiomeColor() &&
                this.colorSampler.getClimateDistribution().fuzzyGrass()) {
            if (doubleTall && state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
                pos = pos.below();
            }

            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            long shift = x * 0x2FC20FL + z * 0x5D8875L + y;
            shift = shift * shift * 0x285B825L + shift * 11L;
            pos = pos.offset(
                (int) (shift >> 14 & 31L),
                (int) (shift >> 19 & 31L),
                (int) (shift >> 24 & 31L)
            );
        }

        //? if >=26.1 {
        return super.colorInWorld(state, level, pos);
        //? } else {
        /*return super.getColor(state, level, pos, tintIndex);
        *///? }
    }

    //? if >=26.1 {
    @Override
    public int colorAsTerrainParticle(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        return this.colorInWorld(state, level, pos);
    }

    @Override
    public java.util.Set<net.minecraft.world.level.block.state.properties.Property<?>> relevantProperties() {
        return doubleTall ? java.util.Set.of(DoublePlantBlock.HALF) : java.util.Set.of();
    }
    //? }
}
