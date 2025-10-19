package mod.bluestaggo.modernerbeta.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.longs.Long2DoubleLinkedOpenHashMap;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.TemperatureHeightScaling;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaLevel;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Level.class)
public abstract class MixinLevel implements ModernBetaLevel {
    @Unique private static final int MODERNER_BETA$TEMPERATURE_CACHE_CAPACITY = 128;

    @Unique private boolean modernerBeta$isModded;
    @Unique private ClimateSampler modernerBeta$climateSampler;
    @Unique private TemperatureHeightScaling modernerBeta$temperatureHeightScaling;
    @Unique private final ThreadLocal<Long2DoubleLinkedOpenHashMap> modernerBeta$temperatureCache
        = ThreadLocal.withInitial(() -> {
            var map = new Long2DoubleLinkedOpenHashMap(MODERNER_BETA$TEMPERATURE_CACHE_CAPACITY);
            map.defaultReturnValue(Double.NaN);
            return map;
        });

    @Override
    public boolean modernerBeta$isModded() {
        return this.modernerBeta$isModded;
    }

    @Override
    public void modernerBeta$setModded(boolean toggle) {
        this.modernerBeta$isModded = toggle;
    }

    @Override
    public ClimateSampler modernerBeta$getClimateSampler() {
        return this.modernerBeta$climateSampler;
    }

    @Override
    public void modernerBeta$setClimateSampler(ClimateSampler climateSampler) {
        this.modernerBeta$climateSampler = climateSampler;
    }

    @Override
    public TemperatureHeightScaling modernerBeta$getTemperatureHeightScaling() {
        return this.modernerBeta$temperatureHeightScaling;
    }

    @Override
    public void modernerBeta$setTemperatureHeightScaling(TemperatureHeightScaling temperatureHeightScaling) {
        this.modernerBeta$temperatureHeightScaling = temperatureHeightScaling;
    }

    @Override
    public double modernerBeta$sampleTemperature(Biome biome, BlockPos pos) {
        var temperatureCache = this.modernerBeta$temperatureCache.get();

        long coord = pos.asLong();
        double temperature = temperatureCache.get(coord);
        if (!Double.isNaN(temperature)) {
            return temperature;
        }

        temperature = this.modernerBeta$generateTemperature(biome, pos);
        if (temperatureCache.size() == MODERNER_BETA$TEMPERATURE_CACHE_CAPACITY) {
            temperatureCache.removeFirstDouble();
        }
        temperatureCache.put(coord, temperature);
        return temperature;
    }

    @Unique
    private double modernerBeta$generateTemperature(Biome biome, BlockPos pos) {
        BiomeAccessor biomeAccessor = ((BiomeAccessor)(Object)biome);
        assert biomeAccessor != null;

        ClimateSampler climateSampler = this.modernerBeta$getClimateSampler();
        TemperatureHeightScaling temperatureHeightScaling = this.modernerBeta$getTemperatureHeightScaling();

        if (climateSampler == null && temperatureHeightScaling == null) {
            return biomeAccessor.invokeGetTemperature(
                pos
                //? if >=1.21.2
                , 64
            );
        }

        double temperature = biome.getBaseTemperature();
        if (climateSampler != null) {
            temperature = climateSampler.sample(pos.getX(), pos.getZ()).temp();
        }

        Biome.TemperatureModifier temperatureModifier = biomeAccessor.getClimateSettings().temperatureModifier();
        if (temperatureHeightScaling.supportsModifier(temperatureModifier)) {
            temperature = temperatureModifier.modifyTemperature(pos, (float)temperature);
        }

        if (temperatureHeightScaling != null) {
            temperature = temperatureHeightScaling.modifyTemperature(pos, temperature);
        }

        return temperature;
    }

    @Override
    public Biome.Precipitation modernerBeta$samplePrecipitation(Biome biome, BlockPos pos) {
        if (!biome.hasPrecipitation()) {
            return Biome.Precipitation.NONE;
        }

        if (this.modernerBeta$getTemperatureHeightScaling() == TemperatureHeightScaling.BETA) {
            pos = pos.atY(64);
        }

        double temp = this.modernerBeta$sampleTemperature(biome, pos);
        double snowThreshold = this.modernerBeta$getClimateSampler() != null ? this.modernerBeta$getClimateSampler().getSnowThreshold() : 0.15;

        return temp < snowThreshold ? Biome.Precipitation.SNOW : Biome.Precipitation.RAIN;
    }

    @WrapOperation(
        //? if >=1.21.6 {
        method = "precipitationAt",
        //?} else {
        /*method = "isRainingAt",
        *///?}
        at = @At(
            value = "INVOKE",
            target = VersionCompat.BIOME_GET_PRECIPITATION_TARGET
        )
    )
    public Biome.Precipitation modifyTickPrecipitation(
        Biome biome, BlockPos blockPos
        /*? if >=1.21.2 {*/, int seaLevel/*?}*/
        , Operation<Biome.Precipitation> original
    ) {
        if (!this.modernerBeta$isModded()) {
            //? if >=1.21.2 {
            return original.call(biome, blockPos, seaLevel);
            //?} else {
            /*return original.call(biome, blockPos);
             *///?}
        }
        return this.modernerBeta$samplePrecipitation(biome, blockPos);
    }
}
