package mod.bluestaggo.modernerbeta.fabric.network;

import mod.bluestaggo.modernerbeta.network.INetworkHelper;
import mod.bluestaggo.modernerbeta.util.ModernBetaPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;

public class NetworkHelperImpl implements INetworkHelper {
    @Override
    public void sendToServer(ModernBetaPayload payload) {
        //? if >=1.20.2 {
        ClientPlayNetworking.send(payload);
        //?} else {
        /*ClientPlayNetworking.send(payload.getId(), payload.toPacketByteBuf());
        *///?}
    }

    @Override
    public void sendToPlayer(ServerPlayerEntity player, ModernBetaPayload payload) {
        //? if >=1.20.2 {
        ServerPlayNetworking.send(player, payload);
        //?} else {
        /*ServerPlayNetworking.send(player, payload.getId(), payload.toPacketByteBuf());
        *///?}
    }

    @Override
    public void sendToPlayersTrackingChunk(ServerWorld world, ChunkPos pos, ModernBetaPayload payload) {
        for (ServerPlayerEntity player : PlayerLookup.tracking(world, pos)) {
            this.sendToPlayer(player, payload);
        }
    }

    @Override
    public void sendToAllPlayers(MinecraftServer server, ModernBetaPayload payload) {
        for (ServerPlayerEntity player : PlayerLookup.all(server)) {
            this.sendToPlayer(player, payload);
        }
    }
}
