package mod.bluestaggo.modernerbeta.settings;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record ModernBetaSettingsPresetCategory(ResourceLocation defaultIcon, List<ResourceLocation> presets) {
    public static final Codec<ModernBetaSettingsPresetCategory> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("defaultIcon").forGetter(ModernBetaSettingsPresetCategory::defaultIcon),
            ResourceLocation.CODEC.listOf().fieldOf("presets").forGetter(ModernBetaSettingsPresetCategory::presets)
        ).apply(instance, ModernBetaSettingsPresetCategory::new)
    );
}
