//~dotLocation
package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresets;
import net.minecraft.resources.Identifier;

public record MiscConfig(
    boolean oldFogColorWeighting,
    Identifier defaultSettingsPreset
) {
    public static final Codec<MiscConfig> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.BOOL.fieldOf("oldFogColorWeighting").orElse(true).forGetter(MiscConfig::oldFogColorWeighting),
            Identifier.CODEC.fieldOf("defaultSettingsPreset").orElse(ModernBetaSettingsPresets.BETA_1_7_3.identifier()).forGetter(MiscConfig::defaultSettingsPreset)
        ).apply(instance, MiscConfig::new)
    );
}
