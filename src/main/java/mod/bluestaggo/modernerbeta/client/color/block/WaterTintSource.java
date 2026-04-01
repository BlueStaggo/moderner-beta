package mod.bluestaggo.modernerbeta.client.color.block;

import mod.bluestaggo.modernerbeta.api.level.biome.climate.Clime;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class WaterTintSource implements net.minecraft.client.color.block.BlockColor {
    private final BlockColorSampler colorSampler;

    public WaterTintSource(BlockColorSampler sampler) {
        this.colorSampler = sampler;
    }

    //? if >=26.1
    //@Override
    public int color(BlockState state) {
        return 0xFFFFFFFF;
    }

    @Override
    //? if >=26.1 {
    /*public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
    *///? } else {
    public int getColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) {
    //? }
        //? if <26.1 {
        if (level == null || pos == null) { // Appears to enter here when loading color for inventory block
            return color(state);
        }
        //? }

        if (this.colorSampler.useWaterColor()) {
            Clime clime = this.colorSampler.sampleClime(pos);
            return this.colorSampler.colormapWater.getColor(clime.temp(), clime.rain());
        }

        return BiomeColors.getAverageWaterColor(level, pos);
    }
}
