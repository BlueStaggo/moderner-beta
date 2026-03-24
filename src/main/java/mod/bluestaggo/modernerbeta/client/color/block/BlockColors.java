package mod.bluestaggo.modernerbeta.client.color.block;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public final class BlockColors {
    public static void register(BlockColorRegisterer registerer) {
        // Grass blocks
        registerer.register(
            new GrassBlockTintSource(BlockColorSampler.INSTANCE),
            Blocks.GRASS_BLOCK
        );

        // Short grass blocks
        registerer.register(
            new GrassTintSource(BlockColorSampler.INSTANCE, false),
            Blocks.FERN,
            VersionCompat.SHORT_GRASS,
            Blocks.POTTED_FERN
            //? if >=1.21.5
            , Blocks.BUSH
        );

        // Tall grass blocks
        registerer.register(
            new GrassTintSource(BlockColorSampler.INSTANCE, true),
            Blocks.TALL_GRASS,
            Blocks.LARGE_FERN
        );

        // Petal blocks
        //? if <26.1
        GrassTintSource petalGrassTint = new GrassTintSource(BlockColorSampler.INSTANCE, false);
        registerer.register(
            //? if >=26.1 {
            /*java.util.List.of(
                net.minecraft.client.color.block.BlockColors.constant(0xFFFFFFFF),
                new GrassTintSource(BlockColorSampler.INSTANCE, false)
            ),
            *///? } else {
            (state, view, pos, tintIndex) -> {
                if (tintIndex == 0)
                    return 0xFFFFFFFF;

                return petalGrassTint.getColor(state, view, pos, tintIndex);
            },
            //? }
            Blocks.PINK_PETALS
            //? if >=1.21.5
            , Blocks.WILDFLOWERS
        );

        // Foliage blocks
        registerer.register(
            new FoliageTintSource(BlockColorSampler.INSTANCE),
            Blocks.OAK_LEAVES,
            Blocks.JUNGLE_LEAVES,
            Blocks.ACACIA_LEAVES,
            Blocks.DARK_OAK_LEAVES,
            Blocks.MANGROVE_LEAVES,
            Blocks.VINE
        );

        // Sugar cane
        registerer.register(
            new ModernBetaConstantTintSource(
                BlockColorSampler.INSTANCE,
                0xFFFFFFFF,
                //? if >=26.1 {
                /*net.minecraft.client.color.block.BlockColors.sugarCane()
                *///? } else {
                (state, level, pos, tintIndex) ->
                    net.minecraft.client.renderer.BiomeColors.getAverageGrassColor(level, pos)
                //? }
            ),
            Blocks.SUGAR_CANE
        );

        // Water blocks
        registerer.register(
            new WaterTintSource(BlockColorSampler.INSTANCE),
            Blocks.WATER,
            Blocks.BUBBLE_COLUMN,
            Blocks.WATER_CAULDRON
        );
    }

    @FunctionalInterface
    public interface BlockColorRegisterer {
        //? if >=26.1 {
        /*default void register(net.minecraft.client.color.block.BlockColor source, Block... blocks) {
            register(java.util.List.of(source), blocks);
        }

        void register(java.util.List<net.minecraft.client.color.block.BlockColor> sources, Block... blocks);
        *///? } else {
        void register(net.minecraft.client.color.block.BlockColor provider, Block... blocks);
        //? }
    }
}
