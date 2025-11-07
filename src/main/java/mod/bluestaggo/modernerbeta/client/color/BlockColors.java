package mod.bluestaggo.modernerbeta.client.color;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public final class BlockColors {
    @FunctionalInterface
    public interface BlockColorRegisterer {
        void register(BlockColor provider, Block... blocks);
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
            VersionCompat.SHORT_GRASS,
            Blocks.POTTED_FERN
            //? if >=1.21.5
            , Blocks.BUSH
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
            Blocks.PINK_PETALS
            //? if >=1.21.5
            , Blocks.WILDFLOWERS
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
