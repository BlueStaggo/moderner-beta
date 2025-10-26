package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;

public record NoiseLandmass(
    boolean enabled,
    float variationScale,
    float depthInfluence,
    float negativeDepthInfluence,
    float depthStretch,
    float depthOffset,
    float positiveDepthDampening,
    float negativeDepthDampening,
    float minDepth,
    float maxDepth,
    boolean negativeDepthFlattening
) {
    public static final Codec<NoiseLandmass> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.BOOL.fieldOf("enabled").orElse(true).forGetter(NoiseLandmass::enabled),
            Codec.FLOAT.fieldOf("variationScale").orElse(1.121f).forGetter(NoiseLandmass::variationScale),
            Codec.FLOAT.fieldOf("depthInfluence").orElse(1.0f).forGetter(NoiseLandmass::depthInfluence),
            Codec.FLOAT.fieldOf("negativeDepthInfluence").orElse(0.3f).forGetter(NoiseLandmass::negativeDepthInfluence),
            Codec.FLOAT.fieldOf("depthStretch").orElse(3.0f).forGetter(NoiseLandmass::depthStretch),
            Codec.FLOAT.fieldOf("depthOffset").orElse(-2.0f).forGetter(NoiseLandmass::depthOffset),
            Codec.FLOAT.fieldOf("positiveDepthDampening").orElse(8.0f).forGetter(NoiseLandmass::positiveDepthDampening),
            Codec.FLOAT.fieldOf("negativeDepthDampening").orElse(5.6f).forGetter(NoiseLandmass::negativeDepthDampening),
            Codec.FLOAT.fieldOf("minDepth").orElse(-1.0f / 2.8f).forGetter(NoiseLandmass::minDepth),
            Codec.FLOAT.fieldOf("maxDepth").orElse(1.0f / 8.0f).forGetter(NoiseLandmass::maxDepth),
            Codec.BOOL.fieldOf("negativeDepthFlattening").orElse(true).forGetter(NoiseLandmass::negativeDepthFlattening)
        ).apply(instance, NoiseLandmass::new)
    );
    public static final NoiseLandmass DEFAULT = CodecUtil.getDefaultByMap(CODEC);

    public static final NoiseLandmass DISABLED = new NoiseLandmass(
        false,
        1.0f,
        1.0f,
        1.0f,
        3.0f,
        -3.0f,
        6.0f,
        2.8f,
        -1.0f / 1.4f,
        1.0f / 6.0f,
        true
    );
    public static final NoiseLandmass INFDEV_611 = new NoiseLandmass(
        true,
        1.0f,
        1.0f,
        1.0f,
        3.0f,
        -3.0f,
        6.0f,
        2.8f,
        -1.0f / 1.4f,
        1.0f / 6.0f,
        true
    );
    public static final NoiseLandmass ALPHA = new NoiseLandmass(
        true,
        1.0f,
        1.0f,
        1.0f,
        3.0f,
        -3.0f,
        6.0f,
        5.6f,
        -1.0f / 2.8f,
        1.0f / 6.0f,
        true
    );
    public static final NoiseLandmass RELEASE = new NoiseLandmass(
        true,
        1.121f,
        0.2f,
        0.3f,
        3.0f,
        -2.0f,
        8.0f,
        5.6f,
        -1.0f / 2.8f,
        1.0f / 8.0f,
        false
    );
}
