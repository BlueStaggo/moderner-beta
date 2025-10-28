package mod.bluestaggo.modernerbeta.client.color;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.mixin.BiomeAccessor;
import mod.bluestaggo.modernerbeta.mixin.client.RenderSectionRegionAccessor;
import mod.bluestaggo.modernerbeta.settings.component.ClimateDistribution;
import mod.bluestaggo.modernerbeta.tags.ModernBetaBiomeTags;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public final class BlockColorSampler {
    private static final int CLIME_CACHE_CAPACITY = 128;

    public static final BlockColorSampler INSTANCE = new BlockColorSampler();

    public final BlockColormap colormapGrass;
    public final BlockColormap colormapFoliage;
    public final BlockColormap colormapWater;
    public final BlockColormap colormapUnderwater;
    
    private ClimateSampler climateSampler;

    private final Long2ObjectLinkedOpenHashMap<Clime> climeCache = new Long2ObjectLinkedOpenHashMap<>(CLIME_CACHE_CAPACITY);
    private final LoadingCache<Class<?>, Optional<Field>> viewLevelFieldCache = CacheBuilder.newBuilder()
        .build(new CacheLoader<>() {
            private static final Set<String> PRIORITY_LEVEL_FIELD_NAMES = Set.of("level", "world");

            @Override
            public @NotNull Optional<Field> load(@NotNull Class<?> clazz) {
                List<Field> potentialFields = Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> LevelReader.class.isAssignableFrom(field.getType()))
                    .toList();

                if (potentialFields.isEmpty()) {
                    return Optional.empty();
                }

                Field field = potentialFields.get(0);
                if (potentialFields.size() > 1) {
                    field = potentialFields.stream()
                        .filter(f -> PRIORITY_LEVEL_FIELD_NAMES.contains(f.getName()))
                        .findFirst()
                        .orElse(field);
                }

                if (!Modifier.isPublic(field.getModifiers()) && !field.trySetAccessible()) {
                    return Optional.empty();
                }

                return Optional.of(field);
            }
        });


    private BlockColorSampler() {
        this.colormapGrass = new BlockColormap();
        this.colormapFoliage = new BlockColormap();
        this.colormapWater = new BlockColormap();
        this.colormapUnderwater = new BlockColormap();
        
        this.climateSampler = null;
    }

    public ClimateSampler getClimateSampler() {
        return this.climateSampler;
    }
    
    public void setClimateSampler(ClimateSampler climateSampler) {
        this.climateSampler = climateSampler;
        this.climeCache.clear();
    }

    private Clime sampleClime(BlockPos pos) {
        return this.sampleClime(pos.getX(), pos.getZ());
    }

    private Clime sampleClime(int x, int z) {
        synchronized (this.climeCache) {
            long coord = ChunkPos.asLong(x, z);
            Clime clime = this.climeCache.get(coord);
            if (clime != null) {
                return clime;
            }

            if (this.climeCache.size() == CLIME_CACHE_CAPACITY) {
                this.climeCache.removeFirst();
            }

            clime = this.climateSampler.sample(x, z);
            this.climeCache.put(coord, clime);
            return clime;
        }
    }

    public int getGrassColor(BlockState ignoredState, BlockAndTintGetter view, BlockPos pos, int ignoredTintNdx) {
        if (view == null || pos == null) { // Appears to enter here when loading color for inventory block
            return GrassColor.getDefaultColor();
        }

        if (this.useBiomeColor()) {
            BiomeManager biomeAccess = getBiomeAccessFromView(view);
            if (biomeAccess != null) {
                return this.sampleModifiedColorMaybeLerped(
                    biomeAccess,
                    pos,
                    //? if >=1.21.11 {
                    /*BiomeSpecialEffects::grassColorOverride,
                    BiomeSpecialEffects::grassColorModifier,
                    *///? } else {
                    BiomeSpecialEffects::getGrassColorOverride,
                    BiomeSpecialEffects::getGrassColorModifier,
                    //? }
                    GrassColor::get
                );
            }

            Clime clime = this.sampleClime(pos);
            return GrassColor.get(clime.temp(), clime.rain());
        }

        return BiomeColors.getAverageGrassColor(view, pos);
    }

    public int getPetalColor(BlockState state, BlockAndTintGetter view, BlockPos pos, int tintNdx) {
        if (tintNdx == 0)
            return 0xFFFFFFFF;

        return getShortGrassColor(state, view, pos, tintNdx);
    }

    public int getTallGrassColor(BlockState state, BlockAndTintGetter view, BlockPos pos, int tintNdx) {
        return getShortGrassColor(state, view, state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos, tintNdx);
    }
    
    public int getShortGrassColor(BlockState ignoredState, BlockAndTintGetter view, BlockPos pos, int ignoredTintNdx) {
        if (view == null || pos == null) { // Appears to enter here when loading color for inventory block
            return GrassColor.getDefaultColor();
        }
        
        if (this.useBiomeColor()) {
            if (this.getClimateDistribution().fuzzyGrass()) {
                int x = pos.getX();
                int y = pos.getY();
                int z = pos.getZ();

                long shift = x * 0x2FC20FL + z * 0x5D8875L + y;
                shift = shift * shift * 0x285B825L + shift * 11L;
                pos = pos.offset(
                    (int)(shift >> 14 & 31L),
                    (int)(shift >> 19 & 31L),
                    (int)(shift >> 24 & 31L)
                );
            }

            BiomeManager biomeAccess = getBiomeAccessFromView(view);
            if (biomeAccess != null) {
                return this.sampleModifiedColorMaybeLerped(
                    biomeAccess,
                    pos,
                    //? if >=1.21.11 {
                    /*BiomeSpecialEffects::grassColorOverride,
                    BiomeSpecialEffects::grassColorModifier,
                    *///? } else {
                    BiomeSpecialEffects::getGrassColorOverride,
                    BiomeSpecialEffects::getGrassColorModifier,
                    //? }
                    GrassColor::get
                );
            }

            Clime clime = this.sampleClime(pos);
            return GrassColor.get(clime.temp(), clime.rain());
        }
        
        return BiomeColors.getAverageGrassColor(view, pos);
    }
    
    public int getFoliageColor(BlockState ignoredState, BlockAndTintGetter view, BlockPos pos, int ignoredTintNdx) {
        if (view == null || pos == null) { // Appears to enter here when loading color for inventory block
            return 0xFF48B518;
        }
        
        if (this.useBiomeColor()) {
            BiomeManager biomeAccess = getBiomeAccessFromView(view);
            if (biomeAccess != null) {
                return this.sampleModifiedColorMaybeLerped(
                    biomeAccess,
                    pos,
                    //? if >=1.21.11 {
                    /*BiomeSpecialEffects::foliageColorOverride,
                    *///? } else {
                    BiomeSpecialEffects::getFoliageColorOverride,
                    //? }
                    effects -> BiomeSpecialEffects.GrassColorModifier.NONE,
                    FoliageColor::get
                );
            }

            Clime clime = this.sampleClime(pos);
            return FoliageColor.get(clime.temp(), clime.rain());
        }
        
        return BiomeColors.getAverageFoliageColor(view, pos);
    }
    
    public int getWaterColor(BlockState ignoredState, BlockAndTintGetter view, BlockPos pos, int ignoredTintNdx) {
        if (view == null || pos == null) {
            return 0xFFFFFFFF;
        }

        if (this.useWaterColor()) {
            Clime clime = this.sampleClime(pos);
            return this.colormapWater.getColor(clime.temp(), clime.rain());
        }
        
        return BiomeColors.getAverageWaterColor(view, pos);
    }
    
    public int getSugarCaneColor(BlockState ignoredState, BlockAndTintGetter view, BlockPos pos, int ignoredTintNdx) {
        if (view == null || pos == null) {
            return 0xFFFFFFFF;
        }

        if (this.useBiomeColor()) {
            return 0xFFFFFFFF;
        }
        
        return BiomeColors.getAverageGrassColor(view, pos);
    }
    
    public boolean useBiomeColor() {
        return this.climateSampler != null && this.climateSampler.useBiomeColor();
    }
    
    public boolean useWaterColor() {
        return this.climateSampler != null && this.climateSampler.useWaterColor();
    }

    public ClimateDistribution getClimateDistribution() {
        if (this.climateSampler == null) {
            return ClimateDistribution.DEFAULT;
        }
        return this.climateSampler.getDistribution();
    }

    private int sampleModifiedColorMaybeLerped(BiomeManager biomeAccess, BlockPos pos,
                                               Function<BiomeSpecialEffects, Optional<Integer>> customColorAccessor,
                                               Function<BiomeSpecialEffects, BiomeSpecialEffects.GrassColorModifier> grassColorModifierAccessor,
                                               ClimateToColorOperator baseColorAccessor) {
        if (this.getClimateDistribution().smoothBorders()) {
            return this.sampleModifiedColorLerped(biomeAccess, pos, customColorAccessor, grassColorModifierAccessor, baseColorAccessor);
        } else {
            return this.sampleModifiedColor(biomeAccess, pos, customColorAccessor, grassColorModifierAccessor, baseColorAccessor);
        }
    }

    private int sampleModifiedColorLerped(BiomeManager biomeAccess, BlockPos pos,
                                          Function<BiomeSpecialEffects, Optional<Integer>> customColorAccessor,
                                          Function<BiomeSpecialEffects, BiomeSpecialEffects.GrassColorModifier> grassColorModifierAccessor,
                                          ClimateToColorOperator baseColorAccessor) {
        int r = 0;
        int g = 0;
        int b = 0;

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    int color = this.sampleModifiedColor(biomeAccess, pos.offset(x, y, z), customColorAccessor, grassColorModifierAccessor, baseColorAccessor);
                    r += (color >> 16) & 255;
                    g += (color >> 8) & 255;
                    b += color & 255;
                }
            }
        }

        return (r / 27) << 16 | (g / 27) << 8 | (b / 27);
    }

    private int sampleModifiedColor(BiomeManager biomeAccess, BlockPos pos,
                                    Function<BiomeSpecialEffects, Optional<Integer>> customColorAccessor,
                                    Function<BiomeSpecialEffects, BiomeSpecialEffects.GrassColorModifier> grassColorModifierAccessor,
                                    ClimateToColorOperator baseColorAccessor) {
        Clime clime = this.sampleClime(pos);
        int climateColor = baseColorAccessor.apply(clime.temp(), clime.rain());

        Holder<Biome> biomeEntry = biomeAccess.getBiome(pos);

        int finalColor = climateColor;

        if (biomeEntry.is(ModernBetaBiomeTags.HAS_EARLY_RELEASE_SWAMP_COLORS)) {
            finalColor = ((finalColor & 0xFEFEFE) + 0x4E0E4E) / 2;
        } else {
            Biome biome = biomeEntry.value();

            Optional<Integer> optionalCustomColor = customColorAccessor.apply(biome.getSpecialEffects());
            if (optionalCustomColor.isPresent()) {
                // Reverse-engineer the custom color as a multiplier for the base climate color

                int customColor = optionalCustomColor.get();

                Biome.ClimateSettings weather = ((BiomeAccessor)(Object)biome).getClimateSettings();

                float temperature = Mth.clamp(weather.temperature(), 0.0F, 1.0F);
                float downfall = Mth.clamp(weather.downfall(), 0.0F, 1.0F);
                int baseColor = baseColorAccessor.apply(temperature, downfall);

                // customR = baseR * modR / 255
                // customR * 255 / baseR = modR
                int modR = ((customColor >> 16) & 255) * 255 / ((baseColor >> 16) & 255);
                int modG = ((customColor >> 8) & 255) * 255 / ((baseColor >> 8) & 255);
                int modB = (customColor & 255) * 255 / (baseColor & 255);

                int r = Mth.clamp(((climateColor >> 16) & 255) * modR / 255, 0, 255);
                int g = Mth.clamp(((climateColor >> 8) & 255) * modG / 255, 0, 255);
                int b = Mth.clamp((climateColor & 255) * modB / 255, 0, 255);
                finalColor = r << 16 | g << 8 | b;
            }

            BiomeSpecialEffects.GrassColorModifier grassColorModifier = grassColorModifierAccessor.apply(biome.getSpecialEffects());
            if (grassColorModifier != BiomeSpecialEffects.GrassColorModifier.NONE) {
                finalColor = grassColorModifier.modifyColor(pos.getX(), pos.getZ(), finalColor);
            }
        }

        return finalColor;
    }

    private BiomeManager getBiomeAccessFromView(BlockAndTintGetter tintGetter) {
        if (tintGetter instanceof Level level) {
            return level.getBiomeManager();
        }

        if (tintGetter instanceof LevelReader levelReader) {
            return levelReader.getBiomeManager();
        }

        if (tintGetter instanceof
            //? if >=1.21.6 {
            net.minecraft.client.renderer.chunk.RenderSectionRegion
            //?} else {
            /*net.minecraft.client.renderer.chunk.RenderChunkRegion
            *///?}
        ) {
            return ((RenderSectionRegionAccessor)tintGetter).getLevel().getBiomeManager();
        }

        Optional<Field> levelField;
        try {
            levelField = this.viewLevelFieldCache.get(tintGetter.getClass());
        } catch (ExecutionException e) {
            this.viewLevelFieldCache.put(tintGetter.getClass(), Optional.empty());
            e.printStackTrace();
            return null;
        }

        if (levelField.isPresent()) {
            try {
                return ((LevelReader)levelField.get().get(tintGetter)).getBiomeManager();
            } catch (IllegalAccessException e) {
                this.viewLevelFieldCache.put(tintGetter.getClass(), Optional.empty());
                e.printStackTrace();
            }
        }

        return null;
    }

    @FunctionalInterface
    private interface ClimateToColorOperator {
        int apply(double temperature, double downfall);
    }
}
