package mod.bluestaggo.modernerbeta.api.world.biome.climate;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;

public enum TemperatureHeightScaling implements StringRepresentable {
    BETA("beta") {
        @Override
        public double modifyTemperature(BlockPos blockPos, double temp) {
            return temp - ((double)(blockPos.getY() - 64) / 64.0) * 0.3;
        }
    },
    MAJOR_RELEASE("major_release") {
        @Override
        public double modifyTemperature(BlockPos blockPos, double temp) {
            if (blockPos.getY() <= 64) return temp;
            double g = TEMPERATURE_NOISE.getValue((float)blockPos.getX() / 8.0f, (float)blockPos.getZ() / 8.0f, false) * 4.0;
            return temp - (g + (float)blockPos.getY() - 64.0) * 0.05 / 30.0;
        }

        @Override
        public boolean supportsModifier(Biome.TemperatureModifier temperatureModifier) {
            return temperatureModifier != Biome.TemperatureModifier.NONE;
        }
    },
    CAVES_AND_CLIFFS("caves_and_cliffs") {
        @Override
        public double modifyTemperature(BlockPos blockPos, double temp) {
            if (blockPos.getY() <= 80) return temp;
            double g = TEMPERATURE_NOISE.getValue((float)blockPos.getX() / 8.0f, (float)blockPos.getZ() / 8.0f, false) * 8.0;
            return temp - (g + (float)blockPos.getY() - 80.0) * 0.05 / 40.0;
        }

        @Override
        public boolean supportsModifier(Biome.TemperatureModifier temperatureModifier) {
            return temperatureModifier != Biome.TemperatureModifier.NONE;
        }
    },
    NONE("none") {
        @Override
        public double modifyTemperature(BlockPos blockPos, double temp) {
            return temp;
        }
    };

    private static final PerlinSimplexNoise TEMPERATURE_NOISE = new PerlinSimplexNoise(new WorldgenRandom(new LegacyRandomSource(1234L)), ImmutableList.of(0));

    public final String id;

    TemperatureHeightScaling(String id) {
        this.id = id;
    }

    public abstract double modifyTemperature(BlockPos blockPos, double temp);

    public boolean supportsModifier(Biome.TemperatureModifier temperatureModifier) {
        return false;
    }

    @Override
    public String getSerializedName() {
        return this.id;
    }
}
