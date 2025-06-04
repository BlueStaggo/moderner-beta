package mod.bluestaggo.modernerbeta.util;

//? if >=1.20.2
import net.minecraft.network.packet.CustomPayload;

public interface ModernBetaPayload
    //? if >=1.20.2
    extends CustomPayload
{
    //? if <1.20.2 {
    /*void writeToPacketByteBuf(PacketByteBuf packetByteBuf);
    Identifier getId();

    default PacketByteBuf toPacketByteBuf() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        this.writeToPacketByteBuf(buf);
        return buf;
    }
    *///?}
}
