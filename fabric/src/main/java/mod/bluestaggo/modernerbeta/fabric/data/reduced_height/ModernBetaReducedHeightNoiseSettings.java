package mod.bluestaggo.modernerbeta.fabric.data.reduced_height;

import net.minecraft.world.level.levelgen.NoiseSettings;

public class ModernBetaReducedHeightNoiseSettings {
    protected static final NoiseSettings VANILLA_SURFACE;
    protected static final NoiseSettings VANILLA_CAVES;

    public static final NoiseSettings BETA;
    public static final NoiseSettings ALPHA;
    public static final NoiseSettings SKYLANDS;
    public static final NoiseSettings INFDEV_611;
    public static final NoiseSettings INFDEV_420;
    public static final NoiseSettings INFDEV_415;
    public static final NoiseSettings INFDEV_227;
    public static final NoiseSettings INDEV;
    public static final NoiseSettings CLASSIC_0_30;
    public static final NoiseSettings PE;
    public static final NoiseSettings EARLY_RELEASE;
    public static final NoiseSettings MAJOR_RELEASE;
    public static final NoiseSettings EARLY_BEDROCK;
    public static final NoiseSettings FULL_HEIGHT;

    static {
        VANILLA_SURFACE = NoiseSettings.create(0, 320, 1, 2);
        VANILLA_CAVES = NoiseSettings.create(0, 128, 1, 2);

        BETA = NoiseSettings.create(0, 128, 1, 2);
        ALPHA = NoiseSettings.create(0, 128, 1, 2);
        SKYLANDS = NoiseSettings.create(0, 128, 2, 1);
        INFDEV_611 = NoiseSettings.create(0, 128, 1, 2);
        INFDEV_420 = NoiseSettings.create(0, 128, 1, 2);
        INFDEV_415 = NoiseSettings.create(0, 128, 1, 1);
        INFDEV_227 = NoiseSettings.create(0, 128, 1, 2);
        INDEV = NoiseSettings.create(0, 256, 1, 2);
        CLASSIC_0_30 = NoiseSettings.create(0, 256, 1, 2);
        PE = NoiseSettings.create(0, 128, 1, 2);
        EARLY_RELEASE = NoiseSettings.create(0, 128, 1, 2);
        MAJOR_RELEASE = NoiseSettings.create(0, 256, 1, 2);
        EARLY_BEDROCK = NoiseSettings.create(0, 128, 1, 2);
        FULL_HEIGHT = NoiseSettings.create(0, 320, 1, 2);
    }
}
