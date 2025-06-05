package mod.bluestaggo.modernerbeta.forgelike.network;

import mod.bluestaggo.modernerbeta.network.INetworkHelper;
import mod.bluestaggo.modernerbeta.util.ModernBetaPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.neoforged.neoforge.network.PacketDistributor;

public class NetworkHelperImpl implements INetworkHelper {
    @Override
    public void sendToServer(ModernBetaPayload payload) {
        PacketDistributor.sendToServer(payload);
    }

    @Override
    public void sendToPlayer(ServerPlayerEntity player, ModernBetaPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    @Override
    public void sendToPlayersTrackingChunk(ServerWorld world, ChunkPos pos, ModernBetaPayload payload) {
        PacketDistributor.sendToPlayersTrackingChunk(world, pos, payload);
    }

    @Override
    public void sendToAllPlayers(MinecraftServer server, ModernBetaPayload payload) {
        PacketDistributor.sendToAllPlayers(payload);
    }
}
