package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.resources.ResourceLocation;

public record MiscConfig(
    boolean oldFogColorWeighting,
    ResourceLocation defaultSettingsPreset
) {
    public static final Codec<MiscConfig> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.BOOL.fieldOf("oldFogColorWeighting").orElse(true).forGetter(MiscConfig::oldFogColorWeighting),
            ResourceLocation.CODEC.fieldOf("defaultSettingsPreset").orElse(ModernerBeta.createId("beta")).forGetter(MiscConfig::defaultSettingsPreset)
        ).apply(instance, MiscConfig::new)
    );
}
