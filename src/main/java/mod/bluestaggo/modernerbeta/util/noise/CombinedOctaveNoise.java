package mod.bluestaggo.modernerbeta.util.noise;

public final class CombinedOctaveNoise {
    private final OctaveNoise firstNoise;
    private final OctaveNoise secondNoise;
    
    public CombinedOctaveNoise(OctaveNoise firstNoise, OctaveNoise secondNoise) {
        this.firstNoise = firstNoise;
        this.secondNoise = secondNoise;
    }
    
    public final double sample(double x, double y) {
        return this.firstNoise.sampleXY(x + this.secondNoise.sampleXY(x, y), y);
    }
}
