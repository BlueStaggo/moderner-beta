package mod.bluestaggo.modernerbeta.util;

import com.mojang.serialization.DataResult;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSampler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.collection.Weighted;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.chunk.Chunk;

import java.util.Optional;
import java.util.function.Consumer;

public final class VersionCompat {
    public static final Block SHORT_GRASS =
        //? if >=1.20.3 {
        Blocks.SHORT_GRASS;
        //?} else {
        /*Blocks.GRASS;
        *///?}

    public static final String BIOME_GET_PRECIPITATION_TARGET =
        //? if >=1.21.2 {
        "Lnet/minecraft/world/biome/Biome;getPrecipitation(Lnet/minecraft/util/math/BlockPos;I)Lnet/minecraft/world/biome/Biome$Precipitation;";
        //?} else {
        /*"Lnet/minecraft/world/biome/Biome;getPrecipitation(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome$Precipitation;";
        *///?}

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
        /^return weighted.getData();
        ^///?}
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

    public static int getTopYExclusive(HeightLimitView heightLimitView) {
        //? if >=1.21.2 {
        return heightLimitView.getTopYInclusive() + 1;
        //?} else {
        /*return heightLimitView.getTopY();
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
    
    public static Identifier id(String string) {
        //? if >=1.21 {
        return Identifier.of(string);
        //?} else {
        /*return new Identifier(string);
        *///?}
    }

    public static Identifier vanillaId(String string) {
        //? if >=1.21 {
        return Identifier.ofVanilla(string);
         //?} else {
        /*return new Identifier(string);
        *///?}
    }
}
