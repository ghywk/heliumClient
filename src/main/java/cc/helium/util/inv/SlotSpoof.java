package cc.helium.util.inv;

import cc.helium.util.Util;
import net.minecraft.item.ItemStack;

public class SlotSpoof implements Util {
    private static int spoofedSlot;
    private static boolean spoofing;

    public static void startSpoofing(int slot) {
        spoofing = true;
        spoofedSlot = slot;
    }

    public static void stopSpoofing() {
        spoofing = false;
    }

    public static int getSpoofedSlot() {
        return spoofing ? spoofedSlot : mc.thePlayer.inventory.currentItem;
    }

    public static ItemStack getSpoofedStack() {
        return spoofing ? mc.thePlayer.inventory.getStackInSlot(spoofedSlot) : mc.thePlayer.inventory.getCurrentItem();
    }

    public static boolean isSpoofing() {
        return spoofing;
    }
}
