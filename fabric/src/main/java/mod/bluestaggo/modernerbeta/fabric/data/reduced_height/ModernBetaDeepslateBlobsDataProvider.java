package mod.bluestaggo.modernerbeta.fabric.data.reduced_height;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.OreConfiguredFeatures;
import net.minecraft.world.gen.feature.OrePlacedFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;

import java.util.concurrent.CompletableFuture;

import static mod.bluestaggo.modernerbeta.fabric.data.reduced_height.ModernBetaReducedHeightDataProvider.alwaysOwnedRegistryEntry;
import static mod.bluestaggo.modernerbeta.fabric.data.reduced_height.ModernBetaReducedHeightDataProvider.modifiersWithCount;

public class ModernBetaDeepslateBlobsDataProvider extends FabricDynamicRegistryProvider {
    public ModernBetaDeepslateBlobsDataProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        RegistryWrapper.Impl<ConfiguredFeature<?, ?>> registryConfiguredFeature = VersionCompat.getRegistryWrapper(registries, RegistryKeys.CONFIGURED_FEATURE);
        RegistryEntry<ConfiguredFeature<?, ?>> deepslate = alwaysOwnedRegistryEntry(entries.ref(ModernBetaReducedHeightDataProvider.ORE_DEEPSLATE_OLD));
        RegistryEntry<ConfiguredFeature<?, ?>> tuff = registryConfiguredFeature.getOrThrow(OreConfiguredFeatures.ORE_TUFF);

        entries.add(OrePlacedFeatures.ORE_COAL_UPPER, new PlacedFeature(deepslate,
                modifiersWithCount(2, HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.aboveBottom(16)))));
        entries.add(OrePlacedFeatures.ORE_TUFF, new PlacedFeature(tuff,
                modifiersWithCount(2, HeightRangePlacementModifier.uniform(YOffset.aboveBottom(0), YOffset.aboveBottom(16)))));
    }

    @Override
    public String getName() {
        return "Deepslate Blobs Data";
    }
}
