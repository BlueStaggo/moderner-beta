package mod.bluestaggo.modernerbeta.fabric.data.reduced_height.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.concurrent.CompletableFuture;

import static mod.bluestaggo.modernerbeta.fabric.data.reduced_height.provider.ModernBetaReducedHeightDataProvider.alwaysSerializableHolder;
import static mod.bluestaggo.modernerbeta.fabric.data.reduced_height.provider.ModernBetaReducedHeightDataProvider.modifiersWithCount;

public class ModernBetaDeepslateBlobsDataProvider extends FabricDynamicRegistryProvider {
    public ModernBetaDeepslateBlobsDataProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        HolderLookup.RegistryLookup<ConfiguredFeature<?, ?>> registryConfiguredFeature = provider.lookupOrThrow(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> deepslate = alwaysSerializableHolder(entries.ref(ModernBetaReducedHeightDataProvider.ORE_DEEPSLATE_OLD));
        Holder<ConfiguredFeature<?, ?>> tuff = registryConfiguredFeature.getOrThrow(OreFeatures.ORE_TUFF);

        entries.add(OrePlacements.ORE_COAL_UPPER, new PlacedFeature(deepslate,
                modifiersWithCount(2, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(16)))));
        entries.add(OrePlacements.ORE_TUFF, new PlacedFeature(tuff,
                modifiersWithCount(2, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(16)))));
    }

    @Override
    public String getName() {
        return "Deepslate Blobs Data";
    }
}
