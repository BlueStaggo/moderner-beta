package mod.bluestaggo.modernerbeta.util;

//? if >=1.20.2 {
import net.minecraft.network.packet.CustomPayload;
//?} else {
/*import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
*///?}

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
