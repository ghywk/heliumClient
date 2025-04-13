package cc.helium.event.impl.packet;

import cc.helium.event.api.cancellable.CancellableEvent;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;

/**
 * @author Kev1nLeft
 */

public class PacketReceiveEvent extends CancellableEvent {
    private Packet<?> packet;
    private NetworkManager networkManager;

    public PacketReceiveEvent(NetworkManager networkManager, Packet<?> packet) {
        this.networkManager = networkManager;
        this.packet = packet;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public void setNetworkManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }
}
