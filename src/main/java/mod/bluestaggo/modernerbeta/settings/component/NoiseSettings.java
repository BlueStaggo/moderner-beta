package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.QuartPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.function.Function;

public record NoiseSettings(int minY, int height, int noiseSizeHorizontal, int noiseSizeVertical) {
    public static final Codec<NoiseSettings> CODEC = RecordCodecBuilder.<NoiseSettings>create(
        i -> i.group(
            Codec.intRange(DimensionType.MIN_Y, DimensionType.MAX_Y).fieldOf("min_y").forGetter(NoiseSettings::minY),
            Codec.intRange(0, DimensionType.Y_SIZE).fieldOf("height").forGetter(NoiseSettings::height),
            Codec.intRange(1, 4).fieldOf("size_horizontal").forGetter(NoiseSettings::noiseSizeHorizontal),
            Codec.intRange(1, 4).fieldOf("size_vertical").forGetter(NoiseSettings::noiseSizeVertical)
        )
        .apply(i, NoiseSettings::new)
    ).comapFlatMap(NoiseSettings::guardY, Function.identity());

    private static DataResult<NoiseSettings> guardY(final NoiseSettings dimensionType) {
        if (dimensionType.minY() + dimensionType.height() > DimensionType.MAX_Y + 1) {
            return DataResult.error(() -> "min_y + height cannot be higher than: " + (DimensionType.MAX_Y + 1));
        } else if (dimensionType.height() % 16 != 0) {
            return DataResult.error(() -> "height has to be a multiple of 16");
        } else {
            return dimensionType.minY() % 16 != 0 ? DataResult.error(() -> "min_y has to be a multiple of 16") : DataResult.success(dimensionType);
        }
    }

    public static NoiseSettings create(final int minY, final int height, final int noiseSizeHorizontal, final int noiseSizeVertical) {
        NoiseSettings noiseSettings = new NoiseSettings(minY, height, noiseSizeHorizontal, noiseSizeVertical);
        guardY(noiseSettings).error().ifPresent(error -> {
            throw new IllegalStateException(error.message());
        });
        return noiseSettings;
    }

    public static NoiseSettings fromVanilla(net.minecraft.world.level.levelgen.NoiseSettings vanillaSettings) {
        return new NoiseSettings(
            vanillaSettings.minY(),
            vanillaSettings.height(),
            //? if >=26.3 {
            /*1, 2
            *///? } else {
            vanillaSettings.noiseSizeHorizontal(),
            vanillaSettings.noiseSizeVertical()
            //? }
        );
    }

    public net.minecraft.world.level.levelgen.NoiseSettings toVanilla() {
        return new net.minecraft.world.level.levelgen.NoiseSettings(
            this.minY,
            this.height
            //? if <26.3 {
            , this.noiseSizeHorizontal(),
            this.noiseSizeVertical()
            //? }
        );
    }

    public int getCellHeight() {
        return QuartPos.toBlock(this.noiseSizeVertical());
    }

    public int getCellWidth() {
        return QuartPos.toBlock(this.noiseSizeHorizontal());
    }

    public NoiseSettings clampToHeightAccessor(final LevelHeightAccessor heightAccessor) {
        int newMinY = Math.max(this.minY, heightAccessor.getMinY());
        int newHeight = Math.min(this.minY + this.height, heightAccessor.getMaxY() + 1) - newMinY;
        return new NoiseSettings(newMinY, newHeight, this.noiseSizeHorizontal, this.noiseSizeVertical);
    }
}
