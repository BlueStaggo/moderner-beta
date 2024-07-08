package mod.bluestaggo.modernerbeta.fabric.data;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.WorldPresetTags;
import net.minecraft.world.gen.WorldPreset;

import java.util.concurrent.CompletableFuture;

public class ModernBetaTagProviderWorldPreset extends FabricTagProvider<WorldPreset> {
    public static final RegistryKey<WorldPreset> MODERN_BETA = keyOf(ModernerBeta.MOD_ID);
    
    public ModernBetaTagProviderWorldPreset(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.WORLD_PRESET, registriesFuture);
    }

    @Override
    protected void configure(WrapperLookup lookup) {
        getOrCreateTagBuilder(WorldPresetTags.NORMAL)
            .add(MODERN_BETA);
    }
    
    private static RegistryKey<WorldPreset> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.WORLD_PRESET, ModernerBeta.createId(ModernerBeta.MOD_ID));
    }
}
