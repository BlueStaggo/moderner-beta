package mod.bluestaggo.modernerbeta.network;

import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;

public interface INetworkHelper {
    void sendToServer(CustomPayload payload);
    void sendToPlayer(ServerPlayerEntity player, CustomPayload payload);
    void sendToPlayersTrackingChunk(ServerWorld world, ChunkPos pos, CustomPayload payload);
    void sendToAllPlayers(MinecraftServer server, CustomPayload payload);
}
