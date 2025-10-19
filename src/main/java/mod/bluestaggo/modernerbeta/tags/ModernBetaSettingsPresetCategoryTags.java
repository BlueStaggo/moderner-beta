package mod.bluestaggo.modernerbeta.tags;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import net.minecraft.tags.TagKey;

public class ModernBetaSettingsPresetCategoryTags {
    public static final TagKey<ModernBetaSettingsPresetCategory> SELECTABLE = keyOf("selectable");

    private static TagKey<ModernBetaSettingsPresetCategory> keyOf(String id) {
        return TagKey.create(ModernBetaResourceKeys.SETTINGS_PRESET_CATEGORY, ModernerBeta.createId(id));
    }
}
