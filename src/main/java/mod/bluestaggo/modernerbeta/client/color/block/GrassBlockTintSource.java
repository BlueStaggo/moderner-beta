package mod.bluestaggo.modernerbeta.client.color.block;

import mod.bluestaggo.modernerbeta.api.level.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.compat.client.ModCompatClient;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.block.state.BlockState;

public class GrassBlockTintSource implements net.minecraft.client.color.block.BlockColor {
    protected final BlockColorSampler colorSampler;

    public GrassBlockTintSource(BlockColorSampler sampler) {
        this.colorSampler = sampler;
    }

    //? if >=26.1
    //@Override
    public int color(BlockState state) {
        return GrassColor.getDefaultColor();
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

        if (this.colorSampler.useBiomeColor()) {
            BiomeManager biomeAccess = colorSampler.getBiomeAccessFromView(level);
            if (biomeAccess != null) {
                return this.colorSampler.sampleModifiedColorMaybeLerped(
                    biomeAccess,
                    pos,
                    //? if >=1.21.11 {
                    /*BiomeSpecialEffects::grassColorOverride,
                    BiomeSpecialEffects::grassColorModifier,
                    *///? } else {
                    BiomeSpecialEffects::getGrassColorOverride,
                    BiomeSpecialEffects::getGrassColorModifier,
                    //? }
                    GrassColor::get,
                    ModCompatClient::modifyGrassColor
                );
            }

            Clime clime = this.colorSampler.sampleClime(pos);
            return GrassColor.get(clime.temp(), clime.rain());
        }

        return BiomeColors.getAverageGrassColor(level, pos);
    }
}
