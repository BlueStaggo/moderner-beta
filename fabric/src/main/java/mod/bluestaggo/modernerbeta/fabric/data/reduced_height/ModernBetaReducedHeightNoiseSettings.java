package mod.bluestaggo.modernerbeta.fabric.data.reduced_height;

import net.minecraft.world.level.levelgen.NoiseSettings;

public class ModernBetaReducedHeightNoiseSettings {
    public static final NoiseSettings VANILLA_SURFACE;
    public static final NoiseSettings VANILLA_CAVES;

    public static final NoiseSettings OVERWORLD_128;
    public static final NoiseSettings INFDEV_415;
    public static final NoiseSettings OVERWORLD_256;

    static {
        VANILLA_SURFACE = NoiseSettings.create(0, 320, 1, 2);
        VANILLA_CAVES = NoiseSettings.create(0, 128, 1, 2);

        OVERWORLD_128 = NoiseSettings.create(0, 128, 1, 2);
        INFDEV_415 = NoiseSettings.create(0, 128, 1, 1);
        OVERWORLD_256 = NoiseSettings.create(0, 256, 1, 2);
    }
}
