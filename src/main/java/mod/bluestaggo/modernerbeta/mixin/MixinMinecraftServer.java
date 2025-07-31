package mod.bluestaggo.modernerbeta.mixin;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProviderFinite;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bluestaggo.modernerbeta.world.chunk.provider.ChunkProviderIndev;
import mod.bluestaggo.modernerbeta.world.chunk.provider.indev.IndevTheme;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.ServerWorldProperties;
import org.slf4j.event.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if <1.20.2 {
/*import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.server.network.SpawnLocating;
*///?} else {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//?}

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {
    @Inject(method = "setupSpawn", at = @At("RETURN"))
    private static void injectSetupSpawn(ServerWorld world, ServerWorldProperties worldProperties, boolean bonusChest, boolean debugWorld, /*? >=1.21.9 {*/ /*net.minecraft.world.chunk.ChunkLoadProgress arg, *//*?}*/ CallbackInfo ci) {
        ChunkGenerator chunkGenerator = world.getChunkManager().getChunkGenerator();

        // Set old spawn angle (doesn't seem to work?)
        if (chunkGenerator instanceof ModernBetaChunkGenerator) {
            worldProperties.setSpawnPos(
                //? if >=1.20.5 {
                worldProperties.getSpawnPos(), 
                //?} else {
                /*new BlockPos(
                    worldProperties.getSpawnX(),
                    worldProperties.getSpawnY(),
                    worldProperties.getSpawnZ()
                ),
                *///?}
                -90.0f
            );
        }
    }

    //? if >=1.20.2 {
    @WrapOperation(
    //?} else {
    /*@Redirect(
    *///?}
        method = "setupSpawn", 
        at = @At(
            value = "INVOKE", 
            target = "Lnet/minecraft/server/network/SpawnLocating;findServerSpawnPoint(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/ChunkPos;)Lnet/minecraft/util/math/BlockPos;"
        )
    )
    private static BlockPos redirectSpawnLocating(
        ServerWorld world, ChunkPos chunkPos
        /*? if >=1.20.2 {*/, Operation<BlockPos> original/*?}*/
    ) {
        ChunkGenerator chunkGenerator = world.getChunkManager().getChunkGenerator();
        //? if <1.20.2
        /*BlockPos spawnPos = SpawnLocating.findServerSpawnPoint(world, chunkPos);*/
        
        if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
            ChunkProvider chunkProvider = modernBetaChunkGenerator.getChunkProvider();
            
            world.getGameRules().get(GameRules.SPAWN_RADIUS).set(0, world.getServer()); // Ensure a centered spawn
            //? if >=1.20.2 {
            BlockPos spawnPos = chunkProvider.getSpawnLocator().locateSpawn().orElseGet(() -> original.call(world, chunkPos));
            //?} else {
            /*spawnPos = chunkProvider.getSpawnLocator().locateSpawn().orElse(spawnPos);
            *///?}
            
            if (spawnPos != null && ModernerBeta.DEV_ENV) {
                int x = spawnPos.getX();
                int y = spawnPos.getY();
                int z = spawnPos.getZ();
                
                ModernerBeta.log(Level.INFO, String.format("Spawning at %d/%d/%d", x, y, z));
            }
            
            if (spawnPos != null && chunkProvider instanceof ChunkProviderIndev chunkProviderIndev) {
                // Generate Indev house
                chunkProviderIndev.generateIndevHouse(world, spawnPos);
                
                // Set Indev world properties.
                setIndevProperties(world, chunkProviderIndev.getLevelTheme());
            }
            
            if (chunkProvider instanceof ChunkProviderFinite) {
                ChunkProviderFinite.resetPhase();
            }

            //? if >=1.20.2
            return spawnPos;
        }

        //? if >=1.20.2 {
        return original.call(world, chunkPos);
        //?} else {
        /*return spawnPos;
        *///?}
    }
    
    @Unique
    private static void setIndevProperties(ServerWorld world, IndevTheme theme) {
        switch(theme) {
            case HELL -> {
                world.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).set(false, null); 
                world.getGameRules().get(GameRules.DO_WEATHER_CYCLE).set(false, null); 
                world.setTimeOfDay(18000);
            } case PARADISE -> {
                world.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).set(false, null); 
                world.getGameRules().get(GameRules.DO_WEATHER_CYCLE).set(false, null); 
                world.setTimeOfDay(6000);
            } case WOODS -> {
                world.getGameRules().get(GameRules.DO_WEATHER_CYCLE).set(false, null); 
                world.setWeather(0, Integer.MAX_VALUE, true, false);
            } default -> {}
        }
    }
}
