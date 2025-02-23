package cc.helium.module.impl.render;

import cc.helium.module.Category;
import cc.helium.module.Module;
import cc.helium.value.impl.BoolValue;
import net.minecraft.util.StringUtils;

/**
 * @author Kev1nLeft
 */

public class TextEditor extends Module {
    public static final BoolValue hideIP = new BoolValue("Hide scoreboard IP", true);
    public static final BoolValue hideServerId = new BoolValue("Hide server ID", true);
    public static final BoolValue hideUsername = new BoolValue("Hide username", true);
    public static boolean enabled;

    public TextEditor() {
        super("TextEditor", -1, Category.Render);
    }

    public static String filter(String text) {
        if (enabled) {
            if (hideUsername.getValue() && mc.getSession() != null) {
                String name = mc.getSession().getUsername();
                if (name != null && !name.trim().isEmpty() && !name.equals("Player") && text.contains(name)) {
                    text = text.replace(name, "Hidden");
                    String text2 = StringUtils.stripControlCodes(text);
                    if (text2.contains("You has ")) {
                        text = text.replace(" has", " have");
                    }
                    if (text2.contains("You was ")) {
                        text = text.replace("was ", "were ");
                    }
                    if (text2.contains("You's ")) {
                        text = text.replace("'s ", "'re ");
                    }
                }
            }
            if (mc.theWorld != null) {
                if (hideIP.getValue() && text.contains("花雨庭")) {
                    text = StringUtils.stripControlCodes(text)
                            .replaceAll("[^A-Za-z0-9 .]", "")
                            .replace("花雨庭", "§eHelium");
                }
                if (hideIP.getValue() && text.startsWith("§ewww.")) {
                    text = StringUtils.stripControlCodes(text)
                            .replaceAll("[^A-Za-z0-9 .]", "")
                            .replace("www.hypixel.net", "§eHelium");
                }
                if (hideServerId.getValue() && text.startsWith("§7") && text.contains("/") && text.contains("  §8")) {
                    text = text.replace("§8", "§8§k");
                }
            }
        }
        return text;
    }

    @Override
    public void onEnable() {
        enabled = true;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        enabled = false;
        super.onDisable();
    }
}
