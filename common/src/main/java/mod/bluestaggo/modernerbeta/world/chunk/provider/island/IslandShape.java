package mod.bluestaggo.modernerbeta.world.chunk.provider.island;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.MathHelper;

public enum IslandShape implements StringIdentifiable {
    CIRCLE("circle", (noiseX, noiseZ) -> MathHelper.sqrt(noiseX * noiseX + noiseZ * noiseZ)),
    SQUARE("square", (noiseX, noiseZ) -> Math.max(Math.abs(noiseX), Math.abs(noiseZ))),
    DIAMOND("diamond", (noiseX, noiseZ) -> Math.abs(noiseX) + Math.abs(noiseZ));
    
    private final String id;
    private final DistanceProvider provider;
    
    IslandShape(String id, DistanceProvider provider) {
        this.id = id;
        this.provider = provider;
    }

    @Override
    public String asString() {
        return this.id;
    }
    
    public double getDistance(int noiseX, int noiseZ) {
        return this.provider.apply(noiseX, noiseZ);
    }

    @FunctionalInterface
    public interface DistanceProvider {
        double apply(int noiseX, int noiseZ);
    }
}
