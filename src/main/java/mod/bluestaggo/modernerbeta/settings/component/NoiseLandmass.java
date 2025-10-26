package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;

public record NoiseLandmass(
    float variationScale,
    float depthInfluence,
    float negativeDepthInfluence,
    boolean negativeDepthFlattening,
    float depthStretch,
    float depthOffset,
    float positiveDepthDampening,
    float negativeDepthDampening,
    float minDepth,
    float maxDepth
) {
    public static final Codec<NoiseLandmass> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.FLOAT.fieldOf("variationScale").orElse(1.121f).forGetter(NoiseLandmass::variationScale),
            Codec.FLOAT.fieldOf("depthInfluence").orElse(1.0f).forGetter(NoiseLandmass::depthInfluence),
            Codec.FLOAT.fieldOf("negativeDepthInfluence").orElse(0.3f).forGetter(NoiseLandmass::negativeDepthInfluence),
            Codec.BOOL.fieldOf("negativeDepthFlattening").orElse(true).forGetter(NoiseLandmass::negativeDepthFlattening),
            Codec.FLOAT.fieldOf("depthStretch").orElse(3.0f).forGetter(NoiseLandmass::depthStretch),
            Codec.FLOAT.fieldOf("depthOffset").orElse(-2.0f).forGetter(NoiseLandmass::depthOffset),
            Codec.FLOAT.fieldOf("positiveDepthDampening").orElse(8.0f).forGetter(NoiseLandmass::positiveDepthDampening),
            Codec.FLOAT.fieldOf("negativeDepthDampening").orElse(5.6f).forGetter(NoiseLandmass::negativeDepthDampening),
            Codec.FLOAT.fieldOf("minDepth").orElse(-1.0f / 2.8f).forGetter(NoiseLandmass::minDepth),
            Codec.FLOAT.fieldOf("maxDepth").orElse(1.0f / 8.0f).forGetter(NoiseLandmass::maxDepth)
        ).apply(instance, NoiseLandmass::new)
    );
    public static final NoiseLandmass DEFAULT = CodecUtil.getDefaultByMap(CODEC);

    public static final NoiseLandmass INFDEV611 = new NoiseLandmass(
        1.0f,
        1.0f,
        1.0f,
        true,
        3.0f,
        -3.0f,
        6.0f,
        2.8f,
        -1.0f / 1.4f,
        1.0f / 6.0f
    );
    public static final NoiseLandmass ALPHA = new NoiseLandmass(
        1.0f,
        1.0f,
        1.0f,
        true,
        3.0f,
        -3.0f,
        6.0f,
        5.6f,
        -1.0f / 2.8f,
        1.0f / 6.0f
    );
    public static final NoiseLandmass RELEASE = new NoiseLandmass(
        1.121f,
        0.2f,
        0.3f,
        false,
        3.0f,
        -2.0f,
        8.0f,
        5.6f,
        -1.0f / 2.8f,
        1.0f / 8.0f
    );
}
