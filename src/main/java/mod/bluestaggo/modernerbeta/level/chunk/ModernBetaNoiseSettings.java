package mod.bluestaggo.modernerbeta.level.chunk;

import mod.bluestaggo.modernerbeta.settings.component.NoiseSettings;

public class ModernBetaNoiseSettings {
    public static final NoiseSettings OVERWORLD_128;
    public static final NoiseSettings SKY_128;
    public static final NoiseSettings INFDEV_415;
    public static final NoiseSettings FINITE_2D;
    public static final NoiseSettings OVERWORLD_256;
    public static final NoiseSettings OVERWORLD_FULL;

    static {
        OVERWORLD_128 = NoiseSettings.create(-64, 192, 1, 2);
        SKY_128 = NoiseSettings.create(0, 128, 2, 1);
        INFDEV_415 = NoiseSettings.create(-64, 192, 1, 1);
        FINITE_2D = NoiseSettings.create(0, 256, 1, 2);
        OVERWORLD_256 = NoiseSettings.create(-64, 320, 1, 2);
        OVERWORLD_FULL = NoiseSettings.create(-64, 384, 1, 2);
    }
}
