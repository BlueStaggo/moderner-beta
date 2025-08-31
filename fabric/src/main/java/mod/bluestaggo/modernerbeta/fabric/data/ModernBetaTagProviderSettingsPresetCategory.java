package mod.bluestaggo.modernerbeta.fabric.data;

import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistryKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

import java.util.concurrent.CompletableFuture;

import static mod.bluestaggo.modernerbeta.tags.ModernBetaSettingsPresetCategoryTags.SELECTABLE;

public class ModernBetaTagProviderSettingsPresetCategory extends FabricTagProvider<ModernBetaSettingsPresetCategory> {
    public ModernBetaTagProviderSettingsPresetCategory(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
        super(output, ModernBetaRegistryKeys.SETTINGS_PRESET_CATEGORY, registriesFuture);
    }

    @Override
    protected void configure(WrapperLookup lookup) {
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
