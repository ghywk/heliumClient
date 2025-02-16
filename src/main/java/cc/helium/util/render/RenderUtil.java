package cc.helium.util.render;

import cc.helium.util.Util;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 * @author Kev1nLeft
 */

public class RenderUtil implements Util {
    public static void rect(float x1, float y1, float x2, float y2, int fill) {
        GlStateManager.color(0, 0, 0);
        GL11.glColor4f(0, 0, 0, 0);

        float f = (fill >> 24 & 0xFF) / 255.0F;
        float f1 = (fill >> 16 & 0xFF) / 255.0F;
        float f2 = (fill >> 8 & 0xFF) / 255.0F;
        float f3 = (fill & 0xFF) / 255.0F;

        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);

        GL11.glPushMatrix();
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(7);
        GL11.glVertex2d(x2, y1);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x1, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void drawBorderedCorneredRect(final float x, final float y, final float x2, final float y2, final float lineWidth, final int lineColor, final int bgColor) {
        rect(x, y, x2, y2, bgColor);
        rect(x - 1.0f, y - 1.0f, x2 + 1.0f, y, lineColor);
        rect(x - 1.0f, y, x, y2, lineColor);
        rect(x - 1.0f, y2, x2 + 1.0f, y2 + 1.0f, lineColor);
        rect(x2, y, x2 + 1.0f, y2, lineColor);
    }

    public static void endClip() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static void startClip(float x1, float y1, float x2, float y2) {
        float temp;
        if (y1 > y2) {
            temp = y2;
            y2 = y1;
            y1 = temp;
        }

        GL11.glScissor((int) x1, (int) (Display.getHeight() - y2), (int) (x2 - x1), (int) (y2 - y1));
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
    }
}
