//? if >=26.1 {
package mod.bluestaggo.modernerbeta.fabric.data.provider.tag;

import mod.bluestaggo.modernerbeta.level.feature.configured.ModernBetaVegetationConfiguredFeatures;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.FeatureTags;
//? if >=26.3 {
/*import net.minecraft.world.level.levelgen.feature.Feature;
*///? } else {
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
//? }

import java.util.concurrent.CompletableFuture;

//~ if >=26.3 'ConfiguredFeature<?, ?>>' -> 'Feature>'
public class ModernBetaTagProviderConfiguredFeature extends FabricTagsProvider<ConfiguredFeature<?, ?>> {
    public ModernBetaTagProviderConfiguredFeature(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        //~ if >=26.3 'CONFIGURED_FEATURE' -> 'FEATURE'
        super(output, Registries.CONFIGURED_FEATURE, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.builder(FeatureTags.CAN_SPAWN_FROM_BONE_MEAL).add(
            ModernBetaVegetationConfiguredFeatures.MUSHROOM_HELL,
            ModernBetaVegetationConfiguredFeatures.DANDELION,
            ModernBetaVegetationConfiguredFeatures.POPPY
        );
    }
}
//? }
