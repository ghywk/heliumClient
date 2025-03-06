package cc.helium.util;

import cc.helium.visual.clickgui.component.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/**
 * @author Kev1nLeft
 */

public interface Util {
    Minecraft mc = Minecraft.getMinecraft();
    ScaledResolution sr = new ScaledResolution(mc);
}
