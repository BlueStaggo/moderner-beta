//? if <1.21 {
/*package mod.bluestaggo.modernerbeta.fabric.data.reduced_height;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.TagKey;

import java.util.concurrent.CompletableFuture;

public class ModernBetaReducedHeightTagProviderBlock extends FabricTagProvider<Block> {
    public ModernBetaReducedHeightTagProviderBlock(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BLOCK, registriesFuture);
    }

    @Override
    protected void configure(WrapperLookup lookup) {
        this.builder(TagKey.of(RegistryKeys.BLOCK, ModernerBeta.createId("air"))).add(
            Blocks.AIR,
            Blocks.CAVE_AIR,
            Blocks.VOID_AIR
        );
    }
}
*///?}