package mod.bluestaggo.modernerbeta.settings;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

import java.util.List;

public record ModernBetaSettingsPresetCategory(Identifier defaultIcon, List<Identifier> presets) {
    public static final Codec<ModernBetaSettingsPresetCategory> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Identifier.CODEC.fieldOf("defaultIcon").forGetter(ModernBetaSettingsPresetCategory::defaultIcon),
            Identifier.CODEC.listOf().fieldOf("presets").forGetter(ModernBetaSettingsPresetCategory::presets)
        ).apply(instance, ModernBetaSettingsPresetCategory::new)
    );
}
