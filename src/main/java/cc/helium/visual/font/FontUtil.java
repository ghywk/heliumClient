package cc.helium.visual.font;

import cc.helium.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.StringUtils;

import java.awt.*;

public class FontUtil implements Util {
    private static FontRenderer fontRenderer;

    public static void setupFontUtils() {
        fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    }

    public static int getStringWidth(String text) {
        return fontRenderer.getStringWidth(StringUtils.stripControlCodes(text));
    }

    public static int getFontHeight() {
        return fontRenderer.FONT_HEIGHT;
    }

    public static void drawString(String text, double x, double y, int color) {
        fontRenderer.drawString(text, (int)x, (int)y, color);
    }

    public static void TTFdrawString(String text, double x, double y, Color color) {
        FontHelper.sf_light36.drawStringWithShadow(text, (int)x, (int)y, color);
    }

    public static void drawStringWithShadow(String text, double x, double y, int color) {
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, (float) x, (float) y, color);
    }

    public static void drawStringSFL(String text, double x, double y, Color color) {
        FontHelper.sf_light36.drawStringWithShadow(text, (float) x, (float) y, color);
    }

    public static void drawStringSFL2(String text, double x, double y, Color color) {
        FontHelper.sf_light72.drawStringWithShadow(text, (float) x, (float) y, color);
    }

    public static void drawStringVerdana(String text, double x, double y, Color color) {
        FontHelper.verdana32.drawStringWithShadow(text, (float) x, (float) y, color);
    }

    public static void drawCenteredString(String text, double x, double y, int color) {
        drawString(text, x - fontRenderer.getStringWidth(text) / 2, y, color);
    }


    public static void drawCenteredString(String text, double x, double y, Color color) {
        TTFdrawString(text, x - FontHelper.sf_light36.getStringWidth(text) / 2, y, color);
    }

    public static void drawCenteredStringWithShadow(String text, double x, double y, int color) {
        drawStringWithShadow(text, x - fontRenderer.getStringWidth(text) / 2, y, color);
    }

    public static void drawTotalCenteredString(String text, double x, double y, int color) {
        drawString(text, x - fontRenderer.getStringWidth(text) / 2, y - fontRenderer.FONT_HEIGHT / 2, color);
    }

    public static void drawTotalCenteredStringWithShadowMC(String text, double x, double y, int color) {
        drawStringWithShadow(text, x - Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) / 2, y - (int) (Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT / 2F) - 1, color);
    }

    public static void drawTotalCenteredStringWithShadowSFL(String text, double x, double y, Color color) {
        drawStringSFL(text, x - FontHelper.sf_light36.getStringWidth(text) / 2, y - FontHelper.sf_light36.FONT_HEIGHT / 2F, color);
    }

    public static void drawTotalCenteredStringWithShadowSFL2(String text, double x, double y, Color color) {
        drawStringSFL2(text, x - FontHelper.sf_light36.getStringWidth(text) / 2, y - FontHelper.sf_light36.FONT_HEIGHT / 2F, color);
    }

    public static void drawTotalCenteredStringWithShadowVerdana(String text, double x, double y, Color color) {
        drawStringVerdana(text, x - FontHelper.verdana32.getStringWidth(text) / 2, y - FontHelper.verdana32.FONT_HEIGHT / 2F, color);
    }
}
