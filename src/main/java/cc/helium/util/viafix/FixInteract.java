package cc.helium.util.viafix;

import cc.helium.event.api.annotations.Priority;
import cc.helium.event.api.annotations.TargetEvent;
import cc.helium.event.impl.packet.PacketSendEvent;
import cc.helium.util.Util;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;

/**
 * @author Kev1nLeft
 */

public class FixInteract implements Util {
    @TargetEvent
    @Priority(value = 0)
    public void onPacketSend(PacketSendEvent event) {
        if (!event.isCancelled() && ViaLoadingBase.getInstance()
                .getTargetVersion().newerThan(ProtocolVersion.v1_8)) {

            if (event.getPacket() instanceof C02PacketUseEntity use) {
                event.setCancelled(event.isCancelled() || !use.getAction().equals(C02PacketUseEntity.Action.ATTACK));
            }
        }
    }
}
