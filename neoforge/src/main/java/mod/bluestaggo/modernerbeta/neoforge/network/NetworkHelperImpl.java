package mod.bluestaggo.modernerbeta.neoforge.network;

import mod.bluestaggo.modernerbeta.network.INetworkHelper;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.neoforged.neoforge.network.PacketDistributor;

public class NetworkHelperImpl implements INetworkHelper {
    @Override
    public void sendToServer(CustomPayload payload) {
        PacketDistributor.sendToServer(payload);
    }

    @Override
    public void sendToPlayer(ServerPlayerEntity player, CustomPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    @Override
    public void sendToPlayersTrackingChunk(ServerWorld world, ChunkPos pos, CustomPayload payload) {
        PacketDistributor.sendToPlayersTrackingChunk(world, pos, payload);
    }

    @Override
    public void sendToAllPlayers(MinecraftServer server, CustomPayload payload) {
        PacketDistributor.sendToAllPlayers(payload);
    }
}
