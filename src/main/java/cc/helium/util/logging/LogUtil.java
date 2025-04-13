package cc.helium.util.logging;

import cc.helium.Client;
import cc.helium.util.Util;
import net.minecraft.util.ChatComponentText;

/**
 * @author Kev1nLeft
 */

public class LogUtil implements Util {
    public static void log_error(String m) {
        Client.logger.error(m);
    }

    public static void log_warn(String m) {
        Client.logger.warn(m);
    }

    public static void log_info(String m) {
        Client.logger.info(m);
    }

    public static void log_chat(String m) {
        mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(m));
    }
}
