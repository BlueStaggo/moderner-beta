package mod.bluestaggo.modernerbeta.settings.component;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.level.structure.ModernBetaStructures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.List;
import java.util.Map;

public record StructureModifiers(
    Map<ResourceKey<StructureSet>, StructureSet> overrides,
    List<ResourceKey<StructureSet>> removed
) {
    public static final Codec<StructureModifiers> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.unboundedMap(ResourceKey.codec(Registries.STRUCTURE_SET), StructureSet.DIRECT_CODEC)
                .fieldOf("overrides").forGetter(StructureModifiers::overrides),
            Codec.list(ResourceKey.codec(Registries.STRUCTURE_SET)).fieldOf("removed")
                .forGetter(StructureModifiers::removed)
        ).apply(instance, StructureModifiers::new)
    );

    public static final StructureModifiers VANILLA = new StructureModifiers(Map.of(), List.of());

    public static StructureModifiers getDefault(HolderGetter<Structure> structureRegistry) {
        return new StructureModifiers(
            Map.of(
                ResourceKey.create(Registries.STRUCTURE_SET, ModernerBeta.createId("ocean_shrine")),
                makeOceanShrineSet(structureRegistry)
            ),
            List.of()
        );
    }

    public static StructureModifiers getFinite(
        HolderGetter<Structure> structureRegistry,
        HolderGetter<Biome> biomeRegistry,
        int width,
        int length
    ) {
        return getFinite(structureRegistry, biomeRegistry, width, length, true);
    }

    public static StructureModifiers getFinite(
        HolderGetter<Structure> structureRegistry,
        HolderGetter<Biome> biomeRegistry,
        int width,
        int length,
        boolean shrines
    ) {
        ImmutableMap.Builder<ResourceKey<StructureSet>, StructureSet> overrideBuilder = ImmutableMap.builder();

        if (shrines) {
            overrideBuilder.put(
                ResourceKey.create(Registries.STRUCTURE_SET, ModernerBeta.createId("ocean_shrine")),
                makeOceanShrineSet(structureRegistry)
            );
        }

        overrideBuilder.put(
            BuiltinStructureSets.STRONGHOLDS,
            new StructureSet(
                structureRegistry.getOrThrow(BuiltinStructures.STRONGHOLD),
                new ConcentricRingsStructurePlacement(32, 3, 128, biomeRegistry.getOrThrow(BiomeTags.STRONGHOLD_BIASED_TO))
            )
        );

        return new StructureModifiers(overrideBuilder.build(), List.of());
    }

    @SafeVarargs
    public static StructureModifiers vanillaWithExceptions(ResourceKey<StructureSet>... structures) {
        return new StructureModifiers(Map.of(), List.of(structures));
    }

    @SafeVarargs
    public static StructureModifiers customWithExceptions(
        HolderGetter<Structure> structureRegistry,
        ResourceKey<StructureSet>... structures
    ) {
        return new StructureModifiers(
            Map.of(
                ResourceKey.create(Registries.STRUCTURE_SET, ModernerBeta.createId("ocean_shrine")),
                makeOceanShrineSet(structureRegistry)
            ),
            List.of(structures)
        );
    }

    private static StructureSet makeOceanShrineSet(HolderGetter<Structure> structureRegistry) {
        return new StructureSet(
            structureRegistry.getOrThrow(ModernBetaStructures.OCEAN_SHRINE),
            new RandomSpreadStructurePlacement(64, 16, RandomSpreadType.TRIANGULAR, 357)
        );
    }
}
