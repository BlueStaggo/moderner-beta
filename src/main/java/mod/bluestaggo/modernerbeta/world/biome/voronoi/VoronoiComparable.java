package mod.bluestaggo.modernerbeta.world.biome.voronoi;

public interface VoronoiComparable<T> {
    double calculateDistanceTo(T other);
}
