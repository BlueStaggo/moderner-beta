package mod.bluestaggo.modernerbeta.fabric.data.provider.tag;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.BiomeSource;

import java.util.concurrent.CompletableFuture;

public class ModernBetaTagProviderBiomeSource extends FabricTagProvider<com.mojang.serialization.MapCodec<? extends BiomeSource>> {
    public ModernBetaTagProviderBiomeSource(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, Registries.BIOME_SOURCE, registryLookupFuture);
    }

    @Override
    protected void addTags(Provider registries) {
        this.builder(TagKey.create(registryKey, VersionCompat.id("lithostiched", "cannot_inject_into")))
            .add(ResourceKey.create(registryKey, ModernerBeta.createId(ModernerBeta.MOD_ID)));
    }
}
