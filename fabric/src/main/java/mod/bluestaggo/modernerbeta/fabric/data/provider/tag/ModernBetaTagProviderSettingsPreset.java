package mod.bluestaggo.modernerbeta.fabric.data.provider.tag;

import mod.bluestaggo.modernerbeta.fabric.data.ModernBetaSettingsPresets;
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
            ModernBetaSettingsPresets.BETA_1_7_3,
            ModernBetaSettingsPresets.BETA_1_1_02,
            ModernBetaSettingsPresets.SKYLANDS,
            ModernBetaSettingsPresets.PE
        );

        this.builder(ALPHA_INFDEV).add(
            ModernBetaSettingsPresets.ALPHA_1_1_2_01,
            ModernBetaSettingsPresets.ALPHA_WINTER,
            ModernBetaSettingsPresets.INFDEV_611,
            ModernBetaSettingsPresets.INFDEV_420,
            ModernBetaSettingsPresets.INFDEV_415,
            ModernBetaSettingsPresets.INFDEV_325,
            ModernBetaSettingsPresets.INFDEV_227
        );
        
        this.builder(FINITE).add(
            ModernBetaSettingsPresets.INDEV,
            ModernBetaSettingsPresets.INDEV_PARADISE,
            ModernBetaSettingsPresets.INDEV_WOODS,
            ModernBetaSettingsPresets.INDEV_HELL,
            ModernBetaSettingsPresets.CLASSIC_0_30,
            ModernBetaSettingsPresets.CLASSIC_0_0_14A_08
        );
        
        this.builder(EARLY_RELEASE).add(
            ModernBetaSettingsPresets.BETA_1_8_1,
            ModernBetaSettingsPresets.BETA_1_9_PRE_3,
            ModernBetaSettingsPresets.RELEASE_1_0_0,
            ModernBetaSettingsPresets.RELEASE_1_1,
            ModernBetaSettingsPresets.RELEASE_1_2_5,
            ModernBetaSettingsPresets.RELEASE_1_6_4
        );
        
        this.builder(EARLY_RELEASE_LARGE_BIOMES).add(
            ModernBetaSettingsPresets.BETA_1_8_1_LARGE_BIOMES,
            ModernBetaSettingsPresets.BETA_1_9_PRE_3_LARGE_BIOMES,
            ModernBetaSettingsPresets.RELEASE_1_0_0_LARGE_BIOMES,
            ModernBetaSettingsPresets.RELEASE_1_1_LARGE_BIOMES,
            ModernBetaSettingsPresets.RELEASE_1_2_5_LARGE_BIOMES,
            ModernBetaSettingsPresets.RELEASE_1_6_4_LARGE_BIOMES
        );
        
        this.builder(EARLY_RELEASE_AMPLIFIED).add(
            ModernBetaSettingsPresets.BETA_1_8_1_AMPLIFIED,
            ModernBetaSettingsPresets.BETA_1_9_PRE_3_AMPLIFIED,
            ModernBetaSettingsPresets.RELEASE_1_0_0_AMPLIFIED,
            ModernBetaSettingsPresets.RELEASE_1_1_AMPLIFIED,
            ModernBetaSettingsPresets.RELEASE_1_2_5_AMPLIFIED,
            ModernBetaSettingsPresets.RELEASE_1_6_4_AMPLIFIED
        );
        
        this.builder(MAJOR_RELEASE).add(
            ModernBetaSettingsPresets.RELEASE_1_12_2,
            ModernBetaSettingsPresets.RELEASE_1_12_2_LARGE_BIOMES,
            ModernBetaSettingsPresets.RELEASE_1_12_2_AMPLIFIED,
            ModernBetaSettingsPresets.BEDROCK_1_2,
            ModernBetaSettingsPresets.RELEASE_1_17_1,
            ModernBetaSettingsPresets.RELEASE_1_17_1_LARGE_BIOMES,
            ModernBetaSettingsPresets.RELEASE_1_17_1_AMPLIFIED,
            ModernBetaSettingsPresets.BEDROCK_1_17
        );
        
        this.builder(BETA_CUSTOM).add(
            ModernBetaSettingsPresets.BETA_SKYLANDS,
            ModernBetaSettingsPresets.BETA_ISLES,
            ModernBetaSettingsPresets.BETA_WATER_WORLD,
            ModernBetaSettingsPresets.BETA_ISLE_LAND,
            ModernBetaSettingsPresets.BETA_CAVE_DELIGHT,
            ModernBetaSettingsPresets.BETA_MOUNTAIN_MADNESS,
            ModernBetaSettingsPresets.BETA_DROUGHT,
            ModernBetaSettingsPresets.BETA_CAVE_CHAOS,
            ModernBetaSettingsPresets.BETA_LARGE_BIOMES,
            ModernBetaSettingsPresets.BETA_XBOX_LEGACY,
            ModernBetaSettingsPresets.BETA_SURVIVAL_ISLAND,
            ModernBetaSettingsPresets.BETA_VANILLA
        );
        
        this.builder(RELEASE_CUSTOM).add(
            ModernBetaSettingsPresets.SNOW_AINT_SNOWIER,
            ModernBetaSettingsPresets.SNOW_AINT_SNOWIER_LARGE_BIOMES,
            ModernBetaSettingsPresets.SNOW_AINT_SNOWIER_AMPLIFIED,
            ModernBetaSettingsPresets.RELEASE_HYBRID,
            ModernBetaSettingsPresets.RELEASE_HYBRID_LARGE_BIOMES,
            ModernBetaSettingsPresets.RELEASE_HYBRID_AMPLIFIED,
            ModernBetaSettingsPresets.WATER_WORLD,
            ModernBetaSettingsPresets.ISLE_LAND,
            ModernBetaSettingsPresets.CAVE_DELIGHT,
            ModernBetaSettingsPresets.MOUNTAIN_MADNESS,
            ModernBetaSettingsPresets.DROUGHT,
            ModernBetaSettingsPresets.CAVE_CHAOS,
            ModernBetaSettingsPresets.LEGACY_CONSOLE_CLASSIC,
            ModernBetaSettingsPresets.LEGACY_CONSOLE_SMALL,
            ModernBetaSettingsPresets.LEGACY_CONSOLE_MEDIUM,
            ModernBetaSettingsPresets.LEGACY_CONSOLE_LARGE
        );
    }
}
