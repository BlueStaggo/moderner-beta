package mod.bluestaggo.modernerbeta.level.chunk.surface;

import com.mojang.serialization.Codec;
//? if >=26.2
//import com.mojang.serialization.MapCodec;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaSurfaceContext;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.SurfaceProperties;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.util.StringRepresentable;
//? if <26.2
import net.minecraft.util.KeyDispatchDataCodec;
//? if >=26.3 {
/*import net.minecraft.world.level.levelgen.material.MaterialRuleContext;
import net.minecraft.world.level.levelgen.material.condition.ConditionEvaluator;
import net.minecraft.world.level.levelgen.material.condition.MaterialCondition;
*///? } else {
import net.minecraft.world.level.levelgen.SurfaceRules;
//? }

public final class ModernBetaSurfaceRules {
    private ModernBetaSurfaceRules() {
    }

    public static SurfaceRules.ConditionSource surfaceProperty(SurfaceProperty property) {
        return new SurfacePropertyConditionSource(property);
    }

    public static SurfaceRules.ConditionSource yBlockCheck(Height height, int offset, int surfaceDepthMultiplier) {
        return new SettingsYConditionSource(height, offset, surfaceDepthMultiplier, false);
    }

    public static SurfaceRules.ConditionSource yStartCheck(Height height, int offset, int surfaceDepthMultiplier) {
        return new SettingsYConditionSource(height, offset, surfaceDepthMultiplier, true);
    }

    @SuppressWarnings("unchecked")
    public static void register(IRegistryHandler<?> handler) {
        IRegistryHandler<com.mojang.serialization.MapCodec<? extends SurfaceRules.ConditionSource>> registry =
            (IRegistryHandler<com.mojang.serialization.MapCodec<? extends SurfaceRules.ConditionSource>>) handler;
        registry.register(ModernerBeta.createId("surface_property"), SurfacePropertyConditionSource.CODEC);
        registry.register(ModernerBeta.createId("settings_y"), SettingsYConditionSource.CODEC);
    }

    private static ModernBetaSurfaceContext view(SurfaceRules.Context context) {
        return (ModernBetaSurfaceContext)(Object)context;
    }

    private static SurfaceProperties surface(ModernBetaSurfaceContext context) {
        ChunkProvider provider = context.modernerBeta$getChunkProvider();
        return provider == null
            ? SurfaceProperties.DEFAULT
            : provider.getChunkSettings().getOrDefault(SettingsComponentTypes.SURFACE_PROPERTIES);
    }

    public enum SurfaceProperty implements StringRepresentable {
        GENERATE_SANDSTONE("generate_sandstone"),
        GRAVEL_OCEAN_BED("gravel_ocean_bed");

        private final String name;

        SurfaceProperty(String name) {
            this.name = name;
        }

        private boolean get(SurfaceProperties surface) {
            return switch (this) {
                case GENERATE_SANDSTONE -> surface.generateSandstone();
                case GRAVEL_OCEAN_BED -> surface.gravelOceanBed();
            };
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

    public enum Height implements StringRepresentable {
        SEA_LEVEL("sea_level"),
        GRAVEL_OCEAN_BED("gravel_ocean_bed");

        private final String name;

        Height(String name) {
            this.name = name;
        }

        private int get(ModernBetaSurfaceContext context, SurfaceProperties surface) {
            ChunkProvider provider = context.modernerBeta$getChunkProvider();
            int seaLevel = provider == null ? 64 : provider.getSeaLevel();
            return this == GRAVEL_OCEAN_BED ? seaLevel - surface.gravelOceanBedDepth() : seaLevel;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

    private record SurfacePropertyConditionSource(SurfaceProperty property) implements SurfaceRules.ConditionSource {
        private static final com.mojang.serialization.MapCodec<SurfacePropertyConditionSource> CODEC = VersionCompat.createMaybeMapCodec(
            instance -> instance.group(
                StringRepresentable.fromEnum(SurfaceProperty::values).fieldOf("property").forGetter(SurfacePropertyConditionSource::property)
            ).apply(instance, SurfacePropertyConditionSource::new)
        );

        @Override
        //~ if >=26.2 'KeyDispatchDataCodec' -> 'MapCodec'
        public KeyDispatchDataCodec<SurfacePropertyConditionSource> codec() {
            //? if >=26.2
            //return CODEC;
            //? if <26.2
            return KeyDispatchDataCodec.of(CODEC);
        }

        @Override
        //~ if >=26.3 'apply' -> 'compile'
        public SurfaceRules.Condition apply(SurfaceRules.Context context) {
            boolean value = this.property.get(surface(view(context)));
            return () -> value;
        }
    }

    private record SettingsYConditionSource(
        Height height,
        int offset,
        int surfaceDepthMultiplier,
        boolean addStoneDepth
    ) implements SurfaceRules.ConditionSource {
        private static final com.mojang.serialization.MapCodec<SettingsYConditionSource> CODEC = VersionCompat.createMaybeMapCodec(
            instance -> instance.group(
                StringRepresentable.fromEnum(Height::values).fieldOf("height").forGetter(SettingsYConditionSource::height),
                Codec.INT.optionalFieldOf("offset", 0).forGetter(SettingsYConditionSource::offset),
                Codec.intRange(-20, 20).optionalFieldOf("surface_depth_multiplier", 0).forGetter(SettingsYConditionSource::surfaceDepthMultiplier),
                Codec.BOOL.optionalFieldOf("add_stone_depth", false).forGetter(SettingsYConditionSource::addStoneDepth)
            ).apply(instance, SettingsYConditionSource::new)
        );

        @Override
        //~ if >=26.2 'KeyDispatchDataCodec' -> 'MapCodec'
        public KeyDispatchDataCodec<SettingsYConditionSource> codec() {
            //? if >=26.2
            //return CODEC;
            //? if <26.2
            return KeyDispatchDataCodec.of(CODEC);
        }

        @Override
        //~ if >=26.3 'apply' -> 'compile'
        public SurfaceRules.Condition apply(SurfaceRules.Context context) {
            ModernBetaSurfaceContext view = view(context);
            int target = this.height.get(view, surface(view)) + this.offset;
            return () -> view.modernerBeta$getBlockY() + (this.addStoneDepth ? view.modernerBeta$getStoneDepthAbove() : 0)
                >= target + view.modernerBeta$getSurfaceDepth() * this.surfaceDepthMultiplier;
        }
    }
}
