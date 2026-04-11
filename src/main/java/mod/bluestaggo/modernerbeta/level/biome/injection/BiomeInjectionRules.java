package mod.bluestaggo.modernerbeta.level.biome.injection;

import com.google.common.collect.ImmutableList;
import mod.bluestaggo.modernerbeta.level.biome.injection.injector.*;
import mod.bluestaggo.modernerbeta.level.biome.injection.predicates.SurfaceBelowSeaLevelInjectionPredicate;
import mod.bluestaggo.modernerbeta.level.biome.injection.predicates.BelowSurfaceInjectionPredicate;
import mod.bluestaggo.modernerbeta.level.biome.injection.predicates.OutOfBoundsInjectionPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.List;

public final class BiomeInjectionRules {
    public static List<BiomeInjectionRule> emptyRules(RegistryOps.RegistryInfoLookup registries) {
        HolderGetter<Biome> biomeRegistry = registries.lookup(Registries.BIOME).orElseThrow().getter();
        ImmutableList.Builder<BiomeInjectionRule> builder = ImmutableList.builder();

        builder.add(new BiomeInjectionRule(
            new PredicateBiomeInjector(
                new OutOfBoundsInjectionPredicate(4),
                new ConstantBiomeInjector(biomeRegistry.getOrThrow(Biomes.THE_VOID))
            ),
            BiomeInjectionRule.Step.PRE
        ));

        return builder.build();
    }

    public static List<BiomeInjectionRule> defaultRules(RegistryOps.RegistryInfoLookup registries, boolean oceans) {
        HolderGetter<Biome> biomeRegistry = registries.lookup(Registries.BIOME).orElseThrow().getter();
        ImmutableList.Builder<BiomeInjectionRule> builder = ImmutableList.builder();

        builder.add(new BiomeInjectionRule(
            new PredicateBiomeInjector(
                new OutOfBoundsInjectionPredicate(4),
                new ConstantBiomeInjector(biomeRegistry.getOrThrow(Biomes.THE_VOID))
            ),
            BiomeInjectionRule.Step.PRE
        ));
        builder.add(new BiomeInjectionRule(
            new PredicateBiomeInjector(
                new BelowSurfaceInjectionPredicate(8),
                new CaveBiomeInjector()
            ),
            BiomeInjectionRule.Step.PRE
        ));

        if (oceans) {
            builder.add(new BiomeInjectionRule(
                new PredicateBiomeInjector(
                    new SurfaceBelowSeaLevelInjectionPredicate(16),
                    new DeepOceanBiomeInjector()
                ),
                BiomeInjectionRule.Step.PRE
            ));
            builder.add(new BiomeInjectionRule(
                new PredicateBiomeInjector(
                    new SurfaceBelowSeaLevelInjectionPredicate(4),
                    new OceanBiomeInjector()
                ),
                BiomeInjectionRule.Step.PRE
            ));
        }

        builder.add(new BiomeInjectionRule(
            new PredicateBiomeInjector(
                new OutOfBoundsInjectionPredicate(4),
                new ConstantBiomeInjector(biomeRegistry.getOrThrow(Biomes.THE_VOID))
            ),
            BiomeInjectionRule.Step.POST
        ));

        return builder.build();
    }
}
