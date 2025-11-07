package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;

public record SurfaceProperties(
    boolean bedrockHoles,
    boolean flipNoiseCoordinates,
    boolean surfaceBeaches,
    float sandBeachScale,
    float gravelBeachScale,
    float surfaceNoiseScale,
    boolean generateSandstone,
    boolean erosion,
    boolean gravelOceanBed
) {
    public static final Codec<SurfaceProperties> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.BOOL.fieldOf("bedrockHoles").orElse(false).forGetter(SurfaceProperties::bedrockHoles),
            Codec.BOOL.fieldOf("flipNoiseCoordinates").orElse(false).forGetter(SurfaceProperties::flipNoiseCoordinates),
            Codec.BOOL.fieldOf("surfaceBeaches").orElse(true).forGetter(SurfaceProperties::surfaceBeaches),
            Codec.FLOAT.fieldOf("sandBeachScale").orElse(0.03125F).forGetter(SurfaceProperties::sandBeachScale),
            Codec.FLOAT.fieldOf("gravelBeachScale").orElse(0.03125F).forGetter(SurfaceProperties::gravelBeachScale),
            Codec.FLOAT.fieldOf("surfaceNoiseScale").orElse(0.0625F).forGetter(SurfaceProperties::surfaceNoiseScale),
            Codec.BOOL.fieldOf("generateSandstone").orElse(true).forGetter(SurfaceProperties::generateSandstone),
            Codec.BOOL.fieldOf("erosion").orElse(true).forGetter(SurfaceProperties::erosion),
            Codec.BOOL.fieldOf("gravelOceanBed").orElse(false).forGetter(SurfaceProperties::gravelOceanBed)
        ).apply(instance, SurfaceProperties::new)
    );
    public static final SurfaceProperties DEFAULT = CodecUtil.getDefaultByMap(CODEC);

    public static final SurfaceProperties ALPHA = new SurfaceProperties(
        true,
        true,
        true,
        0.03125F,
        0.03125F,
        0.0625F,
        false,
        true,
        false
    );
    public static final SurfaceProperties BETA = new SurfaceProperties(
        false,
        false,
        true,
        0.03125F,
        0.03125F,
        0.0625F,
        true,
        true,
        false
    );
    public static final SurfaceProperties EARLY_RELEASE = new SurfaceProperties(
        false,
        false,
        false,
        0.03125F,
        0.03125F,
        0.0625F,
        true,
        true,
        false
    );
    public static final SurfaceProperties MAJOR_RELEASE = new SurfaceProperties(
        false,
        false,
        false,
        0.03125F,
        0.03125F,
        0.0625F,
        true,
        true,
        true
    );
}
