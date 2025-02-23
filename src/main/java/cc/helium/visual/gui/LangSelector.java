package cc.helium.visual.gui;

import cc.helium.Client;
import cc.helium.module.impl.render.HUDModule;
import cc.helium.util.Util;
import cc.helium.util.lang.Languages;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

/**
 * @author Kev1nLeft
 */

public class LangSelector extends GuiScreen implements Util {
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(1, 5, 5, 98, 20, "English"));
        this.buttonList.add(new GuiButton(2, 5, 30, 98, 20, "Chinese"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground(0);
        this.drawString(Util.mc.fontRendererObj, "Language selector", 5, sr.getScaledHeight() - Util.mc.fontRendererObj.FONT_HEIGHT - 5, HUDModule.clientColor.getRGB());
        this.drawString(Util.mc.fontRendererObj, "Now: " + Client.getInstance().lang.name(), 5, sr.getScaledHeight() - Util.mc.fontRendererObj.FONT_HEIGHT * 2 - 10, HUDModule.clientColor.getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        Client.getInstance().configManager.saveConfigs();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1: {
                Client.getInstance().lang = Languages.ENGLISH;
                break;
            }
            case 2: {
                Client.getInstance().lang = Languages.CHINESE;
                break;
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
