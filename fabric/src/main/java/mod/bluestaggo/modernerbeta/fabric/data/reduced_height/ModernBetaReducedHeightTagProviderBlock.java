//? if <1.21 {
/*package mod.bluestaggo.modernerbeta.fabric.data.reduced_height;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import java.util.concurrent.CompletableFuture;

public class ModernBetaReducedHeightTagProviderBlock extends FabricTagProvider<Block> {
    public ModernBetaReducedHeightTagProviderBlock(FabricDataOutput output, CompletableFuture<Provider> registriesFuture) {
        super(output, Registries.BLOCK, registriesFuture);
    }

    @Override
    protected void addTags(Provider provider) {
        this.builder(TagKey.create(Registries.BLOCK, ModernerBeta.createId("air"))).add(
            Blocks.AIR,
            Blocks.CAVE_AIR,
            Blocks.VOID_AIR
        );
    }
}
*///?}