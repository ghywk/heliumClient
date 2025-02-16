package cc.helium.module.impl.render;

import cc.helium.Client;
import cc.helium.config.impl.ClickGuiConfig;
import cc.helium.module.Category;
import cc.helium.module.Module;
import cc.helium.value.impl.BoolValue;
import cc.helium.value.impl.NumberValue;
import cc.helium.visual.clickgui.ClickGui;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class ClickGUI extends Module {
    public BoolValue blur = new BoolValue("Blur", true);
    public NumberValue red = new NumberValue("Red", 163, 0, 255, 1);
    public NumberValue blue = new NumberValue("Blue", 223, 0, 255, 1);
    public NumberValue green = new NumberValue("Green", 255, 0, 255, 1);
    public NumberValue alpha = new NumberValue("Alpha", 220, 0, 255, 1);

    public ClickGui clickgui;

    public ClickGUI() {
        super("ClickGui", Keyboard.KEY_RSHIFT, Category.Render);
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer == null) return;

        if(this.clickgui == null) {
            this.clickgui = new ClickGui();
        }
        Client.getInstance().configManager.getConfig(ClickGuiConfig.class).loadConfig();
        mc.displayGuiScreen(this.clickgui);
        setEnable(false);
    }
}
