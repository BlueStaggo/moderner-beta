package mod.bluestaggo.modernerbeta.fabric.data;

import mod.bluestaggo.modernerbeta.world.structure.ModernBetaStructures;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.concurrent.CompletableFuture;

public class ModernBetaTagProviderStructure extends FabricTagProvider<Structure> {
    public ModernBetaTagProviderStructure(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.STRUCTURE, registriesFuture);
    }

    @Override
    protected void addTags(Provider provider) {
        this.builder(StructureTags.EYE_OF_ENDER_LOCATED)
            .add(ModernBetaStructures.INDEV_STRONGHOLD);
    }
}
