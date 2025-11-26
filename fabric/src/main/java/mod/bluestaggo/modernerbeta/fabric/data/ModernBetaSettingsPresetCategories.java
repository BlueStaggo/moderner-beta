package mod.bluestaggo.modernerbeta.fabric.data;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import mod.bluestaggo.modernerbeta.tags.ModernBetaSettingsPresetTags;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public final class ModernBetaSettingsPresetCategories {
    public static final ResourceKey<ModernBetaSettingsPresetCategory> BETA = keyOf("beta");
    public static final ResourceKey<ModernBetaSettingsPresetCategory> ALPHA_INFDEV = keyOf("alpha_infdev");
    public static final ResourceKey<ModernBetaSettingsPresetCategory> FINITE = keyOf("finite");
    public static final ResourceKey<ModernBetaSettingsPresetCategory> EARLY_RELEASE = keyOf("early_release");
    public static final ResourceKey<ModernBetaSettingsPresetCategory> EARLY_RELEASE_LARGE_BIOMES = keyOf("early_release_large_biomes");
    public static final ResourceKey<ModernBetaSettingsPresetCategory> EARLY_RELEASE_AMPLIFIED = keyOf("early_release_amplified");
    public static final ResourceKey<ModernBetaSettingsPresetCategory> MAJOR_RELEASE = keyOf("major_release");
    public static final ResourceKey<ModernBetaSettingsPresetCategory> BETA_CUSTOM = keyOf("beta_custom");
    public static final ResourceKey<ModernBetaSettingsPresetCategory> RELEASE_CUSTOM = keyOf("release_custom");

    public static void bootstrap(BootstrapContext<ModernBetaSettingsPresetCategory> context) {
        context.register(BETA, new ModernBetaSettingsPresetCategory(
            ModernBetaSettingsPresets.BETA_1_7_3.location(),
            BETA.location(),
            ModernBetaSettingsPresetTags.BETA
        ));
        context.register(ALPHA_INFDEV, new ModernBetaSettingsPresetCategory(
            ModernBetaSettingsPresets.ALPHA_1_1_2_01.location(),
            ALPHA_INFDEV.location(),
            ModernBetaSettingsPresetTags.ALPHA_INFDEV
        ));
        context.register(FINITE, new ModernBetaSettingsPresetCategory(
            ModernBetaSettingsPresets.INDEV.location(),
            FINITE.location(),
            ModernBetaSettingsPresetTags.FINITE
        ));
        context.register(EARLY_RELEASE, new ModernBetaSettingsPresetCategory(
            ModernBetaSettingsPresets.RELEASE_1_6_4.location(),
            EARLY_RELEASE.location(),
            ModernBetaSettingsPresetTags.EARLY_RELEASE
        ));
        context.register(EARLY_RELEASE_LARGE_BIOMES, new ModernBetaSettingsPresetCategory(
            ModernBetaSettingsPresets.RELEASE_1_1_LARGE_BIOMES.location(),
            EARLY_RELEASE_LARGE_BIOMES.location(),
            ModernBetaSettingsPresetTags.EARLY_RELEASE_LARGE_BIOMES
        ));
        context.register(EARLY_RELEASE_AMPLIFIED, new ModernBetaSettingsPresetCategory(
            ModernBetaSettingsPresets.RELEASE_1_1_AMPLIFIED.location(),
            EARLY_RELEASE_AMPLIFIED.location(),
            ModernBetaSettingsPresetTags.EARLY_RELEASE_AMPLIFIED
        ));
        context.register(MAJOR_RELEASE, new ModernBetaSettingsPresetCategory(
            ModernBetaSettingsPresets.RELEASE_1_12_2.location(),
            MAJOR_RELEASE.location(),
            ModernBetaSettingsPresetTags.MAJOR_RELEASE
        ));
        context.register(BETA_CUSTOM, new ModernBetaSettingsPresetCategory(
            ModernBetaSettingsPresets.BETA_SKYLANDS.location(),
            BETA_CUSTOM.location(),
            ModernBetaSettingsPresetTags.BETA_CUSTOM
        ));
        context.register(RELEASE_CUSTOM, new ModernBetaSettingsPresetCategory(
            ModernBetaSettingsPresets.ISLE_LAND.location(),
            RELEASE_CUSTOM.location(),
            ModernBetaSettingsPresetTags.RELEASE_CUSTOM
        ));
    }

    private static ResourceKey<ModernBetaSettingsPresetCategory> keyOf(String id) {
        return ResourceKey.create(ModernBetaResourceKeys.SETTINGS_PRESET_CATEGORY, ModernerBeta.createId(id));
    }
}
