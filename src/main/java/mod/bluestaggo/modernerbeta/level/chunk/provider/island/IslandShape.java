package mod.bluestaggo.modernerbeta.level.chunk.provider.island;

import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;

public enum IslandShape implements StringRepresentable {
    CIRCLE("circle", (noiseX, noiseZ) -> Mth.sqrt(noiseX * noiseX + noiseZ * noiseZ)),
    SQUARE("square", (noiseX, noiseZ) -> Math.max(Math.abs(noiseX), Math.abs(noiseZ))),
    DIAMOND("diamond", (noiseX, noiseZ) -> Math.abs(noiseX) + Math.abs(noiseZ));
    
    private final String id;
    private final DistanceProvider provider;
    
    IslandShape(String id, DistanceProvider provider) {
        this.id = id;
        this.provider = provider;
    }

    @Override
    public String getSerializedName() {
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
