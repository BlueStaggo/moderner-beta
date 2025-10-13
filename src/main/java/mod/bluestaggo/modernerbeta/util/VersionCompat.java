package mod.bluestaggo.modernerbeta.util;

import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
//? if >=1.21.5
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
//? if <1.21.5
/*import net.minecraft.util.random.WeightedEntry;*/
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

//? if >=1.20.5 {
import com.mojang.serialization.MapCodec;
//?} else {
/*import com.mojang.serialization.Codec;
*///?}

public final class VersionCompat {
    public static final Block SHORT_GRASS =
        //? if >=1.20.3 {
        Blocks.SHORT_GRASS;
        //?} else {
        /*Blocks.GRASS;
        *///?}

    public static final String BIOME_GET_PRECIPITATION_TARGET =
        //? if >=1.21.2 {
        "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;I)Lnet/minecraft/world/level/biome/Biome$Precipitation;";
        //?} else {
        /*"Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;";
        *///?}

    public static <T> T accessPool(WeightedList<T> pool, RandomSource random) {
        //? if >=1.21.5 {
        return pool.getRandomOrThrow(random);
        //?} else {
        /*return pool.getRandomValue(random).orElseThrow();
        *///?}
    }

    public static <T> T getWeightedValue
    //? if >=1.21.5 {
    (Weighted<T> weighted) {
        return weighted.value();
    }
    //?} else {
    /*(WeightedEntry.Wrapper<T> weighted) {
        //? if >=1.20.5 {
        /^turn weighted.data();
      ^///?} else {
        return weighted.getData();
        //?}
    }
    *///?}

    public static <T> void forEachValueInPool(WeightedList<T> pool, Consumer<T> consumer) {
        for (var entry : pool.unwrap()) {
            consumer.accept(getWeightedValue(entry));
        }
    }

    public static void addSpawnEntry(MobSpawnSettings.Builder spawnSettings, MobCategory spawnGroup, EntityType<?> entityType, int weight, int minGroupSize, int maxGroupSize) {
        //? if >=1.21.5 {
        spawnSettings.addSpawn(spawnGroup, weight, new MobSpawnSettings.SpawnerData(entityType, minGroupSize, maxGroupSize));
        //?} else {
        /*spawnSettings.addSpawn(spawnGroup, new MobSpawnSettings.SpawnerData(entityType, weight, minGroupSize, maxGroupSize));
        *///?}
    }

    public static void setBlockState(ChunkAccess chunk, BlockPos pos, BlockState blockState) {
        //? if >=1.21.5 {
        chunk.setBlockState(pos, blockState);
        //?} else {
        /*chunk.setBlockState(pos, blockState, false);
        *///?}
    }

    public static void setBlockState(ChunkAccess chunk, BlockPos pos, BlockState blockState, int flags) {
        //? if >=1.21.5 {
        chunk.setBlockState(pos, blockState, flags);
        //?} else {
        /*chunk.setBlockState(pos, blockState, (flags & Block.UPDATE_MOVE_BY_PISTON) != 0);
        *///?}
    }

    public static int getTopYExclusive(LevelHeightAccessor heightLimitView) {
        //? if >=1.21.2 {
        return heightLimitView.getMaxY() + 1;
        //?} else {
        /*return heightLimitView.getMaxBuildHeight();
        *///?}
    }

    @SuppressWarnings("unused")
    public static <T> T unwrap(T t) {
        return t;
    }

    @SuppressWarnings({"unused", "OptionalUsedAsFieldOrParameterType"})
    public static <T> T unwrap(Optional<T> optional) {
        return optional.orElseThrow();
    }

    @SuppressWarnings("unused")
    public static <T> T unwrapOrElse(T t, T orElse) {
        return t;
    }

    @SuppressWarnings({"unused", "OptionalUsedAsFieldOrParameterType"})
    public static <T> T unwrapOrElse(Optional<T> optional, T orElse) {
        return optional.orElse(orElse);
    }

    public static <T> T getOrThrow(DataResult<T> result) {
        //? if >=1.20.3 {
        return result.getOrThrow();
        //?} else {
        /*return result.getOrThrow(false, string -> {});
        *///?}
    }
    
    public static ResourceLocation id(String string) {
        //? if >=1.21 {
        return ResourceLocation.parse(string);
        //?} else {
        /*return new ResourceLocation(string);
        *///?}
    }

    public static ResourceLocation vanillaId(String string) {
        //? if >=1.21 {
        return ResourceLocation.withDefaultNamespace(string);
         //?} else {
        /*return new ResourceLocation(string);
        *///?}
    }

    //? if >=1.20.5 {
    public static <O> MapCodec<O> createMaybeMapCodec(final Function<RecordCodecBuilder.Instance<O>, ? extends App<RecordCodecBuilder.Mu<O>, O>> builder) {
        return RecordCodecBuilder.mapCodec(builder);
    }
    //?} else {
    /*public static <O> Codec<O> createMaybeMapCodec(final Function<RecordCodecBuilder.Instance<O>, ? extends App<RecordCodecBuilder.Mu<O>, O>> builder) {
        return RecordCodecBuilder.create(builder);
    }
    *///?}
}
