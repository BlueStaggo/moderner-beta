package mod.bluestaggo.modernerbeta.api.world.biome.climate;

import mod.bluestaggo.modernerbeta.world.biome.voronoi.VoronoiComparable;

public record Clime(double temp, double rain, double weird) implements VoronoiComparable<Clime> {
    public Clime(double temp, double rain) {
        this(temp, rain, 0.5);
    }
    
    @Override
    public double calculateDistanceTo(Clime other) {
        return
            (this.temp() - other.temp()) * (this.temp() - other.temp()) +
            (this.rain() - other.rain()) * (this.rain() - other.rain()) +
            (this.weird() - other.weird()) * (this.weird() - other.weird());
    }
}
