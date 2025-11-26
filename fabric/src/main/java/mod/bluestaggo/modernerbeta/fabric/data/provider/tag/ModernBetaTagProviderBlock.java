package mod.bluestaggo.modernerbeta.fabric.data.provider.tag;

import mod.bluestaggo.modernerbeta.tags.ModernBetaBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ModernBetaTagProviderBlock
        extends
        //? if >=1.21.6 {
        FabricTagProvider.BlockTagProvider
        //?} else {
        /*FabricTagProvider<net.minecraft.world.level.block.Block>
        *///?}
{
    public ModernBetaTagProviderBlock(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, /*? <1.21.6 {*/ /*net.minecraft.core.registries.Registries.BLOCK, *//*?}*/ registriesFuture);
    }

    @Override
    protected void addTags(Provider provider) {
        //? if >=1.21.6 {
        this.valueLookupBuilder(
        //?} else {
        /*this.builder(
        *///?}
                ModernBetaBlockTags.OVERWORLD_CARVER_REPLACEABLES
        ).add(
            Blocks.STONE,
            Blocks.COBBLESTONE,
            Blocks.DIRT,
            Blocks.GRASS_BLOCK,
            Blocks.DEEPSLATE, 
            Blocks.TUFF,
            Blocks.ANDESITE,
            Blocks.DIORITE,
            Blocks.GRANITE,
            Blocks.IRON_ORE,
            Blocks.DEEPSLATE_IRON_ORE,
            Blocks.RAW_IRON_BLOCK,
            Blocks.COPPER_ORE,
            Blocks.DEEPSLATE_COPPER_ORE,
            Blocks.RAW_COPPER_BLOCK,
            Blocks.COAL_ORE,
            Blocks.DEEPSLATE_COAL_ORE,
            Blocks.COAL_BLOCK
        );
    }
}
