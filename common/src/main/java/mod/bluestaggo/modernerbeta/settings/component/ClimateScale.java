package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;

public record ClimateScale(
    float temp,
    float rain,
    float detail,
    float weird
) {
    public static Codec<ClimateScale> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.FLOAT.fieldOf("temp").orElse(0.025f).forGetter(ClimateScale::temp),
            Codec.FLOAT.fieldOf("rain").orElse(0.05f).forGetter(ClimateScale::rain),
            Codec.FLOAT.fieldOf("detail").orElse(0.25f).forGetter(ClimateScale::detail),
            Codec.FLOAT.fieldOf("weird").orElse(0.003125f).forGetter(ClimateScale::weird)
        ).apply(instance, ClimateScale::new)
    );
    public static final ClimateScale DEFAULT = CodecUtil.getDefaultByMap(CODEC);
}
