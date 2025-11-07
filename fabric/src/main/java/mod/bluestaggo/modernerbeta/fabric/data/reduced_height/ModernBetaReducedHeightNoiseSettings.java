package mod.bluestaggo.modernerbeta.fabric.data.reduced_height;

import net.minecraft.world.level.levelgen.NoiseSettings;

public class ModernBetaReducedHeightNoiseSettings {
    protected static final NoiseSettings VANILLA_SURFACE;
    protected static final NoiseSettings VANILLA_CAVES;

    public static final NoiseSettings SKYLANDS;
    public static final NoiseSettings INFDEV_227;
    public static final NoiseSettings INDEV;
    public static final NoiseSettings FULL_HEIGHT;

    static {
        VANILLA_SURFACE = NoiseSettings.create(0, 320, 1, 2);
        VANILLA_CAVES = NoiseSettings.create(0, 128, 1, 2);

        SKYLANDS = NoiseSettings.create(0, 128, 2, 1);
        INFDEV_227 = NoiseSettings.create(0, 128, 1, 2);
        INDEV = NoiseSettings.create(0, 256, 1, 2);
        FULL_HEIGHT = NoiseSettings.create(0, 320, 1, 2);
    }
}
