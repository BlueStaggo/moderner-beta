package mod.bluestaggo.modernerbeta.network;

import mod.bluestaggo.modernerbeta.util.ModernBetaPayload;
import net.minecraft.nbt.CompoundTag;
//? if >=1.20.2 {
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//?} else {
/*import net.minecraft.network.FriendlyByteBuf;
*///?}
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record BiomeProviderInfoPayload(
        boolean isModernBetaWorld,
        boolean hasBiomeProvider,
        Optional<Long> seed,
        Optional<ResourceLocation> providerId,
        Optional<CompoundTag> settings
) implements ModernBetaPayload
//? if >=1.20.2 {
{
    public static final CustomPacketPayload.Type<BiomeProviderInfoPayload> ID = new CustomPacketPayload.Type<>(ModernBetaNetworkConstants.BIOME_PROVIDER_INFO_PACKET_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, BiomeProviderInfoPayload> CODEC = StreamCodec.composite(
        ByteBufCodecs.BOOL,
        BiomeProviderInfoPayload::isModernBetaWorld,
        ByteBufCodecs.BOOL,
        BiomeProviderInfoPayload::hasBiomeProvider,
        //? if >=1.21.2 {
        ByteBufCodecs.LONG
        //?} else {
        /*ByteBufCodecs.VAR_LONG
        *///?}
            .apply(ByteBufCodecs::optional), BiomeProviderInfoPayload::seed,
        ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs::optional), BiomeProviderInfoPayload::providerId,
        ByteBufCodecs.COMPOUND_TAG.apply(ByteBufCodecs::optional), BiomeProviderInfoPayload::settings,

        BiomeProviderInfoPayload::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}

//?} else {
/*{
    public static final ResourceLocation ID = ModernBetaNetworkConstants.BIOME_PROVIDER_INFO_PACKET_ID;

    public static BiomeProviderInfoPayload fromFriendlyByteBuf(FriendlyByteBuf friendlyByteBuf) {
        return new BiomeProviderInfoPayload(
            friendlyByteBuf.readBoolean(),
            friendlyByteBuf.readBoolean(),
            friendlyByteBuf.readOptional(FriendlyByteBuf::readLong),
            friendlyByteBuf.readOptional(FriendlyByteBuf::readResourceLocation),
            friendlyByteBuf.readOptional(FriendlyByteBuf::readNbt)
        );
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBoolean(this.isModernBetaWorld());
        friendlyByteBuf.writeBoolean(this.hasBiomeProvider());
        friendlyByteBuf.writeOptional(this.seed(), FriendlyByteBuf::writeLong);
        friendlyByteBuf.writeOptional(this.providerId(), FriendlyByteBuf::writeResourceLocation);
        friendlyByteBuf.writeOptional(this.settings(), FriendlyByteBuf::writeNbt);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
*///?}
