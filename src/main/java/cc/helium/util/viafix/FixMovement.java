package cc.helium.util.viafix;

import cc.helium.event.api.annotations.SubscribeEvent;
import cc.helium.event.impl.update.UpdateEvent;
import cc.helium.util.Util;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import net.minecraft.util.AxisAlignedBB;

/**
 * @author Kev1nLeft
 */

public class FixMovement implements Util {
    @SubscribeEvent
    public void onUpdate(UpdateEvent ignored) {
        if (ViaLoadingBase.getInstance().getTargetVersion().newerThan(ProtocolVersion.v1_8)) {
            mc.thePlayer.setEntityBoundingBox(new AxisAlignedBB(mc.thePlayer.posX - 0.3, mc.thePlayer.posY,
                    mc.thePlayer.posZ - 0.3, mc.thePlayer.posX + 0.3, mc.thePlayer.posY + 1.8,
                    mc.thePlayer.posZ + 0.3));
        }
    }
}
