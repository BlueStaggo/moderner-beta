//~dotResourceLocation
package mod.bluestaggo.modernerbeta.world.biome.injector;

import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProviderNoise;
import mod.bluestaggo.modernerbeta.mixin.LevelChunkSectionAccessor;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkHeightmap;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.biome.injector.BiomeInjectionRules.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.world.cavebiome.provider.CaveBiomeProviderNone;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Optional;
import java.util.function.Predicate;

public class BiomeInjector {
    public enum BiomeInjectionStep {
        PRE,  // Injects before surface generation step.
        POST, // Injects after surface generation step.
        ALL   // Injects for structure generation, spawn location.
    }
    
    public static final int OCEAN_START_DEPTH = 4;
    public static final int OCEAN_DEEP_START_DEPTH = 16;
    public static final int CAVE_START_DEPTH = 8;
    
    private final ModernBetaChunkGenerator modernBetaChunkGenerator;
    private final ModernBetaBiomeSource modernBetaBiomeSource;
    
    private final BiomeInjectionRules rulesPre;
    private final BiomeInjectionRules rulesPost;
    private final BiomeInjectionRules rulesAll;

    public BiomeInjector(ModernBetaChunkGenerator modernBetaChunkGenerator, ModernBetaBiomeSource modernBetaBiomeSource) {
        this.modernBetaChunkGenerator = modernBetaChunkGenerator;
        this.modernBetaBiomeSource = modernBetaBiomeSource;
        
        ModernBetaSettings settingsBiome;
        settingsBiome = ModernBetaSettings.fromCompound(this.modernBetaBiomeSource.getBiomeSettings())
            .mapPreset(modernBetaChunkGenerator.getPresetRegistry(), ModernBetaSettingsPreset::biomeSettings);

        boolean useOceanBiomes = settingsBiome.getOrDefault(SettingsComponentTypes.USE_OCEAN_BIOMES);
        
        Predicate<BiomeInjectionContext> cavePredicate = context -> 
            context.getY() >= context.worldMinY && context.getY() + CAVE_START_DEPTH < context.minHeight;

        Predicate<BiomeInjectionContext> oceanPredicate = context -> 
            modernBetaBiomeSource.hasOceanBiomes() && this.atOceanDepth(context.topHeight, OCEAN_START_DEPTH);

        Predicate<BiomeInjectionContext> deepOceanPredicate = context -> 
            modernBetaBiomeSource.hasOceanBiomes() && this.atOceanDepth(context.topHeight, OCEAN_DEEP_START_DEPTH);
        
        BiomeInjectionRules.Builder builderPre = new BiomeInjectionRules.Builder();
        BiomeInjectionRules.Builder builderPost = new BiomeInjectionRules.Builder();
        BiomeInjectionRules.Builder builderAll = new BiomeInjectionRules.Builder();

        if (!(this.modernBetaBiomeSource.getCaveBiomeProvider() instanceof CaveBiomeProviderNone)) {
            builderPost.add(cavePredicate, this.modernBetaBiomeSource::getCaveBiome);
        }

        if (useOceanBiomes) {
            builderPost.add(deepOceanPredicate, this.modernBetaBiomeSource::getDeepOceanBiome);
            builderPost.add(oceanPredicate, this.modernBetaBiomeSource::getOceanBiome);
        }
        
        builderAll.add(builderPre).add(builderPost);
        
        this.rulesPre = builderPre.build();
        this.rulesPost = builderPost.build();
        this.rulesAll = builderAll.build();
    }
    
    public void injectBiomes(ChunkAccess chunk, Sampler noiseSampler, BiomeInjectionStep step) {
        if (this.rulesAll.isEmpty()) {
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
        for (int sectionY = view.getMinSectionY(); sectionY < view.getMaxSectionY(); ++sectionY) {
            int sectionYNdx = chunk.getSectionIndexFromSectionY(sectionY);
            LevelChunkSection section = chunk.getSection(sectionYNdx);
            
            PalettedContainerRO<Holder<Biome>> readableContainer = section.getBiomes();
            PalettedContainer<Holder<Biome>> palettedContainer = section.getBiomes().recreate();
            
            for (int localBiomeX = 0; localBiomeX < 4; ++localBiomeX) {
                for (int localBiomeZ = 0; localBiomeZ < 4; ++localBiomeZ) {
                    int biomeX = localBiomeX + startBiomeX;
                    int biomeZ = localBiomeZ + startBiomeZ;
                    
                    for (int localBiomeY = 0; localBiomeY < 4; ++localBiomeY) {
                        int biomeY = localBiomeY + sectionY << 2;
                        
                        Holder<Biome> initialBiome = readableContainer.get(localBiomeX, localBiomeY, localBiomeZ);
                        Holder<Biome> replacementBiome = this.getOptionalBiome(view, biomeX, biomeY, biomeZ, noiseSampler, step).orElse(initialBiome);
                        
                        palettedContainer.set(localBiomeX, localBiomeY, localBiomeZ, replacementBiome);
                    }   
                }
            }
            
            ((LevelChunkSectionAccessor)section).setBiomes(palettedContainer);
        }
    }
    
    public Holder<Biome> getBiomeAtBlock(LevelHeightAccessor level, int x, int y, int z, Sampler noiseSampler, BiomeInjectionStep step) {
        int biomeX = x >> 2;
        int biomeY = y >> 2;
        int biomeZ = z >> 2;
        
        return this.getBiome(level, biomeX, biomeY, biomeZ, noiseSampler, step);
    }

    public String getBiomeNameAtBlock(LevelHeightAccessor level, int x, int y, int z, Sampler noiseSampler, BiomeInjectionStep step) {
        int biomeX = x >> 2;
        int biomeY = y >> 2;
        int biomeZ = z >> 2;

        ResourceKey<Biome> key = this.getBiome(level, biomeX, biomeY, biomeZ, noiseSampler, step).unwrapKey().orElse(null);
        if (key == null) return "???";
        return key.location().toString();
    }
    
    public Holder<Biome> getBiome(LevelHeightAccessor level, int biomeX, int biomeY, int biomeZ, Sampler noiseSampler, BiomeInjectionStep step) {
        if (this.rulesAll.isEmpty()) {
            return this.modernBetaBiomeSource.getNoiseBiome(biomeX, biomeY, biomeZ, noiseSampler);
        }

        BiomeInjectionContext context = this.createContext(level, biomeX, biomeY, biomeZ);

        return this
            .getBiome(context, biomeX, biomeY, biomeZ, noiseSampler, step)
            .orElseGet(() -> this.modernBetaBiomeSource.getNoiseBiome(biomeX, biomeY, biomeZ, noiseSampler));
    }
    
    public Optional<Holder<Biome>> getOptionalBiome(LevelHeightAccessor level, int biomeX, int biomeY, int biomeZ, Sampler noiseSampler, BiomeInjectionStep step) {
        BiomeInjectionContext context = this.createContext(level, biomeX, biomeY, biomeZ);

        return this.getBiome(context, biomeX, biomeY, biomeZ, noiseSampler, step);
    }
    
    private Optional<Holder<Biome>> getBiome(BiomeInjectionContext context, int biomeX, int biomeY, int biomeZ, Sampler noiseSampler, BiomeInjectionStep step) {
        Holder<Biome> biome = switch(step) {
            case PRE -> this.rulesPre.test(context, biomeX, biomeY, biomeZ);
            case POST -> this.rulesPost.test(context, biomeX, biomeY, biomeZ);
            case ALL -> this.rulesAll.test(context, biomeX, biomeY, biomeZ);
        };
        
        return Optional.ofNullable(biome);
    }
    
    private BiomeInjectionContext createContext(LevelHeightAccessor level, int biomeX, int biomeY, int biomeZ) {
        int y = biomeY << 2;
        
        int worldMinY = this.modernBetaChunkGenerator.getMinY();
        int topHeight = this.sampleTopHeight(level, biomeX, biomeZ);
        int minHeight = this.sampleMinHeight(level, biomeX, biomeZ);

        return new BiomeInjectionContext(worldMinY, topHeight, minHeight).setY(y);
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

    private boolean atOceanDepth(int topHeight, int oceanDepth) {
        return topHeight < this.modernBetaChunkGenerator.getSeaLevel() - oceanDepth;
    }
}
