//? if >=26.1 {
/*package mod.bluestaggo.modernerbeta.fabric.data.provider.tag;

import mod.bluestaggo.modernerbeta.level.feature.configured.ModernBetaVegetationConfiguredFeatures;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.FeatureTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.concurrent.CompletableFuture;

public class ModernBetaTagProviderConfiguredFeature extends FabricTagProvider<ConfiguredFeature<?, ?>> {
    public ModernBetaTagProviderConfiguredFeature(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, Registries.CONFIGURED_FEATURE, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.builder(FeatureTags.CAN_SPAWN_FROM_BONE_MEAL).add(
            ModernBetaVegetationConfiguredFeatures.MUSHROOM,
            ModernBetaVegetationConfiguredFeatures.DANDELION,
            ModernBetaVegetationConfiguredFeatures.POPPY
        );
    }
}
*///? }
