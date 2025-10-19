package mod.bluestaggo.modernerbeta.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProviderFinite;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bluestaggo.modernerbeta.world.chunk.provider.ChunkProviderIndev;
import mod.bluestaggo.modernerbeta.world.chunk.provider.indev.IndevTheme;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.storage.ServerLevelData;
import org.slf4j.event.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Inject(method = "setInitialSpawn", at = @At("RETURN"))
    private static void injectSetInitialSpawn(ServerLevel level, ServerLevelData levelData, boolean bonusChest, boolean debugWorld, /*? >=1.21.9 {*/ /*net.minecraft.server.level.progress.LevelLoadListener arg, *//*?}*/ CallbackInfo ci) {
        ChunkGenerator chunkGenerator = level.getChunkSource().getGenerator();

        // Set old spawn angle (doesn't seem to work?)
        if (chunkGenerator instanceof ModernBetaChunkGenerator) {
            //? if >=1.21.9 {
            /*net.minecraft.world.level.storage.LevelData.RespawnData respawnData = levelData.getRespawnData();
            levelData.setSpawn(
                    new net.minecraft.world.level.storage.LevelData.RespawnData(
                            respawnData.globalPos(),
                            -90.0f,
                            respawnData.yaw()
                    )
            );
            *///?} else {
            levelData.setSpawn(
                //? if >=1.20.5 {
                levelData.getSpawnPos(),
                //?} else {
                /*new BlockPos(
                    levelData.getXSpawn(),
                    levelData.getYSpawn(),
                    levelData.getZSpawn()
                ),
                *///?}
                -90.0f
            );
            //?}
        }
    }

    @WrapOperation(
        method = "setInitialSpawn",
        at = @At(
            value = "INVOKE", 
            target = "Lnet/minecraft/server/level/" +
                //? if >=1.21.9 {
                /*"PlayerSpawnFinder"
                *///? } else {
                "PlayerRespawnLogic"
                //? }
                + ";getSpawnPosInChunk(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/ChunkPos;)Lnet/minecraft/core/BlockPos;"
        )
    )
    private static BlockPos redirectPlayerSpawnFinder(ServerLevel level, ChunkPos chunkPos, Operation<BlockPos> original) {
        ChunkGenerator chunkGenerator = level.getChunkSource().getGenerator();
        
        if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
            ChunkProvider chunkProvider = modernBetaChunkGenerator.getChunkProvider();
            
            level.getGameRules().getRule(GameRules.RULE_SPAWN_RADIUS).set(0, level.getServer()); // Ensure a centered spawn
            BlockPos spawnPos = chunkProvider.getSpawnLocator().locateSpawn(level).orElseGet(() -> original.call(level, chunkPos));
            
            if (spawnPos != null && ModernerBeta.DEV_ENV) {
                int x = spawnPos.getX();
                int y = spawnPos.getY();
                int z = spawnPos.getZ();
                
                ModernerBeta.log(Level.INFO, String.format("Spawning at %d/%d/%d", x, y, z));
            }
            
            if (spawnPos != null && chunkProvider instanceof ChunkProviderIndev chunkProviderIndev) {
                // Generate Indev house
                chunkProviderIndev.generateIndevHouse(level, spawnPos);
                
                // Set Indev world properties.
                setIndevProperties(level, chunkProviderIndev.getLevelTheme());
            }
            
            if (chunkProvider instanceof ChunkProviderFinite) {
                ChunkProviderFinite.resetPhase();
            }

            return spawnPos;
        }

        return original.call(level, chunkPos);
    }
    
    @Unique
    private static void setIndevProperties(ServerLevel level, IndevTheme theme) {
        switch(theme) {
            case HELL -> {
                level.getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(false, null); 
                level.getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE).set(false, null); 
                level.setDayTime(18000);
            } case PARADISE -> {
                level.getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(false, null); 
                level.getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE).set(false, null); 
                level.setDayTime(6000);
            } case WOODS -> {
                level.getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE).set(false, null); 
                level.setWeatherParameters(0, Integer.MAX_VALUE, true, false);
            } default -> {}
        }
    }
}
