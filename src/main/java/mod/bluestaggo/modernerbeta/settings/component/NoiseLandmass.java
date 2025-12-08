package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;

public record NoiseLandmass(
    DepthSettings depth,
    ScaleSettings scale,
    boolean alphaSampling
) {
    public static final Codec<NoiseLandmass> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            DepthSettings.CODEC.fieldOf("depth").orElse(DepthSettings.DEFAULT).forGetter(NoiseLandmass::depth),
            ScaleSettings.CODEC.fieldOf("scale").orElse(ScaleSettings.DEFAULT).forGetter(NoiseLandmass::scale),
            Codec.BOOL.fieldOf("alphaSampling").orElse(false).forGetter(NoiseLandmass::alphaSampling)
        ).apply(instance, NoiseLandmass::new)
    );

    public static final NoiseLandmass DEFAULT = CodecUtil.getDefaultByMap(CODEC);

    public record DepthSettings(
        boolean enabled,
        boolean sample,
        float influence,
        float negativeInfluence,
        float stretch,
        float offset,
        float positiveDampening,
        float negativeDampening,
        float minValue,
        float maxValue,
        boolean negativeFlattening
    ) {
        public static final Codec<DepthSettings> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                Codec.BOOL.fieldOf("enabled").orElse(true).forGetter(DepthSettings::enabled),
                Codec.BOOL.fieldOf("sample").orElse(true).forGetter(DepthSettings::sample),
                Codec.FLOAT.fieldOf("influence").orElse(1.0F).forGetter(DepthSettings::influence),
                Codec.FLOAT.fieldOf("negativeInfluence").orElse(0.3F).forGetter(DepthSettings::negativeInfluence),
                Codec.FLOAT.fieldOf("stretch").orElse(0.3F).forGetter(DepthSettings::stretch),
                Codec.FLOAT.fieldOf("offset").orElse(-2.0F).forGetter(DepthSettings::offset),
                Codec.FLOAT.fieldOf("positiveDampening").orElse(8.0F).forGetter(DepthSettings::positiveDampening),
                Codec.FLOAT.fieldOf("negativeDampening").orElse(5.6F).forGetter(DepthSettings::negativeDampening),
                Codec.FLOAT.fieldOf("minValue").orElse(-1.0f / 2.8F).forGetter(DepthSettings::minValue),
                Codec.FLOAT.fieldOf("maxValue").orElse(1.0f / 8.0F).forGetter(DepthSettings::maxValue),
                Codec.BOOL.fieldOf("negativeFlattening").orElse(true).forGetter(DepthSettings::negativeFlattening)
            ).apply(instance, DepthSettings::new)
        );

        public static final DepthSettings DEFAULT = CodecUtil.getDefaultByMap(CODEC);
    }

    public record ScaleSettings(
        boolean enabled,
        boolean sample,
        float variation,
        float influence,
        float offset
    ) {
        public static final Codec<ScaleSettings> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                Codec.BOOL.fieldOf("enabled").orElse(true).forGetter(ScaleSettings::enabled),
                Codec.BOOL.fieldOf("sample").orElse(true).forGetter(ScaleSettings::sample),
                Codec.FLOAT.fieldOf("variation").orElse(1.121F).forGetter(ScaleSettings::variation),
                Codec.FLOAT.fieldOf("influence").orElse(1.0F).forGetter(ScaleSettings::influence),
                Codec.FLOAT.fieldOf("offset").orElse(0.5F).forGetter(ScaleSettings::offset)
            ).apply(instance, ScaleSettings::new)
        );

        public static final ScaleSettings DEFAULT = CodecUtil.getDefaultByMap(CODEC);
    }

    public NoiseLandmass(
        boolean depthEnabled,
        boolean scaleEnabled,
        boolean sampleDepth,
        boolean sampleScale,
        float variationScale,
        float scaleInfluence,
        float depthInfluence,
        float negativeDepthInfluence,
        float depthStretch,
        float scaleOffset,
        float depthOffset,
        float positiveDepthDampening,
        float negativeDepthDampening,
        float minDepth,
        float maxDepth,
        boolean negativeDepthFlattening,
        boolean alphaSampling
    ) {
        this(
            new DepthSettings(
                depthEnabled,
                sampleDepth,
                depthInfluence,
                negativeDepthInfluence,
                depthStretch,
                depthOffset,
                positiveDepthDampening,
                negativeDepthDampening,
                minDepth,
                maxDepth,
                negativeDepthFlattening
            ),
            new ScaleSettings(
                scaleEnabled,
                sampleScale,
                variationScale,
                scaleInfluence,
                scaleOffset
            ),
            alphaSampling
        );
    }

    public static final NoiseLandmass DISABLED = new NoiseLandmass(
        false,
        false,
        false,
        false,
        1.0f,
        1.0f,
        1.0f,
        1.0f,
        3.0f,
        0.5f,
        -3.0f,
        6.0f,
        2.8f,
        -1.0f / 1.4f,
        1.0f / 6.0f,
        true,
        false
    );
    public static final NoiseLandmass INFDEV_611 = new NoiseLandmass(
        true,
        true,
        true,
        true,
        1.0f,
        1.0f,
        1.0f,
        1.0f,
        3.0f,
        0.5f,
        -3.0f,
        6.0f,
        2.8f,
        -1.0f / 1.4f,
        1.0f / 6.0f,
        true,
        true
    );
    public static final NoiseLandmass ALPHA = new NoiseLandmass(
        true,
        true,
        true,
        true,
        1.0f,
        1.0f,
        1.0f,
        1.0f,
        3.0f,
        0.5f,
        -3.0f,
        6.0f,
        5.6f,
        -1.0f / 2.8f,
        1.0f / 6.0f,
        true,
        true
    );
    public static final NoiseLandmass SKYLANDS = new NoiseLandmass(
        true,
        true,
        false,
        true,
        1.0f,
        1.0f,
        1.0f,
        1.0f,
        3.0f,
        0.5f,
        -3.0f,
        6.0f,
        5.6f,
        -1.0f / 2.8f,
        1.0f / 6.0f,
        true,
        false
    );
    public static final NoiseLandmass RELEASE = new NoiseLandmass(
        true,
        true,
        true,
        false,
        1.121f,
        0.0f,
        0.2f,
        0.3f,
        3.0f,
        0.0f,
        -2.0f,
        8.0f,
        5.6f,
        -1.0f / 2.8f,
        1.0f / 8.0f,
        false,
        false
    );
}
