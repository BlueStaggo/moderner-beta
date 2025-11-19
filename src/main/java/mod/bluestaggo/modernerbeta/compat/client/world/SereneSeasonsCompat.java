package mod.bluestaggo.modernerbeta.compat.client.world;

import com.google.common.collect.Multimap;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SereneSeasonsCompat implements ColorModifierHelper {
    private static final TagKey<Biome> TROPICAL_BIOMES = TagKey.create(Registries.BIOME, VersionCompat.id("sereneseasons:tropical_biomes"));
    private final Multimap<Object, Object> resolverOverrides;

    private final Object grassResolverType;
    private final Object foliageResolverType;

    private final Method getSeasonStateMethod;
    private final Method getTropicalSeasonMethod;
    private final Method getSubSeasonMethod;

    private final Method applySeasonalGrassColoringMethod;
    private final Method applySeasonalFoliageColoringMethod;

    private final Method applyColorOverrideMethod;

    @SuppressWarnings("unchecked")
    public SereneSeasonsCompat() {
        /*
         * What good is an API that is proprietary? What good does it serve for the developer?
         * Making a proprietary API is a huge loss towards Open Source developers and the community.
         * This horrid mess is a result of all of this.
         */
        try {
            Class<?> seasonColorHandlers = Class.forName("sereneseasons.season.SeasonColorHandlers");
            Class<?> colorOverride = Class.forName("sereneseasons.season.SeasonColorHandlers$ColorOverride");
            Class<?> resolverType = Class.forName("sereneseasons.season.SeasonColorHandlers$ResolverType");
            Class<?> seasonHelper = Class.forName("sereneseasons.api.season.SeasonHelper");
            Class<?> seasonState = Class.forName("sereneseasons.api.season.ISeasonState");
            Class<?> seasonColorProvider = Class.forName("sereneseasons.api.season.ISeasonColorProvider");
            Class<?> seasonColorUtil = Class.forName("sereneseasons.util.SeasonColorUtil");

            Field resolverOverridesField = seasonColorHandlers.getDeclaredField("resolverOverrides");
            resolverOverridesField.setAccessible(true);
            resolverOverrides = (Multimap<Object, Object>) resolverOverridesField.get(null);

            Field grassResolverTypeField = resolverType.getField("GRASS");
            Field foliageResolverTypeField = resolverType.getField("FOLIAGE");

            this.grassResolverType = grassResolverTypeField.get(null);
            this.foliageResolverType = foliageResolverTypeField.get(null);

            this.getSeasonStateMethod = seasonHelper.getMethod("getSeasonState", Level.class);
            this.getTropicalSeasonMethod = seasonState.getMethod("getTropicalSeason");
            this.getSubSeasonMethod = seasonState.getMethod("getSubSeason");

            this.applySeasonalGrassColoringMethod = seasonColorUtil.getMethod("applySeasonalGrassColouring",
                    seasonColorProvider, Holder.class, int.class);
            this.applySeasonalFoliageColoringMethod = seasonColorUtil.getMethod("applySeasonalFoliageColouring",
                    seasonColorProvider, Holder.class, int.class);

            this.applyColorOverrideMethod = colorOverride.getMethod("apply",
                    int.class, int.class, int.class, Holder.class, double.class, double.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialise Serene Seasons compatibility!", e);
        }
    }

    @Override
    public int modifyGrass(int original, Holder<Biome> biome, BlockPos pos) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;

        if (level == null)
            return original;
        
        if (biome != null) {
            Object colorProvider = getColorProvider(level, biome);

            try {
                int modifiedBaseColor = (int) applySeasonalGrassColoringMethod.invoke(null, colorProvider, biome, original);
                int modifiedColor = modifiedBaseColor;

                for (Object override : resolverOverrides.get(this.grassResolverType)) {
                    modifiedColor = (int) applyColorOverrideMethod.invoke(override, original, modifiedBaseColor, modifiedColor, biome, pos.getX(), pos.getZ());
                }

                return modifiedColor;
            } catch (Exception e) {
                throw new RuntimeException("Reflection into Serene Seasons failed!", e);
            }
        }

        return original;
    }

    @Override
    public int modifyFoliage(int original, Holder<Biome> biome, BlockPos pos) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;

        if (level == null)
            return original;

        if (biome != null) {
            Object colorProvider = getColorProvider(level, biome);

            try {
                int modifiedBaseColor = (int) applySeasonalFoliageColoringMethod.invoke(null, colorProvider, biome, original);
                int modifiedColor = modifiedBaseColor;

                for (Object override : resolverOverrides.get(this.foliageResolverType)) {
                    modifiedColor = (int) applyColorOverrideMethod.invoke(override, original, modifiedBaseColor, modifiedColor, biome, pos.getX(), pos.getZ());
                }

                return modifiedColor;
            } catch (Exception e) {
                throw new RuntimeException("Reflection into Serene Seasons failed!", e);
            }
        }

        return original;
    }

    private Object getColorProvider(Level level, Holder<Biome> biome) {
        try {
            Object seasonState = getSeasonStateMethod.invoke(null, level);
            return biome.is(TROPICAL_BIOMES) ? getTropicalSeasonMethod.invoke(seasonState) : getSubSeasonMethod.invoke(seasonState);
        } catch (Exception e) {
            throw new RuntimeException("Reflection into Serene Seasons failed!", e);
        }
    }
}
