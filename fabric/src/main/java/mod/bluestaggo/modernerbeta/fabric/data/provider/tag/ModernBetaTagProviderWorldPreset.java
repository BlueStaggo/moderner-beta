package mod.bluestaggo.modernerbeta.fabric.data.provider.tag;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.WorldPresetTags;
import net.minecraft.world.level.levelgen.presets.WorldPreset;

import java.util.concurrent.CompletableFuture;

public class ModernBetaTagProviderWorldPreset extends FabricTagsProvider<WorldPreset> {
    public static final ResourceKey<WorldPreset> MODERN_BETA = keyOf(ModernerBeta.MOD_ID);
    
    public ModernBetaTagProviderWorldPreset(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.WORLD_PRESET, registriesFuture);
    }

    @Override
    protected void addTags(Provider provider) {
        this.builder(WorldPresetTags.NORMAL)
            .add(MODERN_BETA);
    }
    
    private static ResourceKey<WorldPreset> keyOf(String id) {
        return ResourceKey.create(Registries.WORLD_PRESET, ModernerBeta.createId(id));
    }
}
