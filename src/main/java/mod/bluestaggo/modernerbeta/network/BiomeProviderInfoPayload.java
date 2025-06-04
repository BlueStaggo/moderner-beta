package mod.bluestaggo.modernerbeta.network;

import mod.bluestaggo.modernerbeta.util.ModernBetaPayload;
import net.minecraft.nbt.NbtCompound;
//? if >=1.20.2 {
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
//?} else {
//import net.minecraft.network.PacketByteBuf;
//?}
import net.minecraft.util.Identifier;

import java.util.Optional;

public record BiomeProviderInfoPayload(
        boolean isModernBetaWorld,
        boolean hasBiomeProvider,
        Optional<Long> seed,
        Optional<Identifier> providerId,
        Optional<NbtCompound> settings
) implements ModernBetaPayload
//? if >=1.20.2 {
{
    public static final CustomPayload.Id<BiomeProviderInfoPayload> ID = new CustomPayload.Id<>(ModernBetaNetworkConstants.BIOME_PROVIDER_INFO_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, BiomeProviderInfoPayload> CODEC = PacketCodec.tuple(
        //? if >=1.21.4 {
        PacketCodecs.BOOLEAN,
        //?} else {
        /*PacketCodecs.BOOL,
        *///?}
        BiomeProviderInfoPayload::isModernBetaWorld,
        //? if >=1.21.4 {
        PacketCodecs.BOOLEAN,
        //?} else {
        /*PacketCodecs.BOOL,
        *///?}
        BiomeProviderInfoPayload::hasBiomeProvider,
        //? if >=1.21.2 {
        PacketCodecs.LONG
        //?} else {
        /*PacketCodecs.VAR_LONG
        *///?}
            .collect(PacketCodecs::optional), BiomeProviderInfoPayload::seed,
        Identifier.PACKET_CODEC.collect(PacketCodecs::optional), BiomeProviderInfoPayload::providerId,
        PacketCodecs.NBT_COMPOUND.collect(PacketCodecs::optional), BiomeProviderInfoPayload::settings,

        BiomeProviderInfoPayload::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

//?} else {
/*{
    public static final Identifier ID = ModernBetaNetworkConstants.BIOME_PROVIDER_INFO_PACKET_ID;

    public static BiomeProviderInfoPayload fromPacketByteBuf(PacketByteBuf packetByteBuf) {
        return new BiomeProviderInfoPayload(
            packetByteBuf.readBoolean(),
            packetByteBuf.readBoolean(),
            packetByteBuf.readOptional(PacketByteBuf::readLong),
            packetByteBuf.readOptional(PacketByteBuf::readIdentifier),
            packetByteBuf.readOptional(PacketByteBuf::readNbt)
        );
    }

    @Override
    public void writeToPacketByteBuf(PacketByteBuf packetByteBuf) {
        packetByteBuf.writeBoolean(this.isModernBetaWorld());
        packetByteBuf.writeBoolean(this.hasBiomeProvider());
        packetByteBuf.writeOptional(this.seed(), PacketByteBuf::writeLong);
        packetByteBuf.writeOptional(this.providerId(), PacketByteBuf::writeIdentifier);
        packetByteBuf.writeOptional(this.settings(), PacketByteBuf::writeNbt);
    }

    @Override
    public Identifier getId() {
        return ID;
    }
}
*///?}