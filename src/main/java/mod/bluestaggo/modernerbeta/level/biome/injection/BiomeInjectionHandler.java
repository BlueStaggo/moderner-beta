//~dotLocation
package mod.bluestaggo.modernerbeta.level.biome.injection;

import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProviderNoise;
import mod.bluestaggo.modernerbeta.mixin.LevelChunkSectionAccessor;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkHeightmap;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaChunkGenerator;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class BiomeInjectionHandler {
    private final ModernBetaChunkGenerator modernBetaChunkGenerator;
    private final ModernBetaBiomeSource modernBetaBiomeSource;

    private final List<BiomeInjectionRule> rules;
    private final ThreadLocal<BiomeInjectionContext> context;

    public BiomeInjectionHandler(ModernBetaChunkGenerator modernBetaChunkGenerator, ModernBetaBiomeSource modernBetaBiomeSource) {
        this.modernBetaChunkGenerator = modernBetaChunkGenerator;
        this.modernBetaBiomeSource = modernBetaBiomeSource;
        
        ModernBetaSettings settingsBiome = this.modernBetaBiomeSource.getBiomeSettings()
            .mapPreset(modernBetaChunkGenerator.getPresetRegistry(), ModernBetaSettingsPreset::biomeSettings);

        this.rules = settingsBiome.getOrDefault(SettingsComponentTypes.BIOME_INJECTION_RULES);

        this.context = ThreadLocal.withInitial(() -> {
            BiomeInjectionContext instance = new BiomeInjectionContext(this.modernBetaChunkGenerator, this.modernBetaBiomeSource);
            instance.setupContext();
            return instance;
        });
    }
    
    public void injectBiomes(ChunkAccess chunk, Sampler noiseSampler, BiomeInjectionRule.Step step) {
        if (this.rules.isEmpty()) {
            return;
        }

        ChunkPos chunkPos = chunk.getPos();
        
        int startBiomeX = chunkPos.getMinBlockX() >> 2;
        int startBiomeZ = chunkPos.getMinBlockZ() >> 2;
        LevelHeightAccessor view = chunk.getHeightAccessorForGeneration();
        
        /*
         * Collect the following for an x/z coordinate:
         * -> Height at local biome coordinate.
         * -> Minimum height of area around local biome coordinate.
         * -> Blockstate at height of local biome coordinate.
         */
        
        // Replace biomes from biome container
        for (int sectionY = view.getMinSectionY(); sectionY < view.getMaxSectionY() + 1; ++sectionY) {
            int sectionYNdx = chunk.getSectionIndexFromSectionY(sectionY);
            LevelChunkSection section = chunk.getSection(sectionYNdx);
            
            PalettedContainerRO<Holder<Biome>> readableContainer = section.getBiomes();
            PalettedContainer<Holder<Biome>> palettedContainer = section.getBiomes().recreate();
            palettedContainer.acquire();

            try {
                for (int localBiomeX = 0; localBiomeX < 4; ++localBiomeX) {
                    int biomeX = localBiomeX + startBiomeX;

                    for (int localBiomeZ = 0; localBiomeZ < 4; ++localBiomeZ) {
                        int biomeZ = localBiomeZ + startBiomeZ;

                        for (int localBiomeY = 0; localBiomeY < 4; ++localBiomeY) {
                            int biomeY = sectionY << 2 | localBiomeY;

                            Holder<Biome> initialBiome = readableContainer.get(localBiomeX, localBiomeY, localBiomeZ);
                            Holder<Biome> replacementBiome = this.getOptionalBiome(view, biomeX, biomeY, biomeZ,
                                            noiseSampler, step, InjectionNeeds.all()).orElse(initialBiome);

                            palettedContainer.getAndSetUnchecked(localBiomeX, localBiomeY, localBiomeZ, replacementBiome);
                        }
                    }
                }
            } finally {
                palettedContainer.release();
            }

            ((LevelChunkSectionAccessor)section).setBiomes(palettedContainer);
        }
    }
    
    public @NotNull Holder<Biome> getBiomeAtBlock(
        LevelHeightAccessor level,
        int x, int y, int z,
        Sampler noiseSampler,
        BiomeInjectionRule.Step step,
        EnumSet<InjectionNeeds> ableToFulfill
    ) {
        int biomeX = x >> 2;
        int biomeY = y >> 2;
        int biomeZ = z >> 2;
        
        return this.getBiome(level, biomeX, biomeY, biomeZ, noiseSampler, step, ableToFulfill);
    }
    
    public @NotNull Holder<Biome> getBiome(
        LevelHeightAccessor level,
        int biomeX, int biomeY, int biomeZ,
        Sampler noiseSampler,
        BiomeInjectionRule.Step step,
        EnumSet<InjectionNeeds> ableToFulfill
    ) {
        if (this.rules.isEmpty()) {
            return this.modernBetaBiomeSource.getNoiseBiome(biomeX, biomeY, biomeZ, noiseSampler);
        }

        BiomeInjectionContext context = this.setupContext(level, biomeX, biomeY, biomeZ, ableToFulfill);

        return this
            .getBiome(context, biomeX, biomeY, biomeZ, noiseSampler, step, ableToFulfill)
            .orElseGet(() -> this.modernBetaBiomeSource.getNoiseBiome(biomeX, biomeY, biomeZ, noiseSampler));
    }
    
    public @NotNull Optional<Holder<Biome>> getOptionalBiome(
        LevelHeightAccessor level,
        int biomeX, int biomeY, int biomeZ,
        Sampler noiseSampler,
        BiomeInjectionRule.Step step,
        EnumSet<InjectionNeeds> ableToFulfill
    ) {
        BiomeInjectionContext context = this.setupContext(level, biomeX, biomeY, biomeZ, ableToFulfill);

        return this.getBiome(context, biomeX, biomeY, biomeZ, noiseSampler, step, ableToFulfill);
    }
    
    private @NotNull Optional<Holder<Biome>> getBiome(
        BiomeInjectionContext context,
        int biomeX, int biomeY, int biomeZ,
        Sampler noiseSampler,
        BiomeInjectionRule.Step step,
        EnumSet<InjectionNeeds> ableToFulfill
    ) {
        Holder<Biome> biome = null;

        for (BiomeInjectionRule rule : this.rules) {
            if (step != BiomeInjectionRule.Step.ALL && step != rule.stepFor())
                continue;

            if (!rule.canFulfill(ableToFulfill))
                continue;

            rule.initIfNeeded();
            biome = rule.apply(context, biomeX, biomeY, biomeZ);
            if (biome != null)
                break;
        }

        return Optional.ofNullable(biome);
    }
    
    private BiomeInjectionContext setupContext(
        LevelHeightAccessor level,
        int biomeX, int biomeY, int biomeZ,
        EnumSet<InjectionNeeds> ableToFulfill
    ) {
        BiomeInjectionContext context = this.context.get();

        if (ableToFulfill.contains(InjectionNeeds.HEIGHTS)) {
            int worldMinY = this.modernBetaChunkGenerator.getMinY();
            int topHeight = this.sampleTopHeight(level, biomeX, biomeZ);
            int minHeight = this.sampleMinHeight(level, biomeX, biomeZ);

            context.setHeights(worldMinY, topHeight, minHeight);
        }

        return context.setPosition((biomeX << 2) + 2, biomeY << 2, (biomeZ << 2) + 2);
    }
    
    private int sampleTopHeight(LevelHeightAccessor level, int biomeX, int biomeZ) {
        int x = (biomeX << 2) + 2;
        int z = (biomeZ << 2) + 2;
        
        return this.modernBetaChunkGenerator.getHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG, level);
    }
    
    private int sampleFloorHeight(LevelHeightAccessor level, int biomeX, int biomeZ) {
        int x = (biomeX << 2) + 2;
        int z = (biomeZ << 2) + 2;
        
        ChunkProvider chunkProvider = this.modernBetaChunkGenerator.getChunkProvider();
        
        return chunkProvider instanceof ChunkProviderNoise chunkProviderNoise ?
            chunkProviderNoise.getHeight(level, x, z, ChunkHeightmap.Type.SURFACE_FLOOR) :
            chunkProvider.getHeight(level, x, z, Heightmap.Types.OCEAN_FLOOR_WG);
    }
    
    private int sampleMinHeight(LevelHeightAccessor level, int centerBiomeX, int centerBiomeZ) {
        int minHeight = Integer.MAX_VALUE;
        
        for (int localBiomeX = -1; localBiomeX <= 1; ++localBiomeX) {
            for (int localBiomeZ = -1; localBiomeZ <= 1; ++localBiomeZ) {
                int biomeX = centerBiomeX + localBiomeX;
                int biomeZ = centerBiomeZ + localBiomeZ;
                
                minHeight = Math.min(minHeight, this.sampleFloorHeight(level, biomeX, biomeZ));
            }
        }
        
        return minHeight;
    }
}
