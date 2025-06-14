package mod.bluestaggo.modernerbeta.tags;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistryKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import net.minecraft.block.Block;
import net.minecraft.registry.tag.TagKey;

public class ModernBetaSettingsPresetCategoryTags {
    public static final TagKey<ModernBetaSettingsPresetCategory> SELECTABLE = keyOf("selectable");

    private static TagKey<ModernBetaSettingsPresetCategory> keyOf(String id) {
        return TagKey.of(ModernBetaRegistryKeys.SETTINGS_PRESET_CATEGORY, ModernerBeta.createId(id));
    }
}
