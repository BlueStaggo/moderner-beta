package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import net.minecraft.util.StringIdentifiable;

import java.util.*;

public class BorderLayer extends SingleParentLayer {
    public static final MapCodec<BorderLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(Case.CODEC.listOf().fieldOf("cases").forGetter(layer -> layer.cases))
            .apply(instance, BorderLayer::new)
    );

    private final List<Case> cases;

    public BorderLayer(String id, long seed, String parent, List<Case> cases) {
        super(id, seed, parent);
        this.cases = cases;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.BORDER;
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        ExtendedBiomeId base = this.parentLayer.sample(x, z);
        ExtendedBiomeId[] neighbors = this.parentLayer.sampleNeighbors(x, z);

        if (allNeighborsEqual(neighbors, base)) {
            return base;
        }

        for (Case $case : this.cases) {
            if ($case.isSatisfied(base, neighbors)) {
                return $case.edgeVariant;
            }
        }
        return base;
    }

    public record Case(Set<ExtendedBiomeId> biomes, boolean inverseBiomeFilter, ExtendedBiomeId edgeVariant, Set<ExtendedBiomeId> filter, FilterType filterType) {
        public static final Codec<Case> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CodecUtil.set(ExtendedBiomeId.CODEC).fieldOf("biomes").forGetter(Case::biomes),
            Codec.BOOL.fieldOf("inverseBiomeFilter").orElse(false).forGetter(Case::inverseBiomeFilter),
            ExtendedBiomeId.CODEC.fieldOf("edgeVariant").forGetter(Case::edgeVariant),
            CodecUtil.set(ExtendedBiomeId.CODEC).fieldOf("filter").orElse(Set.of()).forGetter(Case::filter),
            StringIdentifiable.createCodec(FilterType::values).fieldOf("filterType").orElse(FilterType.ANY_MATCH).forGetter(Case::filterType)
        ).apply(instance, Case::new));

        public static final Case MUSHROOM_SHORE = byOcean(Set.of(ExtendedBiomeId.MUSHROOM_ISLAND), false, ExtendedBiomeId.MUSHROOM_SHORE);

        public static Case byOcean(Set<ExtendedBiomeId> biomes, boolean inverseBiomeFilter, ExtendedBiomeId edgeVariant) {
            return new Case(biomes, inverseBiomeFilter, edgeVariant, Collections.singleton(ExtendedBiomeId.OCEAN), FilterType.ANY_MATCH);
        }

        public boolean isSatisfied(ExtendedBiomeId biome, ExtendedBiomeId[] neighbors) {
            if (this.biomes.isEmpty()) {
                if (!this.inverseBiomeFilter) {
                    return false;
                }
            } else if (this.biomes.contains(biome) == this.inverseBiomeFilter) {
                return false;
            }

            return this.filter.isEmpty() || this.filterType.isSatisfied(neighbors, this.filter);
        }
    }

    public enum FilterType implements StringIdentifiable {
        ALL_MATCH("all_match") {
            @Override
            public boolean isSatisfied(ExtendedBiomeId[] neighbors, Set<ExtendedBiomeId> filter) {
                return allNeighborsInSet(neighbors, filter);
            }
        },
        NONE_MATCH("none_match") {
            @Override
            public boolean isSatisfied(ExtendedBiomeId[] neighbors, Set<ExtendedBiomeId> filter) {
                return !anyNeighborsInSet(neighbors, filter);
            }
        },
        ANY_MATCH("any_match") {
            @Override
            public boolean isSatisfied(ExtendedBiomeId[] neighbors, Set<ExtendedBiomeId> filter) {
                return anyNeighborsInSet(neighbors, filter);
            }
        },
        NOT_ALL_MATCH("not_all_match") {
            @Override
            public boolean isSatisfied(ExtendedBiomeId[] neighbors, Set<ExtendedBiomeId> filter) {
                return !allNeighborsInSet(neighbors, filter);
            }
        };

        public final String id;

        FilterType(String id) {
            this.id = id;
        }

        @Override
        public String asString() {
            return this.id;
        }

        public abstract boolean isSatisfied(ExtendedBiomeId[] neighbors, Set<ExtendedBiomeId> filter);
    }
}
