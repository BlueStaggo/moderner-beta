package mod.bluestaggo.modernerbeta.client.color;

import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import net.minecraft.block.Blocks;

public final class BlockColors {
    public static void register() {
        // Grass blocks
        ColorHandlerRegistry.registerBlockColors(
            BlockColorSampler.INSTANCE::getGrassColor,
            Blocks.GRASS_BLOCK
        );
        
        // Tall grass blocks
        ColorHandlerRegistry.registerBlockColors(
            BlockColorSampler.INSTANCE::getTallGrassColor,
            Blocks.FERN,
            Blocks.GRASS,
            Blocks.POTTED_FERN,
            Blocks.TALL_GRASS,
            Blocks.LARGE_FERN
        );
        
        // Foliage blocks
        ColorHandlerRegistry.registerBlockColors(
            BlockColorSampler.INSTANCE::getFoliageColor,
            Blocks.OAK_LEAVES, 
            Blocks.JUNGLE_LEAVES, 
            Blocks.ACACIA_LEAVES, 
            Blocks.DARK_OAK_LEAVES, 
            Blocks.VINE
        );
        
        // Sugar cane
        ColorHandlerRegistry.registerBlockColors(
            BlockColorSampler.INSTANCE::getSugarCaneColor,
            Blocks.SUGAR_CANE
        );
        
        // Water blocks
        ColorHandlerRegistry.registerBlockColors(
            BlockColorSampler.INSTANCE::getWaterColor,
            Blocks.WATER,
            Blocks.BUBBLE_COLUMN,
            Blocks.WATER_CAULDRON
        );
    }   
}
