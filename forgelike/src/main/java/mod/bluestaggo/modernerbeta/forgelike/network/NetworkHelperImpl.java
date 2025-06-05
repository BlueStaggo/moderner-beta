package mod.bluestaggo.modernerbeta.forgelike.network;

import mod.bluestaggo.modernerbeta.network.INetworkHelper;
import mod.bluestaggo.modernerbeta.util.ModernBetaPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
//? if neoforge {
import net.neoforged.neoforge.network.PacketDistributor;
//?} else {
/*import net.minecraftforge.network.PacketDistributor;
*///?}

public class NetworkHelperImpl implements INetworkHelper {
    @Override
    public void sendToServer(ModernBetaPayload payload) {
        //? if neoforge {
        PacketDistributor.sendToServer(payload);
        //?} else {
        /*PacketDistributor.SERVER.noArg().send(payload);
        *///?}
    }

    @Override
    public void sendToPlayer(ServerPlayerEntity player, ModernBetaPayload payload) {
        //? if neoforge {
        PacketDistributor.sendToPlayer(player, payload);
        //?} else {
        /*PacketDistributor.PLAYER.with(() -> player).send(payload);
        *///?}
    }

    @Override
    public void sendToPlayersTrackingChunk(ServerWorld world, ChunkPos pos, ModernBetaPayload payload) {
        //? if neoforge {
        PacketDistributor.sendToPlayersTrackingChunk(world, pos, payload);
        //?} else {
        /*PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunk(pos.x, pos.z)).send(payload);
        *///?}
    }

    @Override
    public void sendToAllPlayers(MinecraftServer server, ModernBetaPayload payload) {
        //? if neoforge {
        PacketDistributor.sendToAllPlayers(payload);
        //?} else {
        /*PacketDistributor.ALL.noArg().send(payload);
        *///?}
    }
}
