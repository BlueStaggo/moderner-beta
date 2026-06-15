package mod.bluestaggo.modernerbeta.fabric.data.provider.tag;

import mod.bluestaggo.modernerbeta.fabric.data.ModernBetaSettingsPresetCategories;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup.Provider;

import java.util.concurrent.CompletableFuture;

import static mod.bluestaggo.modernerbeta.tags.ModernBetaSettingsPresetCategoryTags.SELECTABLE;

public class ModernBetaTagProviderSettingsPresetCategory extends FabricTagsProvider<ModernBetaSettingsPresetCategory> {
    public ModernBetaTagProviderSettingsPresetCategory(FabricPackOutput output, CompletableFuture<Provider> registriesFuture) {
        super(output, ModernBetaResourceKeys.SETTINGS_PRESET_CATEGORY, registriesFuture);
    }

    @Override
    protected void addTags(Provider provider) {
        this.builder(SELECTABLE).add(
            ModernBetaSettingsPresetCategories.BETA,
            ModernBetaSettingsPresetCategories.ALPHA_INFDEV,
            ModernBetaSettingsPresetCategories.FINITE,
            ModernBetaSettingsPresetCategories.EARLY_RELEASE,
            ModernBetaSettingsPresetCategories.EARLY_RELEASE_LARGE_BIOMES,
            ModernBetaSettingsPresetCategories.EARLY_RELEASE_AMPLIFIED,
            ModernBetaSettingsPresetCategories.MAJOR_RELEASE,
            ModernBetaSettingsPresetCategories.BETA_CUSTOM,
            ModernBetaSettingsPresetCategories.RELEASE_CUSTOM
        );
    }
}
