package mod.bluestaggo.modernerbeta.util;

//? if >=1.20.2 {
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//?} else {
/*import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
*///?}

public interface ModernBetaPayload
    //? if >=1.20.2 {
    extends CustomPacketPayload
    //?} else {
    /*extends Packet<PacketListener>
    *///?}
{
    //? if <1.20.2 {
    /*void write(FriendlyByteBuf FriendlyByteBuf);
    ResourceLocation getId();

    @Override
    default void handle(PacketListener listener) {
    }

    default FriendlyByteBuf toFriendlyByteBuf() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        this.write(buf);
        return buf;
    }
    *///?}
}
