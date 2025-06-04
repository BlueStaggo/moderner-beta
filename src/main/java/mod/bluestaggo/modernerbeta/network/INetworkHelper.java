package mod.bluestaggo.modernerbeta.network;

import mod.bluestaggo.modernerbeta.util.ModernBetaPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;

public interface INetworkHelper {
    void sendToServer(ModernBetaPayload payload);
    void sendToPlayer(ServerPlayerEntity player, ModernBetaPayload payload);
    void sendToPlayersTrackingChunk(ServerWorld world, ChunkPos pos, ModernBetaPayload payload);
    void sendToAllPlayers(MinecraftServer server, ModernBetaPayload payload);
}
