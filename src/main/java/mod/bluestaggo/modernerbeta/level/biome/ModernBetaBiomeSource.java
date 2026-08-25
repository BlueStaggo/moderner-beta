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
import mod.bluestaggo.modernerbeta.level.biome.injection.handler.BiomeInjectionHandler;
import mod.bluestaggo.modernerbeta.level.biome.injection.handler.CachedBiomeInjectionHandler;
import mod.bluestaggo.modernerbeta.level.biome.injection.handler.SimpleBiomeInjectionHandler;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.api.level.cavebiome.CaveBiomeProvider;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaChunkGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.QuartPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModernBetaBiomeSource extends BiomeSource /*? >=26.3 {*/ /*implements BiomeManager.NoiseBiomeSource *//*? }*/ {
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
    private BiomeInjectionHandler biomeInjectionHandler;

    private ModernBetaChunkGenerator chunkGenerator;

    public ModernBetaBiomeSource(
        HolderGetter<Biome> biomeRegistry,
        HolderGetter<ModernBetaSettingsPreset> presetRegistry,
        ModernBetaSettings biomeSettings,
        ModernBetaSettings caveBiomeSettings
    ) {
        super();

        biomeSettings = biomeSettings.resolveDefaultPreset();
        caveBiomeSettings = caveBiomeSettings.resolveDefaultPreset();

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
        this.biomeProvider.init();

        this.caveBiomeProvider = ModernBetaRegistries.CAVE_BIOME
            .getValue(caveBiomeSettings.getProvider())
            .apply(caveBiomeSettings, this.biomeRegistry, seed);
        this.caveBiomeProvider.init();

        this.biomeInjectionHandler = new CachedBiomeInjectionHandler(
            new SimpleBiomeInjectionHandler(this.chunkGenerator, this)
        );
    }
    
    @Override
    public @NotNull Holder<Biome> getNoiseBiome(int biomeX, int biomeY, int biomeZ /*? <26.3 {*/, Climate.Sampler noiseSampler /*? }*/) {
        return this.biomeProvider.getBiome(biomeX, biomeY, biomeZ);
    }
    
    @Override
    public @NotNull Set<Holder<Biome>> getBiomesWithin(int startX, int startY, int startZ, int radius /*? <26.3 {*/, Sampler noiseSampler /*? }*/) {
        if (this.chunkGenerator == null)
            //? if >=26.3 {
            /*return Set.of();
            *///? } else {
            return super.getBiomesWithin(startX, startY, startZ, radius, noiseSampler);
            //? }

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

                int y = this.chunkGenerator.getHeight(biomeX << 2, biomeZ << 2, Heightmap.Types.OCEAN_FLOOR_WG, null);

                set.add(this.biomeInjectionHandler.getBiome(
                    null,
                    biomeX, QuartPos.fromBlock(y), biomeZ,
                    BiomeInjectionRule.Step.ALL,
                    InjectionNeeds.all(),
                    true
                ));
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
        //~ if >=26.3 'Climate.Sampler' -> 'RandomState'
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
                
                Holder<Biome> biome = this.biomeInjectionHandler
                    .getBiome(level, biomeX, biomeY, biomeZ, BiomeInjectionRule.Step.ALL, InjectionNeeds.cheapToFulfill(), true);

                if (!biomeSet.contains(biome)) continue;
                
                return Pair.of(new BlockPos(x, y, z), biome);
            }
        }
        
        return null;
    }

    //? if >=26.3 {
    /*@Override
    public BiomeManager.NoiseBiomeSource createResolver(Sampler sampler) {
        return this;
    }
    *///? }

    public Holder<Biome> getCaveBiome(int biomeX, int biomeY, int biomeZ) {
        return this.caveBiomeProvider.getBiome(biomeX, biomeY, biomeZ);
    }
    
    public Holder<Biome> getBiomeForSpawn(int x, int y, int z) {
        if (this.biomeProvider instanceof BiomeResolverBlock biomeResolver) {
            return biomeResolver.getBiomeBlock(x, y, z);
        }
        
        return this.biomeProvider.getBiome(x >> 2, y >> 2, z >> 2);
    }
    
    public Holder<Biome> getBiomeForSurfaceGen(BiomeManager biomeManager, BlockPos pos) {
        if (this.biomeProvider instanceof BiomeResolverBlock biomeResolver)
            return biomeResolver.getBiomeBlock(pos.getX(), pos.getY(), pos.getZ());
        
        return biomeManager.getBiome(pos);
    }

    public ExtendedHolder<Biome> getBiomeForHeightGen(int biomeX, int biomeY, int biomeZ) {
        ExtendedHolder<Biome> biome;
        if (this.biomeProvider instanceof BiomeResolverExtendedId biomeResolver) {
            biome = biomeResolver.getExtendedBiomeId(biomeX, biomeY, biomeZ);
        } else {
            biome = new ExtendedHolder<>(this.biomeProvider.getBiome(biomeX, biomeY, biomeZ));
        }

        return biome;
    }
    
    public void setChunkGenerator(ModernBetaChunkGenerator chunkGenerator) {
        this.chunkGenerator = chunkGenerator;
    }

    public HolderGetter<ModernBetaSettingsPreset> getPresetRegistry() {
        return this.presetRegistry;
    }

    public BiomeProvider getBiomeProvider() {
        return this.biomeProvider;
    }
    
    public CaveBiomeProvider getCaveBiomeProvider() {
        return this.caveBiomeProvider;
    }

    public BiomeInjectionHandler getBiomeInjectionHandler() {
        return this.biomeInjectionHandler;
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

        Set<Holder<Biome>> biomes = new HashSet<>();
        biomes.addAll(biomeProvider.getBiomes());
        biomes.addAll(caveBiomeProvider.getBiomes());

        List<BiomeInjectionRule> injectionRules = biomeSettings.getOrDefault(SettingsComponentTypes.BIOME_INJECTION_RULES);
        for (BiomeInjectionRule injectionRule : injectionRules) {
            biomes.addAll(injectionRule.getPossibleBiomes());
        }

        return biomes.stream();
    }
}
