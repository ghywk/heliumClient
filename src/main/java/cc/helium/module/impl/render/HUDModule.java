package cc.helium.module.impl.render;

import cc.helium.Client;
import cc.helium.event.api.annotations.SubscribeEvent;
import cc.helium.event.impl.render.Render2DEvent;
import cc.helium.module.Category;
import cc.helium.module.Module;
import cc.helium.util.animation.Animation;
import cc.helium.util.animation.Direction;
import cc.helium.util.cal.Mth;
import cc.helium.value.impl.BoolValue;
import cc.helium.value.impl.NumberValue;
import cc.helium.visual.font.FontManager;
import cc.helium.visual.hud.notifications.Notification;
import cc.helium.visual.hud.notifications.NotificationManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @author Kev1nLeft
 */

public class HUDModule extends Module {
    private static final NumberValue red = new NumberValue("Client red", 163, 0, 255, 1);
    private static final NumberValue blue = new NumberValue("Client blue", 223, 0, 255, 1);
    private static final NumberValue green = new NumberValue("Client green", 255, 0, 255, 1);
    private static final NumberValue alpha = new NumberValue("Client alpha", 220, 0, 255, 1);
    public static BoolValue fontValue = new BoolValue("Custom font", true);
    public static BoolValue notificationsValue = new BoolValue("Notifications", true);
    private final NumberValue time = new NumberValue("Notifications time", 2, 1, 10, 0.5, () -> notificationsValue.getValue());
    public static BoolValue watermarkValue = new BoolValue("WaterMark", true);
    private final BoolValue drawTimeVal = new BoolValue("WaterMark time", true, () -> watermarkValue.getValue());
    private final BoolValue rainBow = new BoolValue("WaterMark rainbow", true, () -> watermarkValue.getValue());
    public static BoolValue arrayListValue = new BoolValue("Array list", true);
    private final NumberValue rainBowSpeed = new NumberValue("Rainbow speed", 15, 1, 25, 1, () -> watermarkValue.getValue() || arrayListValue.getValue());
    private final BoolValue arrayListLeft = new BoolValue("Array list left", false, () -> arrayListValue.getValue());

    private float hue = 0.0F;
    public static Color clientColor = new Color(red.getValue().intValue(), green.getValue().intValue(), blue.getValue().intValue(), alpha.getValue().intValue());
    public HUDModule() {
        super("HUD", -1, Category.Render);
    }

    @SubscribeEvent
    public void onRender2D(Render2DEvent ignored) {
        if (mc.thePlayer == null) return;
        this.hue += rainBowSpeed.getValue().floatValue() / 5.0F;
        clientColor = new Color(red.getValue().intValue(), green.getValue().intValue(), blue.getValue().intValue(), alpha.getValue().intValue());

        if (notificationsValue.getValue()) {
            renderNotifications();
        }

        if (watermarkValue.getValue()) {
            drawWaterMark();
        }

        if (arrayListValue.getValue()) {
            drawArrayList(hue, clientColor.getRGB());
        }
    }

    public static int getColor(int red, int green, int blue, int alpha) {
        int color = MathHelper.clamp_int(alpha, 0, 255) << 24;
        color |= MathHelper.clamp_int(red, 0, 255) << 16;
        color |= MathHelper.clamp_int(green, 0, 255) << 8;
        color |= MathHelper.clamp_int(blue, 0, 255);
        return color;
    }

    public static int getColor(int brightness, int alpha) {
        return getColor(brightness, brightness, brightness, alpha);
    }

    public void drawWaterMark() {
        int colorXD;
        String clientName;
        String name;
        float h = this.hue;
        boolean drawTime = drawTimeVal.getValue();
        FontRenderer font = mc.fontRendererObj;
        if (fontValue.getValue()) font = FontManager.plain18;
        boolean selected = rainBow.getValue();
        Color color2222 = Color.getHSBColor(h / 255.0F, 0.55F, 0.9F);
        int c2222 = color2222.getRGB();
        colorXD = selected ? c2222 : getColor(clientColor.getRed(), clientColor.getGreen(), clientColor.getBlue(), 220);
        clientName = "Exhibition";

        name = String.valueOf(clientName.charAt(0));
        font.drawStringWithShadow(name, 3.0F, 3.0F, colorXD);

        String ok = clientName.substring(1);
        SimpleDateFormat sdfDate = new SimpleDateFormat("hh:mm");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        font.drawStringWithShadow(ok + (drawTime ? " \2477[\247r" + strDate + "\2477]\247f" : ""), 3F + font.getStringWidth(name), 3.0F, getColor(255, 220));
    }

    private void drawArrayList(float h, int colorXD) {
        FontRenderer font;
        if (fontValue.getValue()) font = FontManager.plain18;
        else {
            font = mc.fontRendererObj;
        }
        boolean left = arrayListLeft.getValue();
        int y = left ? 15 : 3;
        List<Module> modules = Client.getInstance().moduleManager.getEnableModules();

        modules.sort(Comparator.comparingDouble((o) -> -Mth.getIncremental(font.getStringWidth(o.getSuffix() != null ? o.getTranslatedName() + " " + o.getSuffix() : o.getTranslatedName()), 0.5D)));

        for (Module module : modules) {
            if (h > 255.0F) {
                h = 0.0F;
            }

            String suffix = module.getSuffix() != null ? " \2477" + module.getSuffix() : "";
            ScaledResolution resolution = new ScaledResolution(mc);
            String text = module.getTranslatedName() + suffix;
            float x = left ? 3.0F : resolution.getScaledWidth() - font.getStringWidth(text) - 3.0F;
            boolean rainbow = rainBow.getValue();
            font.drawStringWithShadow(text, x, y, rainbow ? colorXD : clientColor.getRGB());
            h += 9.0F;
            y += 9;
        }
    }

    public void renderNotifications() {
        float yOffset = 0;
        int notificationHeight;
        int notificationWidth;
        int actualOffset;
        ScaledResolution sr = new ScaledResolution(mc);

        NotificationManager.setToggleTime(time.getValue().floatValue());

        for (Notification notification : NotificationManager.getNotifications()) {
            Animation animation = notification.getAnimation();
            animation.setDirection(notification.getTimerUtil().finished((long) notification.getTime()) ? Direction.BACKWARDS : Direction.FORWARDS);

            if (animation.finished(Direction.BACKWARDS)) {
                NotificationManager.getNotifications().remove(notification);
                continue;
            }

            float x, y;

            animation.setDuration(200);
            actualOffset = 3;
            notificationHeight = 16;
            String editTitle = notification.getNotificationType().name() + ": " + notification.getTitle() + notification.getDescription();

            notificationWidth = FontManager.verdana18.getStringWidth(editTitle) + 5;

            x = sr.getScaledWidth() - (notificationWidth + 5);
            y = sr.getScaledHeight() - (yOffset + 18 + notificationHeight);

            notification.drawSuicideX(x, y, notificationWidth, notificationHeight, animation.getOutput().floatValue());

            yOffset += (notificationHeight + actualOffset) * animation.getOutput().floatValue();
        }
    }
}
