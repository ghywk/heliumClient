package net.minecraft.network;

import cc.helium.Client;
import cc.helium.module.impl.player.Disabler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IThreadListener;

public class PacketThreadUtil {
    public static int lastDimensionId = Integer.MIN_VALUE;

    public static <T extends INetHandler> void checkThreadAndEnqueue(final Packet<T> p_180031_0_, final T p_180031_1_, IThreadListener p_180031_2_) throws ThreadQuickExitException {
        if (!p_180031_2_.isCallingFromMinecraftThread()) {
            p_180031_2_.addScheduledTask(() -> {
                if (Disabler.post.getValue() && Disabler.mode.is("Grim") && Client.getInstance().moduleManager.getModule(Disabler.class).isEnable() && p_180031_1_ == Minecraft.getMinecraft().getNetHandler()){
                    Disabler.postPackets.add((Packet<INetHandlerPlayClient>) p_180031_0_);
                    return;
                }
                p_180031_0_.processPacket(p_180031_1_);
            });
            throw ThreadQuickExitException.INSTANCE;
        }
    }
}
