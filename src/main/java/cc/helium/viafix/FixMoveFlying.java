package cc.helium.viafix;

import cc.helium.event.api.annotations.TargetEvent;
import cc.helium.event.impl.packet.PacketSendEvent;
import cc.helium.util.Util;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

/**
 * @author Kev1nLeft
 */

public class FixMoveFlying implements Util {
    private boolean lastGround;

    @TargetEvent
    public void onPacketSend(PacketSendEvent event) {
        if (ViaLoadingBase.getInstance().getTargetVersion().newerThan(ProtocolVersion.v1_8)) {
            final Packet<?> packet = event.getPacket();

            if (packet instanceof C03PacketPlayer wrapper) {

                if (!wrapper.isMoving() && !wrapper.getRotating() && wrapper.isOnGround() == this.lastGround) {
                    event.setCancelled(true);
                }

                this.lastGround = wrapper.isOnGround();
            }
        }
    }
}
