package mod.bluestaggo.modernerbeta.client.color;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.mixin.AccessorBiome;
import mod.bluestaggo.modernerbeta.mixin.client.AccessorChunkRendererRegion;
import mod.bluestaggo.modernerbeta.settings.component.ClimateDistribution;
import mod.bluestaggo.modernerbeta.tags.ModernBetaBiomeTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
//? if >=1.21 {
import net.minecraft.world.biome.FoliageColors;
import net.minecraft.world.biome.GrassColors;
//?} else {
/*import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
*///?}

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public final class BlockColorSampler {
    private static final int CLIME_CACHE_CAPACITY = 128;
    private static final Class<?> SODIUM_LEVEL_SLICE_CLASS;
    private static final Field SODIUM_LEVEL_SLICE_LEVEL_FIELD;

    public static final BlockColorSampler INSTANCE = new BlockColorSampler();

    public final BlockColormap colormapGrass;
    public final BlockColormap colormapFoliage;
    public final BlockColormap colormapWater;
    public final BlockColormap colormapUnderwater;
    
    private ClimateSampler climateSampler;

    private final Long2ObjectLinkedOpenHashMap<Clime> climeCache = new Long2ObjectLinkedOpenHashMap<>(CLIME_CACHE_CAPACITY);

    static {
        Class<?> sodiumLevelSliceClass = null;
        Field sodiumLevelSliceLevelField = null;

        List<Pair<String, String>> potentialTargets = List.of(
            new Pair<>("net.caffeinemc.mods.sodium.client.world.LevelSlice", "level"), // Sodium 0.6
            new Pair<>("net.caffeinemc.mods.sodium.client.world.WorldSlice", "world"), // Sodium 0.6 (Pre-merge)
            new Pair<>("me.jellysquid.mods.sodium.client.world.WorldSlice", "world"), // Sodium 0.5 / Embeddium 0.3
            new Pair<>("org.embeddedt.embeddium.impl.world.WorldSlice", "world") // Embeddium 1.0
        );

        for (Pair<String, String> target : potentialTargets) {
            try {
                // Sodium 0.5 names
                sodiumLevelSliceClass = Class.forName(target.getLeft());
                sodiumLevelSliceLevelField = sodiumLevelSliceClass.getDeclaredField(target.getRight());
                sodiumLevelSliceLevelField.setAccessible(true);
            } catch (ClassNotFoundException | NoSuchFieldException ignored) {
                // If the class or field doesn't exist then the target mod and version probably isn't loaded. Try a different target.
            }
        }

        SODIUM_LEVEL_SLICE_CLASS = sodiumLevelSliceClass;
        SODIUM_LEVEL_SLICE_LEVEL_FIELD = sodiumLevelSliceLevelField;
    }

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
            long coord = ChunkPos.toLong(x, z);
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

    public int getGrassColor(BlockState ignoredState, BlockRenderView view, BlockPos pos, int ignoredTintNdx) {
        if (view == null || pos == null) { // Appears to enter here when loading color for inventory block
            return GrassColors.getDefaultColor();
        }

        if (this.useBiomeColor()) {
            World world = getWorldFromView(view);
            if (world != null) {
                return this.sampleModifiedColorMaybeLerped(
                    world,
                    pos,
                    BiomeEffects::getGrassColor,
                    BiomeEffects::getGrassColorModifier,
                    GrassColors::getColor
                );
            }

            Clime clime = this.sampleClime(pos);
            return GrassColors.getColor(clime.temp(), clime.rain());
        }

        return BiomeColors.getGrassColor(view, pos);
    }

    public int getPetalColor(BlockState state, BlockRenderView view, BlockPos pos, int tintNdx) {
        if (tintNdx == 0)
            return 0xFFFFFFFF;

        return getShortGrassColor(state, view, pos, tintNdx);
    }

    public int getTallGrassColor(BlockState state, BlockRenderView view, BlockPos pos, int tintNdx) {
        return getShortGrassColor(state, view, state.get(TallPlantBlock.HALF) == DoubleBlockHalf.UPPER ? pos.down() : pos, tintNdx);
    }
    
    public int getShortGrassColor(BlockState ignoredState, BlockRenderView view, BlockPos pos, int ignoredTintNdx) {
        if (view == null || pos == null) { // Appears to enter here when loading color for inventory block
            return GrassColors.getDefaultColor();
        }
        
        if (this.useBiomeColor()) {
            if (this.getClimateDistribution().fuzzyGrass()) {
                int x = pos.getX();
                int y = pos.getY();
                int z = pos.getZ();

                long shift = x * 0x2FC20FL + z * 0x5D8875L + y;
                shift = shift * shift * 0x285B825L + shift * 11L;
                pos = pos.add(
                    (int)(shift >> 14 & 31L),
                    (int)(shift >> 19 & 31L),
                    (int)(shift >> 24 & 31L)
                );
            }

            World world = getWorldFromView(view);
            if (world != null) {
                return this.sampleModifiedColorMaybeLerped(
                    world,
                    pos,
                    BiomeEffects::getGrassColor,
                    BiomeEffects::getGrassColorModifier,
                    GrassColors::getColor
                );
            }

            Clime clime = this.sampleClime(pos);
            return GrassColors.getColor(clime.temp(), clime.rain());
        }
        
        return BiomeColors.getGrassColor(view, pos);
    }
    
    public int getFoliageColor(BlockState ignoredState, BlockRenderView view, BlockPos pos, int ignoredTintNdx) {
        if (view == null || pos == null) { // Appears to enter here when loading color for inventory block
            return 0xFF48B518;
        }
        
        if (this.useBiomeColor()) {
            World world = getWorldFromView(view);
            if (world != null) {
                return this.sampleModifiedColorMaybeLerped(
                    world,
                    pos,
                    BiomeEffects::getFoliageColor,
                    effects -> BiomeEffects.GrassColorModifier.NONE,
                    FoliageColors::getColor
                );
            }

            Clime clime = this.sampleClime(pos);
            return FoliageColors.getColor(clime.temp(), clime.rain());
        }
        
        return BiomeColors.getFoliageColor(view, pos);
    }
    
    public int getWaterColor(BlockState ignoredState, BlockRenderView view, BlockPos pos, int ignoredTintNdx) {
        if (view == null || pos == null) {
            return 0xFFFFFFFF;
        }

        if (this.useWaterColor()) {
            Clime clime = this.sampleClime(pos);
            return this.colormapWater.getColor(clime.temp(), clime.rain());
        }
        
        return BiomeColors.getWaterColor(view, pos);
    }
    
    public int getSugarCaneColor(BlockState ignoredState, BlockRenderView view, BlockPos pos, int ignoredTintNdx) {
        if (view == null || pos == null) {
            return 0xFFFFFFFF;
        }

        if (this.useBiomeColor()) {
            return 0xFFFFFFFF;
        }
        
        return BiomeColors.getGrassColor(view, pos);
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

    private int sampleModifiedColorMaybeLerped(World world, BlockPos pos,
                                               Function<BiomeEffects, Optional<Integer>> customColorAccessor,
                                               Function<BiomeEffects, BiomeEffects.GrassColorModifier> grassColorModifierAccessor,
                                               ClimateToColorOperator baseColorAccessor) {
        if (this.getClimateDistribution().smoothBorders()) {
            return this.sampleModifiedColorLerped(world, pos, customColorAccessor, grassColorModifierAccessor, baseColorAccessor);
        } else {
            return this.sampleModifiedColor(world, pos, customColorAccessor, grassColorModifierAccessor, baseColorAccessor);
        }
    }

    private int sampleModifiedColorLerped(World world, BlockPos pos,
                                          Function<BiomeEffects, Optional<Integer>> customColorAccessor,
                                          Function<BiomeEffects, BiomeEffects.GrassColorModifier> grassColorModifierAccessor,
                                          ClimateToColorOperator baseColorAccessor) {
        int r = 0;
        int g = 0;
        int b = 0;

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    int color = this.sampleModifiedColor(world, pos.add(x, y, z), customColorAccessor, grassColorModifierAccessor, baseColorAccessor);
                    r += (color >> 16) & 255;
                    g += (color >> 8) & 255;
                    b += color & 255;
                }
            }
        }

        return (r / 27) << 16 | (g / 27) << 8 | (b / 27);
    }

    private int sampleModifiedColor(World world, BlockPos pos,
                                    Function<BiomeEffects, Optional<Integer>> customColorAccessor,
                                    Function<BiomeEffects, BiomeEffects.GrassColorModifier> grassColorModifierAccessor,
                                    ClimateToColorOperator baseColorAccessor) {
        Clime clime = this.sampleClime(pos);
        int climateColor = baseColorAccessor.apply(clime.temp(), clime.rain());

        RegistryEntry<Biome> biomeEntry = world.getBiome(pos);

        int finalColor = climateColor;

        if (biomeEntry.isIn(ModernBetaBiomeTags.HAS_EARLY_RELEASE_SWAMP_COLORS)) {
            finalColor = ((finalColor & 0xFEFEFE) + 0x4E0E4E) / 2;
        } else {
            Biome biome = biomeEntry.value();

            Optional<Integer> optionalCustomColor = customColorAccessor.apply(biome.getEffects());
            if (optionalCustomColor.isPresent()) {
                // Reverse-engineer the custom color as a multiplier for the base climate color

                int customColor = optionalCustomColor.get();

                Biome.Weather weather = ((AccessorBiome)(Object)biome).getWeather();
                int baseColor = baseColorAccessor.apply(weather.temperature(), weather.downfall());

                // customR = baseR * modR / 255
                // customR * 255 / baseR = modR
                int modR = ((customColor >> 16) & 255) * 255 / ((baseColor >> 16) & 255);
                int modG = ((customColor >> 8) & 255) * 255 / ((baseColor >> 8) & 255);
                int modB = (customColor & 255) * 255 / (baseColor & 255);

                int r = MathHelper.clamp(((climateColor >> 16) & 255) * modR / 255, 0, 255);
                int g = MathHelper.clamp(((climateColor >> 8) & 255) * modG / 255, 0, 255);
                int b = MathHelper.clamp((climateColor & 255) * modB / 255, 0, 255);
                finalColor = r << 16 | g << 8 | b;
            }

            BiomeEffects.GrassColorModifier grassColorModifier = grassColorModifierAccessor.apply(biome.getEffects());
            if (grassColorModifier != BiomeEffects.GrassColorModifier.NONE) {
                finalColor = grassColorModifier.getModifiedGrassColor(pos.getX(), pos.getZ(), finalColor);
            }
        }

        return finalColor;
    }

    private static World getWorldFromView(BlockRenderView view) {
        if (view instanceof World world) {
            return world;
        }

        if (view instanceof ChunkRendererRegion) {
            return ((AccessorChunkRendererRegion)view).getWorld();
        }

        if (SODIUM_LEVEL_SLICE_CLASS != null && SODIUM_LEVEL_SLICE_CLASS.isInstance(view)) {
            try {
                return (World) SODIUM_LEVEL_SLICE_LEVEL_FIELD.get(view);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    @FunctionalInterface
    private interface ClimateToColorOperator {
        int apply(double temperature, double downfall);
    }
}
