package mod.bluestaggo.modernerbeta.fabric.data;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.WorldPresetTags;
import net.minecraft.world.level.levelgen.presets.WorldPreset;

import java.util.concurrent.CompletableFuture;

public class ModernBetaTagProviderWorldPreset extends FabricTagProvider<WorldPreset> {
    public static final ResourceKey<WorldPreset> MODERN_BETA = keyOf(ModernerBeta.MOD_ID);
    
    public ModernBetaTagProviderWorldPreset(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.WORLD_PRESET, registriesFuture);
    }

    @Override
    protected void addTags(Provider provider) {
        this.builder(WorldPresetTags.NORMAL)
            .add(MODERN_BETA);
    }
    
    private static ResourceKey<WorldPreset> keyOf(String id) {
        return ResourceKey.create(Registries.WORLD_PRESET, ModernerBeta.createId(ModernerBeta.MOD_ID));
    }
}
