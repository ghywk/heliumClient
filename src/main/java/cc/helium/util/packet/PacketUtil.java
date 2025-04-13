package cc.helium.util.packet;

import cc.helium.util.Util;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.exception.CancelException;
import net.minecraft.network.Packet;

/**
 * @author Kev1nLeft
 */

public class PacketUtil implements Util {
    public static void send(Packet<?> packet) {
        if (mc.thePlayer != null) {
            mc.getNetHandler().addToSendQueue(packet);
        }
    }

    public static void sendPacketNoEvent(Packet<?> packet) {
        mc.getNetHandler().addToSendQueueUnregistered(packet);
    }

    public static void sendToServer(PacketWrapper packet, Class<? extends Protocol> packetProtocol, boolean skipCurrentPipeline, boolean currentThread) {
        try {
            if (currentThread) {
                packet.sendToServer(packetProtocol, skipCurrentPipeline);
            } else {
                packet.scheduleSendToServer(packetProtocol, skipCurrentPipeline);
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }
}
