package mod.bluestaggo.modernerbeta.fabric.data;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

import static mod.bluestaggo.modernerbeta.tags.ModernBetaSettingsPresetTags.*;

public class ModernBetaTagProviderSettingsPreset extends FabricTagProvider<ModernBetaSettingsPreset> {
    public ModernBetaTagProviderSettingsPreset(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, ModernBetaResourceKeys.SETTINGS_PRESET, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.builder(BETA).add(
            ModernBetaBuiltInTypes.Preset.BETA_1_7_3.key,
            ModernBetaBuiltInTypes.Preset.BETA_1_1_02.key,
            ModernBetaBuiltInTypes.Preset.SKYLANDS.key,
            ModernBetaBuiltInTypes.Preset.PE.key
        );

        this.builder(ALPHA_INFDEV).add(
            ModernBetaBuiltInTypes.Preset.ALPHA_1_1_2_01.key,
            ModernBetaBuiltInTypes.Preset.ALPHA_WINTER.key,
            ModernBetaBuiltInTypes.Preset.INFDEV_611.key,
            ModernBetaBuiltInTypes.Preset.INFDEV_420.key,
            ModernBetaBuiltInTypes.Preset.INFDEV_415.key,
            ModernBetaBuiltInTypes.Preset.INFDEV_325.key,
            ModernBetaBuiltInTypes.Preset.INFDEV_227.key
        );
        
        this.builder(FINITE).add(
            ModernBetaBuiltInTypes.Preset.INDEV.key,
            ModernBetaBuiltInTypes.Preset.INDEV_PARADISE.key,
            ModernBetaBuiltInTypes.Preset.INDEV_WOODS.key,
            ModernBetaBuiltInTypes.Preset.INDEV_HELL.key,
            ModernBetaBuiltInTypes.Preset.CLASSIC_0_30.key,
            ModernBetaBuiltInTypes.Preset.CLASSIC_0_0_14A_08.key
        );
        
        this.builder(EARLY_RELEASE).add(
            ModernBetaBuiltInTypes.Preset.BETA_1_8_1.key,
            ModernBetaBuiltInTypes.Preset.BETA_1_9_PRE_3.key,
            ModernBetaBuiltInTypes.Preset.RELEASE_1_0_0.key,
            ModernBetaBuiltInTypes.Preset.RELEASE_1_1.key,
            ModernBetaBuiltInTypes.Preset.RELEASE_1_2_5.key,
            ModernBetaBuiltInTypes.Preset.RELEASE_1_6_4.key
        );
        
        this.builder(EARLY_RELEASE_LARGE_BIOMES).add(
            ModernBetaBuiltInTypes.Preset.BETA_1_8_1_LARGE_BIOMES.key,
            ModernBetaBuiltInTypes.Preset.BETA_1_9_PRE_3_LARGE_BIOMES.key,
            ModernBetaBuiltInTypes.Preset.RELEASE_1_0_0_LARGE_BIOMES.key,
            ModernBetaBuiltInTypes.Preset.RELEASE_1_1_LARGE_BIOMES.key,
            ModernBetaBuiltInTypes.Preset.RELEASE_1_2_5_LARGE_BIOMES.key,
            ModernBetaBuiltInTypes.Preset.RELEASE_1_6_4_LARGE_BIOMES.key
        );
        
        this.builder(EARLY_RELEASE_AMPLIFIED).add(
            ModernBetaBuiltInTypes.Preset.BETA_1_8_1_AMPLIFIED.key,
            ModernBetaBuiltInTypes.Preset.BETA_1_9_PRE_3_AMPLIFIED.key,
            ModernBetaBuiltInTypes.Preset.RELEASE_1_0_0_AMPLIFIED.key,
            ModernBetaBuiltInTypes.Preset.RELEASE_1_1_AMPLIFIED.key,
            ModernBetaBuiltInTypes.Preset.RELEASE_1_2_5_AMPLIFIED.key,
            ModernBetaBuiltInTypes.Preset.RELEASE_1_6_4_AMPLIFIED.key
        );
        
        this.builder(MAJOR_RELEASE).add(
            ModernBetaBuiltInTypes.Preset.RELEASE_1_12_2.key,
            ModernBetaBuiltInTypes.Preset.RELEASE_1_12_2_LARGE_BIOMES.key,
            ModernBetaBuiltInTypes.Preset.RELEASE_1_12_2_AMPLIFIED.key,
            ModernBetaBuiltInTypes.Preset.BEDROCK_1_2.key,
            ModernBetaBuiltInTypes.Preset.RELEASE_1_17_1.key,
            ModernBetaBuiltInTypes.Preset.RELEASE_1_17_1_LARGE_BIOMES.key,
            ModernBetaBuiltInTypes.Preset.RELEASE_1_17_1_AMPLIFIED.key,
            ModernBetaBuiltInTypes.Preset.BEDROCK_1_17.key
        );
        
        this.builder(BETA_CUSTOM).add(
            ModernBetaBuiltInTypes.Preset.BETA_SKYLANDS.key,
            ModernBetaBuiltInTypes.Preset.BETA_ISLES.key,
            ModernBetaBuiltInTypes.Preset.BETA_WATER_WORLD.key,
            ModernBetaBuiltInTypes.Preset.BETA_ISLE_LAND.key,
            ModernBetaBuiltInTypes.Preset.BETA_CAVE_DELIGHT.key,
            ModernBetaBuiltInTypes.Preset.BETA_MOUNTAIN_MADNESS.key,
            ModernBetaBuiltInTypes.Preset.BETA_DROUGHT.key,
            ModernBetaBuiltInTypes.Preset.BETA_CAVE_CHAOS.key,
            ModernBetaBuiltInTypes.Preset.BETA_LARGE_BIOMES.key,
            ModernBetaBuiltInTypes.Preset.BETA_XBOX_LEGACY.key,
            ModernBetaBuiltInTypes.Preset.BETA_SURVIVAL_ISLAND.key,
            ModernBetaBuiltInTypes.Preset.BETA_VANILLA.key
        );
        
        this.builder(RELEASE_CUSTOM).add(
            ModernBetaBuiltInTypes.Preset.SNOW_AINT_SNOWIER.key,
            ModernBetaBuiltInTypes.Preset.SNOW_AINT_SNOWIER_LARGE_BIOMES.key,
            ModernBetaBuiltInTypes.Preset.SNOW_AINT_SNOWIER_AMPLIFIED.key,
            ModernBetaBuiltInTypes.Preset.RELEASE_HYBRID.key,
            ModernBetaBuiltInTypes.Preset.RELEASE_HYBRID_LARGE_BIOMES.key,
            ModernBetaBuiltInTypes.Preset.RELEASE_HYBRID_AMPLIFIED.key,
            ModernBetaBuiltInTypes.Preset.WATER_WORLD.key,
            ModernBetaBuiltInTypes.Preset.ISLE_LAND.key,
            ModernBetaBuiltInTypes.Preset.CAVE_DELIGHT.key,
            ModernBetaBuiltInTypes.Preset.MOUNTAIN_MADNESS.key,
            ModernBetaBuiltInTypes.Preset.DROUGHT.key,
            ModernBetaBuiltInTypes.Preset.CAVE_CHAOS.key,
            ModernBetaBuiltInTypes.Preset.LEGACY_CONSOLE_CLASSIC.key,
            ModernBetaBuiltInTypes.Preset.LEGACY_CONSOLE_SMALL.key,
            ModernBetaBuiltInTypes.Preset.LEGACY_CONSOLE_MEDIUM.key,
            ModernBetaBuiltInTypes.Preset.LEGACY_CONSOLE_LARGE.key
        );
    }
}
