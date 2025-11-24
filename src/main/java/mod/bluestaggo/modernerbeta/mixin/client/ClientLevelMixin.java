package mod.bluestaggo.modernerbeta.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.client.color.BlockColorSampler;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaLevel;
import mod.bluestaggo.modernerbeta.mixin.BiomeAccessor;
import mod.bluestaggo.modernerbeta.tags.ModernBetaBiomeTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Cursor3D;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
//? if <1.21.11 {
import mod.bluestaggo.modernerbeta.client.color.SkyColorSampler;
import net.minecraft.util.CubicSampler;
import net.minecraft.world.phys.Vec3;
//? }

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(value = ClientLevel.class, priority = 1)
public abstract class ClientLevelMixin implements LevelReader {
    @Shadow public abstract int calculateBlockTint(BlockPos blockPos, ColorResolver colorResolver);

    @WrapOperation(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/Util;make(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;"
        )
    )
    private Object injectCustomTintCaches(Object object, Consumer<?> consumer, Operation<Object> original) {
        return original.call(object, (Consumer<Object2ObjectArrayMap<ColorResolver, BlockTintCache>>) map -> {
            map.put(BiomeColors.GRASS_COLOR_RESOLVER, new BlockTintCache(pos ->
                this.modernBeta$calculateBlockTint(pos, BiomeColors.GRASS_COLOR_RESOLVER)));
            map.put(BiomeColors.FOLIAGE_COLOR_RESOLVER, new BlockTintCache(pos ->
                this.modernBeta$calculateBlockTint(pos, BiomeColors.FOLIAGE_COLOR_RESOLVER)));
            //? if >=1.21.5 {
            map.put(BiomeColors.DRY_FOLIAGE_COLOR_RESOLVER, new BlockTintCache(pos ->
                this.calculateBlockTint(pos, BiomeColors.DRY_FOLIAGE_COLOR_RESOLVER)));
            //? }
            map.put(BiomeColors.WATER_COLOR_RESOLVER, new BlockTintCache(pos ->
                this.modernBeta$calculateBlockTint(pos, BiomeColors.WATER_COLOR_RESOLVER)));
        });
    }

    @Unique
    private int modernBeta$calculateBlockTint(BlockPos pos, ColorResolver resolver) {
        int radius = Minecraft.getInstance().options.biomeBlendRadius().get();
        if (radius == 0) {
            return modernBeta$calculateColour(pos, resolver);
        } else {
            int area = (radius * 2 + 1) * (radius * 2 + 1);
            int r = 0;
            int g = 0;
            int b = 0;
            Cursor3D cursor = new Cursor3D(pos.getX() - radius, pos.getY(), pos.getZ() - radius,
                    pos.getX() + radius, pos.getY(), pos.getZ() + radius);
            BlockPos.MutableBlockPos sample = new BlockPos.MutableBlockPos();

            while (cursor.advance()) {
                sample.set(cursor.nextX(), cursor.nextY(), cursor.nextZ());
                int n = modernBeta$calculateColour(sample, resolver);
                r += (n & 0xFF0000) >> 16;
                g += (n & 0xFF00) >> 8;
                b += n & 0xFF;
            }

            return (r / area & 0xFF) << 16 | (g / area & 0xFF) << 8 | b / area & 0xFF;
        }
    }

    @Unique
    private int modernBeta$calculateColour(BlockPos pos, ColorResolver resolver) {
        Holder<Biome> biomeEntry = this.getBiome(pos);

        if (((ModernBetaLevel) this).modernerBeta$isModded()) {
            BlockColorSampler colorSampler = BlockColorSampler.INSTANCE;
            Clime clime = colorSampler.getClimateSampler().sample(pos.getX(), pos.getZ());
            int climateColor = baseColorAccessor.apply(clime.temp(), clime.rain());
            int finalColor = climateColor;

            int resolvedColour = resolver.getColor(biomeEntry.value(), pos.getX(), pos.getZ());
            if (biomeEntry.is(ModernBetaBiomeTags.HAS_EARLY_RELEASE_SWAMP_COLORS)) {
                resolvedColour = ((resolvedColour & 0xFEFEFE) + 0x4E0E4E) / 2;
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

                return finalColor;
            }
        }

        return resolver.getColor(biomeEntry.value(), pos.getX(), pos.getZ());
    }

    //? if <1.21.11 {
    @WrapOperation(
        method = "getSkyColor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/CubicSampler;gaussianSampleVec3(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/util/CubicSampler$Vec3Fetcher;)Lnet/minecraft/world/phys/Vec3;"
        )
    )
    private Vec3 injectGetSkyColor(Vec3 instance, CubicSampler.Vec3Fetcher j, Operation<Vec3> original, Vec3 pos, float f2) {
        SkyColorSampler sampler = SkyColorSampler.INSTANCE;

        Vec3 originalColor = original.call(instance, j);

        if (sampler.useSkyColor())
            return SkyColorSampler.INSTANCE.getSkyColor(pos);

        return originalColor;
    }
    //? }
}
