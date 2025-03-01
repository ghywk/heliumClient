package cc.helium.viafix;

import cc.helium.event.api.annotations.TargetEvent;
import cc.helium.event.impl.packet.PacketSendEvent;
import cc.helium.util.Util;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

/**
 * @author Kev1nLeft
 */

public class FixPlacement implements Util {
    @TargetEvent
    public void onPacketSend(PacketSendEvent event) {
        if (ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_11)) {
            final Packet<?> packet = event.getPacket();

            if (packet instanceof C08PacketPlayerBlockPlacement wrapper) {
                wrapper.facingX /= 16.0F;
                wrapper.facingY /= 16.0F;
                wrapper.facingZ /= 16.0F;
                event.setPacket(wrapper);
            }
        }
    }
}
