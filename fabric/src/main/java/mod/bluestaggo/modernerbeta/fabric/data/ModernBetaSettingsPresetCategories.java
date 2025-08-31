package mod.bluestaggo.modernerbeta.fabric.data;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistryKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;

import java.util.List;

public final class ModernBetaSettingsPresetCategories {
    public static final RegistryKey<ModernBetaSettingsPresetCategory> BETA = keyOf("beta");
    public static final RegistryKey<ModernBetaSettingsPresetCategory> ALPHA_INFDEV = keyOf("alpha_infdev");
    public static final RegistryKey<ModernBetaSettingsPresetCategory> FINITE = keyOf("finite");
    public static final RegistryKey<ModernBetaSettingsPresetCategory> EARLY_RELEASE = keyOf("early_release");
    public static final RegistryKey<ModernBetaSettingsPresetCategory> EARLY_RELEASE_LARGE_BIOMES = keyOf("early_release_large_biomes");
    public static final RegistryKey<ModernBetaSettingsPresetCategory> EARLY_RELEASE_AMPLIFIED = keyOf("early_release_amplified");
    public static final RegistryKey<ModernBetaSettingsPresetCategory> MAJOR_RELEASE = keyOf("major_release");
    public static final RegistryKey<ModernBetaSettingsPresetCategory> BETA_CUSTOM = keyOf("beta_custom");
    public static final RegistryKey<ModernBetaSettingsPresetCategory> RELEASE_CUSTOM = keyOf("release_custom");

    public static void bootstrap(Registerable<ModernBetaSettingsPresetCategory> categoryRegisterable) {
        categoryRegisterable.register(BETA, new ModernBetaSettingsPresetCategory(
            ModernBetaBuiltInTypes.Preset.BETA_1_7_3.id,
            List.of(
                ModernBetaBuiltInTypes.Preset.BETA_1_7_3.id,
                ModernBetaBuiltInTypes.Preset.BETA_1_1_02.id,
                ModernBetaBuiltInTypes.Preset.SKYLANDS.id,
                ModernBetaBuiltInTypes.Preset.PE.id
            )
        ));
        categoryRegisterable.register(ALPHA_INFDEV, new ModernBetaSettingsPresetCategory(
            ModernBetaBuiltInTypes.Preset.ALPHA_1_1_2_01.id,
            List.of(
                ModernBetaBuiltInTypes.Preset.ALPHA_1_1_2_01.id,
                ModernBetaBuiltInTypes.Preset.ALPHA_WINTER.id,
                ModernBetaBuiltInTypes.Preset.INFDEV_611.id,
                ModernBetaBuiltInTypes.Preset.INFDEV_420.id,
                ModernBetaBuiltInTypes.Preset.INFDEV_415.id,
                ModernBetaBuiltInTypes.Preset.INFDEV_325.id,
                ModernBetaBuiltInTypes.Preset.INFDEV_227.id
            )
        ));
        categoryRegisterable.register(FINITE, new ModernBetaSettingsPresetCategory(
            ModernBetaBuiltInTypes.Preset.INDEV.id,
            List.of(
                ModernBetaBuiltInTypes.Preset.INDEV.id,
                ModernBetaBuiltInTypes.Preset.INDEV_PARADISE.id,
                ModernBetaBuiltInTypes.Preset.INDEV_WOODS.id,
                ModernBetaBuiltInTypes.Preset.INDEV_HELL.id,
                ModernBetaBuiltInTypes.Preset.CLASSIC_0_30.id,
                ModernBetaBuiltInTypes.Preset.CLASSIC_0_0_14A_08.id
            )
        ));
        categoryRegisterable.register(EARLY_RELEASE, new ModernBetaSettingsPresetCategory(
            ModernBetaBuiltInTypes.Preset.RELEASE_1_6_4.id,
            List.of(
                ModernBetaBuiltInTypes.Preset.BETA_1_8_1.id,
                ModernBetaBuiltInTypes.Preset.BETA_1_9_PRE_3.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_0_0.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_1.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_2_5.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_6_4.id
            )
        ));
        categoryRegisterable.register(EARLY_RELEASE_LARGE_BIOMES, new ModernBetaSettingsPresetCategory(
            ModernBetaBuiltInTypes.Preset.RELEASE_1_1_LARGE_BIOMES.id,
            List.of(
                ModernBetaBuiltInTypes.Preset.BETA_1_8_1_LARGE_BIOMES.id,
                ModernBetaBuiltInTypes.Preset.BETA_1_9_PRE_3_LARGE_BIOMES.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_0_0_LARGE_BIOMES.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_1_LARGE_BIOMES.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_2_5_LARGE_BIOMES.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_6_4_LARGE_BIOMES.id
            )
        ));
        categoryRegisterable.register(EARLY_RELEASE_AMPLIFIED, new ModernBetaSettingsPresetCategory(
            ModernBetaBuiltInTypes.Preset.RELEASE_1_1_AMPLIFIED.id,
            List.of(
                ModernBetaBuiltInTypes.Preset.BETA_1_8_1_AMPLIFIED.id,
                ModernBetaBuiltInTypes.Preset.BETA_1_9_PRE_3_AMPLIFIED.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_0_0_AMPLIFIED.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_1_AMPLIFIED.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_2_5_AMPLIFIED.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_6_4_AMPLIFIED.id
            )
        ));
        categoryRegisterable.register(MAJOR_RELEASE, new ModernBetaSettingsPresetCategory(
            ModernBetaBuiltInTypes.Preset.RELEASE_1_12_2.id,
            List.of(
                ModernBetaBuiltInTypes.Preset.RELEASE_1_12_2.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_12_2_LARGE_BIOMES.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_12_2_AMPLIFIED.id,
                ModernBetaBuiltInTypes.Preset.BEDROCK_1_2.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_17_1.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_17_1_LARGE_BIOMES.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_17_1_AMPLIFIED.id,
                ModernBetaBuiltInTypes.Preset.BEDROCK_1_17.id
            )
        ));
        categoryRegisterable.register(BETA_CUSTOM, new ModernBetaSettingsPresetCategory(
            ModernBetaBuiltInTypes.Preset.BETA_SKYLANDS.id,
            List.of(
                ModernBetaBuiltInTypes.Preset.BETA_SKYLANDS.id,
                ModernBetaBuiltInTypes.Preset.BETA_ISLES.id,
                ModernBetaBuiltInTypes.Preset.BETA_WATER_WORLD.id,
                ModernBetaBuiltInTypes.Preset.BETA_ISLE_LAND.id,
                ModernBetaBuiltInTypes.Preset.BETA_CAVE_DELIGHT.id,
                ModernBetaBuiltInTypes.Preset.BETA_MOUNTAIN_MADNESS.id,
                ModernBetaBuiltInTypes.Preset.BETA_DROUGHT.id,
                ModernBetaBuiltInTypes.Preset.BETA_CAVE_CHAOS.id,
                ModernBetaBuiltInTypes.Preset.BETA_LARGE_BIOMES.id,
                ModernBetaBuiltInTypes.Preset.BETA_XBOX_LEGACY.id,
                ModernBetaBuiltInTypes.Preset.BETA_SURVIVAL_ISLAND.id,
                ModernBetaBuiltInTypes.Preset.BETA_VANILLA.id
            )
        ));
        categoryRegisterable.register(RELEASE_CUSTOM, new ModernBetaSettingsPresetCategory(
            ModernBetaBuiltInTypes.Preset.ISLE_LAND.id,
            List.of(
                ModernBetaBuiltInTypes.Preset.SNOW_AINT_SNOWIER.id,
                ModernBetaBuiltInTypes.Preset.SNOW_AINT_SNOWIER_LARGE_BIOMES.id,
                ModernBetaBuiltInTypes.Preset.SNOW_AINT_SNOWIER_AMPLIFIED.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_HYBRID.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_HYBRID_LARGE_BIOMES.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_HYBRID_AMPLIFIED.id,
                ModernBetaBuiltInTypes.Preset.WATER_WORLD.id,
                ModernBetaBuiltInTypes.Preset.ISLE_LAND.id,
                ModernBetaBuiltInTypes.Preset.CAVE_DELIGHT.id,
                ModernBetaBuiltInTypes.Preset.MOUNTAIN_MADNESS.id,
                ModernBetaBuiltInTypes.Preset.DROUGHT.id,
                ModernBetaBuiltInTypes.Preset.CAVE_CHAOS.id,
                ModernBetaBuiltInTypes.Preset.LEGACY_CONSOLE_CLASSIC.id,
                ModernBetaBuiltInTypes.Preset.LEGACY_CONSOLE_SMALL.id,
                ModernBetaBuiltInTypes.Preset.LEGACY_CONSOLE_MEDIUM.id,
                ModernBetaBuiltInTypes.Preset.LEGACY_CONSOLE_LARGE.id
            )
        ));
    }

    private static RegistryKey<ModernBetaSettingsPresetCategory> keyOf(String id) {
        return RegistryKey.of(ModernBetaRegistryKeys.SETTINGS_PRESET_CATEGORY, ModernerBeta.createId(id));
    }
}
