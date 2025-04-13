package cc.helium.module.impl.player;

import cc.helium.Client;
import cc.helium.event.api.annotations.SubscribeEvent;
import cc.helium.event.api.type.Timing;
import cc.helium.event.impl.packet.PacketReceiveEvent;
import cc.helium.event.impl.update.MotionEvent;
import cc.helium.event.impl.update.TickEvent;
import cc.helium.module.Category;
import cc.helium.module.Module;
import cc.helium.value.impl.BoolValue;
import cc.helium.value.impl.ModeValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Kev1nLeft
 */

public class Disabler extends Module {
    public static final ModeValue mode = new ModeValue("Mode", "Grim", "Grim");
    public static final BoolValue post = new BoolValue("Post", true);
    public static final LinkedBlockingQueue<Packet<INetHandlerPlayClient>> postPackets = new LinkedBlockingQueue<>();

    public Disabler() {
        super("Disabler", -1, Category.Player);
    }

    public static void releasePost() {
        if (mode.is("Grim") && post.getValue() && Client.getInstance().moduleManager.getModule(Disabler.class).isEnable() && mc.getNetHandler() != null) {
            while (!postPackets.isEmpty()) {
                PacketReceiveEvent packetEvent = new PacketReceiveEvent(mc.getNetHandler().getNetworkManager(), postPackets.poll());
                Client.getInstance().eventManager.call(packetEvent);
                if (packetEvent.isCancelled()) {
                    continue;  // Skip cancelled events and continue processing the next packet
                }
                Packet<INetHandlerPlayClient> packet = (Packet<INetHandlerPlayClient>) packetEvent.getPacket();
                if (packet == null) {
                    continue;
                }
                try {
                    packet.processPacket(mc.getNetHandler());
                } catch (Exception e) {
                    // Log exception or debug information
                    e.printStackTrace();
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (event.timing().equals(Timing.POST)) {
            releasePost();
        }
    }

    @SubscribeEvent
    public void onMotion(MotionEvent event) {
        if (event.isPost()) {
            releasePost();
        }
    }
}
