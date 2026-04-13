//~registryGet
//~minBuild
//~dotLocation
package mod.bluestaggo.modernerbeta.level.biome;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.level.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.api.level.biome.BiomeResolverBlock;
import mod.bluestaggo.modernerbeta.api.level.biome.BiomeResolverExtendedId;
import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionRule;
import mod.bluestaggo.modernerbeta.level.biome.injection.InjectionNeeds;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.api.level.cavebiome.CaveBiomeProvider;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaChunkGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.QuartPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModernBetaBiomeSource extends BiomeSource {
    public static final com.mojang.serialization.MapCodec<ModernBetaBiomeSource> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            RegistryOps.retrieveGetter(Registries.BIOME),
            RegistryOps.retrieveGetter(ModernBetaResourceKeys.SETTINGS_PRESET),
            ModernBetaSettings.WORLD_CODEC.fieldOf("provider_settings").forGetter(biomeSource -> biomeSource.biomeSettings),
            ModernBetaSettings.WORLD_CODEC.fieldOf("cave_provider_settings").forGetter(biomeSource -> biomeSource.caveBiomeSettings)
        ).apply(instance, (instance).stable(ModernBetaBiomeSource::new))
    );

    private final HolderGetter<Biome> biomeRegistry;
    private final HolderGetter<ModernBetaSettingsPreset> presetRegistry;
    private final ModernBetaSettings biomeSettings;
    private final ModernBetaSettings caveBiomeSettings;

    private BiomeProvider biomeProvider;
    private CaveBiomeProvider caveBiomeProvider;

    private ModernBetaChunkGenerator chunkGenerator;

    public ModernBetaBiomeSource(
        HolderGetter<Biome> biomeRegistry,
        HolderGetter<ModernBetaSettingsPreset> presetRegistry,
        ModernBetaSettings biomeSettings,
        ModernBetaSettings caveBiomeSettings
    ) {
        super();

        if (!ModernerBeta.GENERATING_DATA && ModernBetaSettings.DEFAULT_PRESET_ID.equals(
            biomeSettings.getOrDefault(SettingsComponentTypes.PRESET))) {
            biomeSettings = biomeSettings
                .extend()
                .remove(SettingsComponentTypes.PRESET)
                .add(SettingsComponentTypes.PRESET, ModernerBeta.config
                    .getOrDefault(SettingsComponentTypes.CONFIG_MISCELLANEOUS).defaultSettingsPreset())
                .build();
        }

        if (!ModernerBeta.GENERATING_DATA && ModernBetaSettings.DEFAULT_PRESET_ID.equals(
            caveBiomeSettings.getOrDefault(SettingsComponentTypes.PRESET))) {
            caveBiomeSettings = caveBiomeSettings
                .extend()
                .remove(SettingsComponentTypes.PRESET)
                .add(SettingsComponentTypes.PRESET, ModernerBeta.config
                    .getOrDefault(SettingsComponentTypes.CONFIG_MISCELLANEOUS).defaultSettingsPreset())
                .build();
        }

        this.biomeRegistry = biomeRegistry;
        this.presetRegistry = presetRegistry;
        this.biomeSettings = biomeSettings;
        this.caveBiomeSettings = caveBiomeSettings;
    }
    
    public void initProvider(long seed) {
        ModernBetaSettings biomeSettings = this.biomeSettings.mapPreset(this.presetRegistry, ModernBetaSettingsPreset::biomeSettings);
        ModernBetaSettings caveBiomeSettings = this.caveBiomeSettings.mapPreset(this.presetRegistry, ModernBetaSettingsPreset::caveBiomeSettings);
        
        this.biomeProvider = ModernBetaRegistries.BIOME
            .getValue(biomeSettings.getProvider())
            .apply(biomeSettings, this.biomeRegistry, seed);
        
        this.caveBiomeProvider = ModernBetaRegistries.CAVE_BIOME
            .getValue(caveBiomeSettings.getProvider())
            .apply(caveBiomeSettings, this.biomeRegistry, seed);
    }
    
    @Override
    public @NotNull Holder<Biome> getNoiseBiome(int biomeX, int biomeY, int biomeZ, Climate.Sampler noiseSampler) {
        return this.biomeProvider.getBiome(biomeX, biomeY, biomeZ);
    }
    
    @Override
    public @NotNull Set<Holder<Biome>> getBiomesWithin(int startX, int startY, int startZ, int radius, Sampler noiseSampler) {
        if (this.chunkGenerator == null)
            return super.getBiomesWithin(startX, startY, startZ, radius, noiseSampler);
        
        int minX = QuartPos.fromBlock(startX - radius);
        int minZ = QuartPos.fromBlock(startZ - radius);
        
        int maxX = QuartPos.fromBlock(startX + radius);
        int maxZ = QuartPos.fromBlock(startZ + radius);
        
        int rangeX = maxX - minX + 1;
        int rangeZ = maxZ - minZ + 1;
        
        HashSet<Holder<Biome>> set = Sets.newHashSet();
        for (int localZ = 0; localZ < rangeZ; ++localZ) {
            for (int localX = 0; localX < rangeX; ++localX) {
                int biomeX = minX + localX;
                int biomeZ = minZ + localZ;
                
                int x = biomeX << 2;
                int z = biomeZ << 2;
                int y = this.chunkGenerator.getHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG, null);
                
                set.add(this.chunkGenerator.getBiomeInjector().getBiomeAtBlock(null, x, y, z, noiseSampler, BiomeInjectionRule.Step.ALL, InjectionNeeds.all()));
            }
        }
        
        return set;
    }
    
    @Override
    public Pair<BlockPos, Holder<Biome>> findClosestBiome3d(
        BlockPos origin,
        int radius,
        int horizontalBlockCheckInterval,
        int verticalBlockCheckInterval,
        Predicate<Holder<Biome>> predicate,
        Climate.Sampler noiseSampler,
        LevelReader level
    ) {
        if (this.chunkGenerator == null) {
            return super.findClosestBiome3d(
                origin,
                radius,
                horizontalBlockCheckInterval,
                verticalBlockCheckInterval,
                predicate,
                noiseSampler,
                level
            );
        }
        
        Set<Holder<Biome>> biomeSet = this.possibleBiomes()
            .stream()
            .filter(predicate)
            .collect(Collectors.toUnmodifiableSet());
        
        if (biomeSet.isEmpty()) {
            return null;
        }
        
        int searchRadius = Math.floorDiv(radius, horizontalBlockCheckInterval);
        int[] sections = Mth
            .outFromOrigin(origin.getY(), level.getMinY() + 1, VersionCompat.getTopYExclusive(level), verticalBlockCheckInterval)
            .toArray();
        
        for (BlockPos.MutableBlockPos mutable : BlockPos.spiralAround(BlockPos.ZERO, searchRadius, Direction.EAST, Direction.SOUTH)) {
            int x = origin.getX() + mutable.getX() * horizontalBlockCheckInterval;
            int z = origin.getZ() + mutable.getZ() * horizontalBlockCheckInterval;
            
            int biomeX = QuartPos.fromBlock(x);
            int biomeZ = QuartPos.fromBlock(z);
            
            for (int y : sections) {
                int biomeY = QuartPos.fromBlock(y);
                
                Holder<Biome> biome = this.chunkGenerator
                    .getBiomeInjector()
                    .getBiome(level, biomeX, biomeY, biomeZ, noiseSampler, BiomeInjectionRule.Step.ALL, InjectionNeeds.cheapToFulfill());

                if (!biomeSet.contains(biome)) continue;
                
                return Pair.of(new BlockPos(x, y, z), biome);
            }
        }
        
        return null;
    }
    
    public Holder<Biome> getCaveBiome(int biomeX, int biomeY, int biomeZ) {
        return this.caveBiomeProvider.getBiome(biomeX, biomeY, biomeZ);
    }
    
    public Holder<Biome> getBiomeForSpawn(int x, int y, int z) {
        if (this.biomeProvider instanceof BiomeResolverBlock biomeResolver) {
            return biomeResolver.getBiomeBlock(x, y, z);
        }
        
        return this.biomeProvider.getBiome(x >> 2, y >> 2, z >> 2);
    }
    
    public Holder<Biome> getBiomeForSurfaceGen(WorldGenRegion region, BlockPos pos) {
        if (this.biomeProvider instanceof BiomeResolverBlock biomeResolver)
            return biomeResolver.getBiomeBlock(pos.getX(), pos.getY(), pos.getZ());
        
        return region.getBiome(pos);
    }

    public ExtendedBiomeId getBiomeForHeightGen(int biomeX, int biomeY, int biomeZ) {
        if (this.biomeProvider instanceof BiomeResolverExtendedId biomeResolver)
            return biomeResolver.getExtendedBiomeId(biomeX, biomeY, biomeZ);

        return ExtendedBiomeId.of(this.biomeProvider.getBiome(biomeX, biomeY, biomeZ).unwrapKey().orElseThrow().location());
    }
    
    public void setChunkGenerator(ModernBetaChunkGenerator chunkGenerator) {
        this.chunkGenerator = chunkGenerator;
    }
    
    public BiomeProvider getBiomeProvider() {
        return this.biomeProvider;
    }
    
    public CaveBiomeProvider getCaveBiomeProvider() {
        return this.caveBiomeProvider;
    }
    
    public ModernBetaSettings getBiomeSettings() {
        return this.biomeSettings;
    }
    
    public ModernBetaSettings getCaveBiomeSettings() {
        return this.caveBiomeSettings;
    }
    
    @SuppressWarnings("unchecked")
    public static void register(IRegistryHandler<?> handler) {
        var registryHandler = (IRegistryHandler<com.mojang.serialization.MapCodec<?>>) handler;
        registryHandler.register(ModernerBeta.createId(ModernerBeta.MOD_ID), CODEC);
    }

    @Override
    protected com.mojang.serialization.MapCodec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    protected @NotNull Stream<Holder<Biome>> collectPossibleBiomes() {
        ModernBetaSettings biomeSettings = this.biomeSettings.mapPreset(this.presetRegistry, ModernBetaSettingsPreset::biomeSettings);
        ModernBetaSettings caveBiomeSettings = this.caveBiomeSettings.mapPreset(this.presetRegistry, ModernBetaSettingsPreset::caveBiomeSettings);
        
        BiomeProvider biomeProvider = ModernBetaRegistries.BIOME
            .getValue(biomeSettings.getProvider())
            .apply(biomeSettings, biomeRegistry, 0L);
        
        CaveBiomeProvider caveBiomeProvider = ModernBetaRegistries.CAVE_BIOME
            .getValue(caveBiomeSettings.getProvider())
            .apply(caveBiomeSettings, biomeRegistry, 0L);

        List<Holder<Biome>> biomes = new ArrayList<>();
        biomes.addAll(biomeProvider.getBiomes());
        biomes.addAll(caveBiomeProvider.getBiomes());
        
        return biomes.stream();
    }
}