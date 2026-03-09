package mod.bluestaggo.modernerbeta.level.biome.injector;

import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;

public class BiomeInjectionContext {
    public final int worldMinY;
    public final int topHeight;
    public final int minHeight;

    private int x;
    private int y;
    private int z;

    public BiomeInjectionContext(int worldMinY, int topHeight, int minHeight) {
        this.worldMinY = worldMinY;
        this.topHeight = topHeight;
        this.minHeight = minHeight;
        this.y = topHeight;
    }

    public BiomeInjectionContext setPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }
}
