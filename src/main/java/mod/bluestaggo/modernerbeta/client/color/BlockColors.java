package mod.bluestaggo.modernerbeta.client.color;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColorProvider;

@Environment(EnvType.CLIENT)
public final class BlockColors {
    @FunctionalInterface
    public interface BlockColorRegisterer {
        void register(BlockColorProvider provider, Block... blocks);
    }
    
    public static void register(BlockColorRegisterer registerer) {
        // Grass blocks
        registerer.register(
            BlockColorSampler.INSTANCE::getGrassColor,
            Blocks.GRASS_BLOCK
        );

        // Short grass blocks
        registerer.register(
            BlockColorSampler.INSTANCE::getShortGrassColor,
            Blocks.FERN,
            Blocks.SHORT_GRASS,
            Blocks.POTTED_FERN,
            Blocks.BUSH
        );

        // Tall grass blocks
        registerer.register(
            BlockColorSampler.INSTANCE::getTallGrassColor,
            Blocks.TALL_GRASS,
            Blocks.LARGE_FERN
        );

        // Petal blocks
        registerer.register(
            BlockColorSampler.INSTANCE::getPetalColor,
            Blocks.PINK_PETALS,
            Blocks.WILDFLOWERS
        );

        // Foliage blocks
        registerer.register(
            BlockColorSampler.INSTANCE::getFoliageColor,
            Blocks.OAK_LEAVES,
            Blocks.JUNGLE_LEAVES,
            Blocks.ACACIA_LEAVES,
            Blocks.DARK_OAK_LEAVES,
            Blocks.MANGROVE_LEAVES,
            Blocks.VINE
        );

        // Sugar cane
        registerer.register(
            BlockColorSampler.INSTANCE::getSugarCaneColor,
            Blocks.SUGAR_CANE
        );

        // Water blocks
        registerer.register(
            BlockColorSampler.INSTANCE::getWaterColor,
            Blocks.WATER,
            Blocks.BUBBLE_COLUMN,
            Blocks.WATER_CAULDRON
        );
    }
}
