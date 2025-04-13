package cc.helium.util.inv;

import cc.helium.util.Util;
import net.minecraft.client.Minecraft;

/**
 * @author Kev1nLeft
 */

public class InvUtil implements Util {
    public static void swap(int slot, int switchSlot) {
        mc.playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, slot, switchSlot, 2, Minecraft.getMinecraft().thePlayer);
    }
}
