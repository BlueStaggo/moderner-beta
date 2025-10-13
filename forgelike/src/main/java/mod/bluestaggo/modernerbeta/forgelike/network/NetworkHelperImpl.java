package mod.bluestaggo.modernerbeta.forgelike.network;

import mod.bluestaggo.modernerbeta.network.INetworkHelper;
import mod.bluestaggo.modernerbeta.util.ModernBetaPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
//? if neoforge {
import net.neoforged.neoforge.network.PacketDistributor;
//?} else {
/*import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
*///?}

public class NetworkHelperImpl implements INetworkHelper {
    //? if neoforge {
    @Override
    public void sendToServer(ModernBetaPayload payload) {
        //? if >=1.21.7 {
        /*.neoforged.neoforge.client.network.ClientPacketDistributor.sendToServer(payload);
     *///?} else {
        PacketDistributor.sendToServer(payload);
        //?}
    }

    @Override
    public void sendToPlayer(ServerPlayer player, ModernBetaPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    @Override
    public void sendToPlayersTrackingChunk(ServerLevel world, ChunkPos pos, ModernBetaPayload payload) {
        PacketDistributor.sendToPlayersTrackingChunk(world, pos, payload);
    }

    @Override
    public void sendToAllPlayers(MinecraftServer server, ModernBetaPayload payload) {
        PacketDistributor.sendToAllPlayers(payload);
    }
    //?} else {
    /*public final SimpleChannel channel;

    public NetworkHelperImpl() {
        this.channel = NetworkRegistry.newSimpleChannel(ModernerBeta.createId(ModernerBeta.MOD_ID), () -> "", string -> true, string -> true);
    }

    @Override
    public void sendToServer(ModernBetaPayload payload) {
        this.channel.sendToServer(payload);
    }

    @Override
    public void sendToPlayer(ServerPlayerEntity player, ModernBetaPayload payload) {
        this.channel.sendTo(payload, player.networkHandler.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    @Override
    public void sendToPlayersTrackingChunk(ServerWorld world, ChunkPos pos, ModernBetaPayload payload) {
        this.channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunk(pos.x, pos.z)), payload);
    }

    @Override
    public void sendToAllPlayers(MinecraftServer server, ModernBetaPayload payload) {
        this.channel.send(PacketDistributor.ALL.noArg(), payload);
    }
    *///?}
}
