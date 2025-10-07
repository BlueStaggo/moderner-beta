package mod.bluestaggo.modernerbeta.fabric.data.reduced_height;

import net.minecraft.world.gen.chunk.GenerationShapeConfig;

public class ModernBetaShapeReducedHeightConfigs {
    protected static final GenerationShapeConfig VANILLA_SURFACE;
    protected static final GenerationShapeConfig VANILLA_CAVES;

    public static final GenerationShapeConfig BETA;
    public static final GenerationShapeConfig ALPHA;
    public static final GenerationShapeConfig SKYLANDS;
    public static final GenerationShapeConfig INFDEV_611;
    public static final GenerationShapeConfig INFDEV_420;
    public static final GenerationShapeConfig INFDEV_415;
    public static final GenerationShapeConfig INFDEV_227;
    public static final GenerationShapeConfig INDEV;
    public static final GenerationShapeConfig CLASSIC_0_30;
    public static final GenerationShapeConfig PE;
    public static final GenerationShapeConfig EARLY_RELEASE;
    public static final GenerationShapeConfig MAJOR_RELEASE;
    public static final GenerationShapeConfig EARLY_BEDROCK;

    static {
        VANILLA_SURFACE = GenerationShapeConfig.create(0, 320, 1, 2);
        VANILLA_CAVES = GenerationShapeConfig.create(0, 128, 1, 2);

        BETA = GenerationShapeConfig.create(0, 128, 1, 2);
        ALPHA = GenerationShapeConfig.create(0, 128, 1, 2);
        SKYLANDS = GenerationShapeConfig.create(0, 128, 2, 1);
        INFDEV_611 = GenerationShapeConfig.create(0, 128, 1, 2);
        INFDEV_420 = GenerationShapeConfig.create(0, 128, 1, 2);
        INFDEV_415 = GenerationShapeConfig.create(0, 128, 1, 1);
        INFDEV_227 = GenerationShapeConfig.create(0, 128, 1, 2);
        INDEV = GenerationShapeConfig.create(0, 256, 1, 2);
        CLASSIC_0_30 = GenerationShapeConfig.create(0, 256, 1, 2);
        PE = GenerationShapeConfig.create(0, 128, 1, 2);
        EARLY_RELEASE = GenerationShapeConfig.create(0, 128, 1, 2);
        MAJOR_RELEASE = GenerationShapeConfig.create(0, 256, 1, 2);
        EARLY_BEDROCK = GenerationShapeConfig.create(0, 128, 1, 2);
    }
}
