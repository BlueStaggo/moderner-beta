package mod.bluestaggo.modernerbeta.tags;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import net.minecraft.tags.TagKey;

public class ModernBetaSettingsPresetTags {
    public static final TagKey<ModernBetaSettingsPreset> BETA = keyOf("beta");
    public static final TagKey<ModernBetaSettingsPreset> ALPHA_INFDEV = keyOf("alpha_infdev");
    public static final TagKey<ModernBetaSettingsPreset> FINITE = keyOf("finite");
    public static final TagKey<ModernBetaSettingsPreset> EARLY_RELEASE = keyOf("early_release");
    public static final TagKey<ModernBetaSettingsPreset> EARLY_RELEASE_LARGE_BIOMES = keyOf("early_release_large_biomes");
    public static final TagKey<ModernBetaSettingsPreset> EARLY_RELEASE_AMPLIFIED = keyOf("early_release_amplified");
    public static final TagKey<ModernBetaSettingsPreset> MAJOR_RELEASE = keyOf("major_release");
    public static final TagKey<ModernBetaSettingsPreset> BETA_CUSTOM = keyOf("beta_custom");
    public static final TagKey<ModernBetaSettingsPreset> RELEASE_CUSTOM = keyOf("release_custom");

    private static TagKey<ModernBetaSettingsPreset> keyOf(String id) {
        return TagKey.create(ModernBetaResourceKeys.SETTINGS_PRESET, ModernerBeta.createId(id));
    }
}
