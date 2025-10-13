package mod.bluestaggo.modernerbeta.mixin;

import mod.bluestaggo.modernerbeta.api.world.spawn.SpawnLocator;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.core.BlockPos;
//? if >=1.21.9 {
/*import net.minecraft.server.level.PlayerSpawnFinder;
*///?} else {
import net.minecraft.server.level.PlayerRespawnLogic;
//?}
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
    //? if >=1.21.9 {
    /*PlayerSpawnFinder.class
    *///?} else {
    PlayerRespawnLogic.class
    //?}
)
public abstract class MixinSpawnLocating {
    /*
     * Override vanilla behavior of moving player to highest solid block, 
     * even after finding initial spawn point.
     */
    @Inject(method = 
            //? if >=1.21.9 {
            /*"findOverworldSpawn"
            *///?} else {
            "getOverworldRespawnPos"
            //?}
            , at = @At("HEAD"), cancellable = true)
    private static void injectFindOverworldSpawnHeight(ServerLevel world, int x, int z, CallbackInfoReturnable<BlockPos> info) {
        ChunkGenerator chunkGenerator = world.getChunkSource().getGenerator();
        
        if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator && 
            modernBetaChunkGenerator.getChunkProvider().getSpawnLocator() != SpawnLocator.DEFAULT
        ) {
            int spawnY = world.getLevelData()
                //? if >=1.21.9 {
                /*.getRespawnData().globalPos().pos().getY();
                *///?} else if >=1.20.5 {
                .getSpawnPos().getY();
                //?} else {
                /*.getYSpawn();
                *///?}
            
            info.setReturnValue(new BlockPos(x, spawnY, z));
        }
    }
}
