package mod.bluestaggo.modernerbeta.network;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.Optional;

public record BiomeProviderInfoPayload(
        boolean isModernBetaWorld,
        boolean hasBiomeProvider,
        Optional<Long> seed,
        Optional<Identifier> providerId,
        Optional<NbtCompound> settings
) implements CustomPayload {
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
