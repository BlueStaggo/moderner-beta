package mod.bluestaggo.modernerbeta.level.biome.injection.handler;

import mod.bluestaggo.modernerbeta.api.level.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.api.level.biome.BiomeResolverBlock;
import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionRule;
import mod.bluestaggo.modernerbeta.level.biome.injection.InjectionNeeds;
import mod.bluestaggo.modernerbeta.mixin.LevelChunkSectionAccessor;
import net.minecraft.core.Holder;
import net.minecraft.util.LinearCongruentialGenerator;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public interface BiomeInjectionHandler {
    @Nullable Holder<Biome> getBiome(
        LevelHeightAccessor level,
        int biomeX, int biomeY, int biomeZ,
        BiomeInjectionRule.Step step,
        EnumSet<InjectionNeeds> ableToFulfill,
        boolean shouldDelegate
    );

    @NotNull List<BiomeInjectionRule> getRulesForStep(
        BiomeInjectionRule.Step step,
        EnumSet<InjectionNeeds> ableToFulfill
    );

    default void clear() {
    }

    default @NotNull Holder<Biome> getBiomeAtBlock(
        LevelHeightAccessor level,
        BiomeProvider baseBiomeProvider,
        long biomeZoomSeed,
        int x, int y, int z,
        BiomeInjectionRule.Step step,
        EnumSet<InjectionNeeds> ableToFulfill,
        boolean shouldDelegate
    ) {
        int absX = x - 2;
        int absY = y - 2;
        int absZ = z - 2;

        int parentX = absX >> 2;
        int parentY = absY >> 2;
        int parentZ = absZ >> 2;

        double fractX = (absX & 3) / 4.0;
        double fractY = (absY & 3) / 4.0;
        double fractZ = (absZ & 3) / 4.0;

        int minI = 0;
        double minFiddledDistance = Double.POSITIVE_INFINITY;

        for (int i = 0; i < 8; i++) {
            boolean xEven = (i & 4) == 0;
            boolean yEven = (i & 2) == 0;
            boolean zEven = (i & 1) == 0;

            int cornerX = xEven ? parentX : parentX + 1;
            int cornerY = yEven ? parentY : parentY + 1;
            int cornerZ = zEven ? parentZ : parentZ + 1;

            double distanceX = xEven ? fractX : fractX - 1.0;
            double distanceY = yEven ? fractY : fractY - 1.0;
            double distanceZ = zEven ? fractZ : fractZ - 1.0;

            double next = getFiddledDistance(biomeZoomSeed, cornerX, cornerY, cornerZ, distanceX, distanceY, distanceZ);
            if (minFiddledDistance > next) {
                minI = i;
                minFiddledDistance = next;
            }
        }

        int biomeX = (minI & 4) == 0 ? parentX : parentX + 1;
        int biomeY = (minI & 2) == 0 ? parentY : parentY + 1;
        int biomeZ = (minI & 1) == 0 ? parentZ : parentZ + 1;
        Holder<Biome> biome = this.getBiome(level, biomeX, biomeY, biomeZ, step, ableToFulfill, shouldDelegate);

        if (biome == null) {
            if (baseBiomeProvider instanceof BiomeResolverBlock biomeBlock) {
                biome = biomeBlock.getBiomeBlock(x, y, z);
            } else {
                biome = baseBiomeProvider.getBiome(biomeX, biomeY, biomeZ);
            }
        }

        return biome;
    }

    default PalettedContainer<Holder<Biome>> injectIntoChunkSection(
        LevelHeightAccessor view,
        ChunkPos chunkPos,
        int sectionY,
        PalettedContainerRO<Holder<Biome>> biomes,
        BiomeInjectionRule.Step step,
        EnumSet<InjectionNeeds> ableToFulfill
    ) {
        int startBiomeX = chunkPos.getMinBlockX() >> 2;
        int startBiomeZ = chunkPos.getMinBlockZ() >> 2;

        PalettedContainer<Holder<Biome>> palettedContainer = biomes.recreate();
        palettedContainer.acquire();

        try {
            for (int localBiomeX = 0; localBiomeX < 4; ++localBiomeX) {
                int biomeX = localBiomeX + startBiomeX;

                for (int localBiomeZ = 0; localBiomeZ < 4; ++localBiomeZ) {
                    int biomeZ = localBiomeZ + startBiomeZ;

                    for (int localBiomeY = 0; localBiomeY < 4; ++localBiomeY) {
                        int biomeY = sectionY << 2 | localBiomeY;

                        Holder<Biome> replacementBiome = this.getBiome(view, biomeX, biomeY, biomeZ, step, ableToFulfill, true);
                        if (replacementBiome == null) {
                            replacementBiome = biomes.get(localBiomeX, localBiomeY, localBiomeZ);
                        }

                        palettedContainer.getAndSetUnchecked(localBiomeX, localBiomeY, localBiomeZ, replacementBiome);
                    }
                }
            }
        } finally {
            palettedContainer.release();
        }

        return palettedContainer;
    }

    default void injectIntoChunk(ChunkAccess chunk, BiomeInjectionRule.Step step, EnumSet<InjectionNeeds> ableToFulfill) {
        LevelHeightAccessor view = chunk.getHeightAccessorForGeneration();

        for (int sectionY = view.getMinSectionY(); sectionY < view.getMaxSectionY() + 1; sectionY++) {
            int sectionIndex = chunk.getSectionIndexFromSectionY(sectionY);
            LevelChunkSection section = chunk.getSection(sectionIndex);

            PalettedContainerRO<Holder<Biome>> biomes = section.getBiomes();
            PalettedContainer<Holder<Biome>> injected =
                    this.injectIntoChunkSection(view, chunk.getPos(), sectionY, biomes, step, ableToFulfill);

            ((LevelChunkSectionAccessor)section).setBiomes(injected);
        }
    }

    private static double getFiddledDistance(
        long seed,
        int xRandom, int yRandom, int zRandom,
        double distanceX, double distanceY, double distanceZ
    ) {
        long rval = LinearCongruentialGenerator.next(seed, xRandom);
        rval = LinearCongruentialGenerator.next(rval, yRandom);
        rval = LinearCongruentialGenerator.next(rval, zRandom);
        rval = LinearCongruentialGenerator.next(rval, xRandom);
        rval = LinearCongruentialGenerator.next(rval, yRandom);
        rval = LinearCongruentialGenerator.next(rval, zRandom);
        double fiddleX = getFiddle(rval);
        rval = LinearCongruentialGenerator.next(rval, seed);
        double fiddleY = getFiddle(rval);
        rval = LinearCongruentialGenerator.next(rval, seed);
        double fiddleZ = getFiddle(rval);

        return Mth.square(distanceZ + fiddleZ)
                + Mth.square(distanceY + fiddleY)
                + Mth.square(distanceX + fiddleX);
    }

    private static double getFiddle(long rval) {
        double uniform = Math.floorMod(rval >> 24, 1024) / 1024.0;
        return (uniform - 0.5) * 0.9;
    }
}
