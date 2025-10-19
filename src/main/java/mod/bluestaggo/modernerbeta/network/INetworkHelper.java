package mod.bluestaggo.modernerbeta.network;

import mod.bluestaggo.modernerbeta.util.ModernBetaPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;

public interface INetworkHelper {
    void sendToServer(ModernBetaPayload payload);
    void sendToPlayer(ServerPlayer player, ModernBetaPayload payload);
    void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos pos, ModernBetaPayload payload);
    void sendToAllPlayers(MinecraftServer server, ModernBetaPayload payload);
}
