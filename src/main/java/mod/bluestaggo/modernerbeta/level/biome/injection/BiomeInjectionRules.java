package mod.bluestaggo.modernerbeta.level.biome.injection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomes;
import mod.bluestaggo.modernerbeta.level.biome.injection.injector.*;
import mod.bluestaggo.modernerbeta.level.biome.injection.predicates.BiomeInSetInjectionPredicate;
import mod.bluestaggo.modernerbeta.level.biome.injection.predicates.SurfaceBelowSeaLevelInjectionPredicate;
import mod.bluestaggo.modernerbeta.level.biome.injection.predicates.BelowSurfaceInjectionPredicate;
import mod.bluestaggo.modernerbeta.level.biome.injection.predicates.OutOfBoundsInjectionPredicate;
import mod.bluestaggo.modernerbeta.tags.ModernBetaBiomeTags;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.Arrays;
import java.util.List;

public final class BiomeInjectionRules {
    public static List<BiomeInjectionRule> emptyRules(RegistryOps.RegistryInfoLookup registries, BiomeInjectionRule... rules) {
        //~ if >=26.3 '.getter();' -> ';'
        HolderGetter<Biome> biomeRegistry = registries.lookup(Registries.BIOME).orElseThrow().getter();
        ImmutableList.Builder<BiomeInjectionRule> builder = ImmutableList.builder();

        builder.add(new BiomeInjectionRule(
            new Cache2DBiomeInjector(
                new PredicateBiomeInjector(
                    new OutOfBoundsInjectionPredicate(4),
                    new ConstantBiomeInjector(biomeRegistry.getOrThrow(Biomes.THE_VOID))
                )
            ),
            BiomeInjectionRule.Step.PRE
        ));

        builder.addAll(Arrays.asList(rules));

        return builder.build();
    }

    public static List<BiomeInjectionRule> standardRules(RegistryOps.RegistryInfoLookup registries, BiomeInjectionRule... rules) {
        //~ if >=26.3 '.getter();' -> ';'
        HolderGetter<Biome> biomeRegistry = registries.lookup(Registries.BIOME).orElseThrow().getter();
        ImmutableList.Builder<BiomeInjectionRule> builder = ImmutableList.builder();

        builder.add(new BiomeInjectionRule(
            new Cache2DBiomeInjector(
                new PredicateBiomeInjector(
                    new OutOfBoundsInjectionPredicate(4),
                    new ConstantBiomeInjector(biomeRegistry.getOrThrow(Biomes.THE_VOID))
                )
            ),
            BiomeInjectionRule.Step.PRE
        ));

        builder.addAll(Arrays.asList(rules));

        builder.add(new BiomeInjectionRule(
            new PredicateBiomeInjector(
                new BelowSurfaceInjectionPredicate(8)
                    .and(new BiomeInSetInjectionPredicate(biomeRegistry.getOrThrow(Biomes.THE_VOID)).invert()),
                new CaveBiomeInjector()
            ),
            BiomeInjectionRule.Step.PRE
        ));

        return builder.build();
    }
    
    public static BiomeInjectionRule makeBetaOceanRule(RegistryOps.RegistryInfoLookup registries, boolean pe) {
        //~ if >=26.3 '.getter();' -> ';'
        HolderGetter<Biome> biomeRegistry = registries.lookup(Registries.BIOME).orElseThrow().getter();

        ImmutableMap.Builder<TagKey<Biome>, Holder<Biome>> builder = ImmutableMap.builder();

        builder.put(ModernBetaBiomeTags.REPLACE_WITH_FROZEN_OCEAN,
                biomeRegistry.getOrThrow(!pe ? ModernBetaBiomes.BETA_FROZEN_OCEAN : ModernBetaBiomes.PE_FROZEN_OCEAN));
        builder.put(ModernBetaBiomeTags.REPLACE_WITH_COLD_OCEAN,
                biomeRegistry.getOrThrow(!pe ? ModernBetaBiomes.BETA_COLD_OCEAN : ModernBetaBiomes.PE_COLD_OCEAN));
        builder.put(ModernBetaBiomeTags.REPLACE_WITH_OCEAN,
                biomeRegistry.getOrThrow(!pe ? ModernBetaBiomes.BETA_OCEAN : ModernBetaBiomes.PE_OCEAN));
        builder.put(ModernBetaBiomeTags.REPLACE_WITH_LUKEWARM_OCEAN,
                biomeRegistry.getOrThrow(!pe ? ModernBetaBiomes.BETA_LUKEWARM_OCEAN : ModernBetaBiomes.PE_LUKEWARM_OCEAN));
        builder.put(ModernBetaBiomeTags.REPLACE_WITH_WARM_OCEAN,
                biomeRegistry.getOrThrow(!pe ? ModernBetaBiomes.BETA_WARM_OCEAN : ModernBetaBiomes.PE_WARM_OCEAN));

        return new BiomeInjectionRule(
            new Cache2DBiomeInjector(
                new PredicateBiomeInjector(
                    new SurfaceBelowSeaLevelInjectionPredicate(4),
                    new ReplaceByTagBiomeInjector(builder.build())
                )
            ),
            BiomeInjectionRule.Step.PRE
        );
    } 
    
    public static BiomeInjectionRule makeModernOceanRule(RegistryOps.RegistryInfoLookup registries) {
        //~ if >=26.3 '.getter();' -> ';'
        HolderGetter<Biome> biomeRegistry = registries.lookup(Registries.BIOME).orElseThrow().getter();
        
        return new BiomeInjectionRule(
            new Cache2DBiomeInjector(
                new PredicateBiomeInjector(
                    new BiomeInSetInjectionPredicate(biomeRegistry.getOrThrow(Biomes.THE_VOID)).invert(),
                    new PredicateBiomeInjector(
                        new SurfaceBelowSeaLevelInjectionPredicate(16),
                        new MappedClimateBiomeInjector(MappedClimateBiomeInjector.Type.TEMPERATURE, List.of(
                            new MappedClimateBiomeInjector.Entry(0.2, biomeRegistry.getOrThrow(Biomes.DEEP_LUKEWARM_OCEAN)),
                            new MappedClimateBiomeInjector.Entry(0.0, biomeRegistry.getOrThrow(Biomes.DEEP_OCEAN)),
                            new MappedClimateBiomeInjector.Entry(-0.1, biomeRegistry.getOrThrow(Biomes.DEEP_COLD_OCEAN)),
                            new MappedClimateBiomeInjector.Entry(-0.2, biomeRegistry.getOrThrow(Biomes.DEEP_FROZEN_OCEAN))
                        )),
                        new PredicateBiomeInjector(
                            new SurfaceBelowSeaLevelInjectionPredicate(4),
                            new MappedClimateBiomeInjector(MappedClimateBiomeInjector.Type.TEMPERATURE, List.of(
                                new MappedClimateBiomeInjector.Entry(0.4, biomeRegistry.getOrThrow(Biomes.WARM_OCEAN)),
                                new MappedClimateBiomeInjector.Entry(0.2, biomeRegistry.getOrThrow(Biomes.LUKEWARM_OCEAN)),
                                new MappedClimateBiomeInjector.Entry(0.0, biomeRegistry.getOrThrow(Biomes.OCEAN)),
                                new MappedClimateBiomeInjector.Entry(-0.1, biomeRegistry.getOrThrow(Biomes.COLD_OCEAN)),
                                new MappedClimateBiomeInjector.Entry(-0.2, biomeRegistry.getOrThrow(Biomes.FROZEN_OCEAN))
                            ))
                        )
                    )
                )
            ),
            BiomeInjectionRule.Step.PRE
        );
    }
}
