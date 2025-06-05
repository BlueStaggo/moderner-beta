package mod.bluestaggo.modernerbeta.util;

//? if >=1.20.2 {
import net.minecraft.network.packet.CustomPayload;
//?} else {
/*import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Identifier;
*///?}

public interface ModernBetaPayload
    //? if >=1.20.2 {
    extends CustomPayload
    //?} else {
    /*extends Packet<PacketListener>
    *///?}
{
    //? if <1.20.2 {
    /*void write(PacketByteBuf packetByteBuf);
    Identifier getId();

    @Override
    default void apply(PacketListener listener) {
    }

    default PacketByteBuf toPacketByteBuf() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        this.write(buf);
        return buf;
    }
    *///?}
}
