package mod.bluestaggo.modernerbeta.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.collection.Weighted;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.chunk.Chunk;

import java.util.Optional;
import java.util.function.Consumer;

public final class VersionCompat {
    public static <T> T accessPool(Pool<T> pool, Random random) {
        //? if >=1.21.5 {
        return pool.get(random);
        //?} else {
        /*return pool.getDataOrEmpty(random).orElseThrow();
        *///?}
    }

    public static <T> T getWeightedValue
    //? if >=1.21.5 {
    (Weighted<T> weighted) {
        return weighted.value();
    }
    //?} else {
    /*(Weighted.Present<T> weighted) {
        //? if >=1.20.5 {
        return weighted.data();
        //?} else {
        //return weighted.getData();
        //?}
    }
    *///?}

    public static <T> void forEachValueInPool(Pool<T> pool, Consumer<T> consumer) {
        for (var entry : pool.getEntries()) {
            consumer.accept(getWeightedValue(entry));
        }
    }

    public static void addSpawnEntry(SpawnSettings.Builder spawnSettings, SpawnGroup spawnGroup, EntityType<?> entityType, int weight, int minGroupSize, int maxGroupSize) {
        //? if >=1.21.5 {
        spawnSettings.spawn(spawnGroup, weight, new SpawnSettings.SpawnEntry(entityType, minGroupSize, maxGroupSize));
        //?} else {
        /*spawnSettings.spawn(spawnGroup, new SpawnSettings.SpawnEntry(entityType, weight, minGroupSize, maxGroupSize));
        *///?}
    }

    public static void setBlockState(Chunk chunk, BlockPos pos, BlockState blockState) {
        //? if >=1.21.5 {
        chunk.setBlockState(pos, blockState);
        //?} else {
        /*chunk.setBlockState(pos, blockState, false);
        *///?}
    }

    public static void setBlockState(Chunk chunk, BlockPos pos, BlockState blockState, int flags) {
        //? if >=1.21.5 {
        chunk.setBlockState(pos, blockState, flags);
        //?} else {
        /*chunk.setBlockState(pos, blockState, (flags & Block.MOVED) != 0);
        *///?}
    }

    public static <T> T unwrap(T t) {
        return t;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T> T unwrap(Optional<T> optional) {
        return optional.orElseThrow();
    }

    public static <T> T unwrapOrElse(T t, T orElse) {
        return t;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T> T unwrapOrElse(Optional<T> optional, T orElse) {
        return optional.orElse(orElse);
    }
}
