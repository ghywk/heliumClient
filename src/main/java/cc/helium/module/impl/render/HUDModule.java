package cc.helium.module.impl.render;

import cc.helium.event.api.annotations.TargetEvent;
import cc.helium.event.impl.render.Render2DEvent;
import cc.helium.module.Category;
import cc.helium.module.Module;
import cc.helium.util.animation.Animation;
import cc.helium.util.animation.Direction;
import cc.helium.value.impl.BoolValue;
import cc.helium.value.impl.NumberValue;
import cc.helium.visual.font.FontManager;
import cc.helium.visual.hud.notifications.Notification;
import cc.helium.visual.hud.notifications.NotificationManager;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

/**
 * @author Kev1nLeft
 */

public class HUDModule extends Module {
    private static final NumberValue red = new NumberValue("ClientRed", 163, 0, 255, 1);
    private static final NumberValue blue = new NumberValue("ClientBlue", 223, 0, 255, 1);
    private static final NumberValue green = new NumberValue("ClientGreen", 255, 0, 255, 1);
    private static final NumberValue alpha = new NumberValue("ClientAlpha", 220, 0, 255, 1);
    public static BoolValue notificationsValue = new BoolValue("Notifications", true);
    private final NumberValue time = new NumberValue("Noti-Time", 2, 1, 10, 0.5);
    private final BoolValue notiGlow = new BoolValue("Noti-Glow", false);

    public static Color clientColor = new Color(red.getValue().intValue(), green.getValue().intValue(), blue.getValue().intValue(), alpha.getValue().intValue());
    public HUDModule() {
        super("HUD", -1, Category.Render);
    }

    @TargetEvent
    public void onRender2D(Render2DEvent event) {
        if (!notificationsValue.getValue() || mc.thePlayer == null) return;
        renderEffects();
        render();
    }

    public void render() {
        float yOffset = 0;
        int notificationHeight = 0;
        int notificationWidth;
        int actualOffset = 0;
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

            notification.drawSuicideX(x, y, notificationWidth, notificationHeight, (float) animation.getOutput().floatValue());

            yOffset += (notificationHeight + actualOffset) * animation.getOutput().floatValue();

        }
    }

    public void renderEffects() {
        float yOffset = 0;
        int notificationHeight = 0;
        int notificationWidth;
        int actualOffset = 0;
        ScaledResolution sr = new ScaledResolution(mc);

        for (Notification notification : NotificationManager.getNotifications()) {
            Animation animation = notification.getAnimation();
            animation.setDirection(notification.getTimerUtil().finished((long) notification.getTime()) ? Direction.BACKWARDS : Direction.FORWARDS);

            if (animation.finished(Direction.BACKWARDS)) {
                NotificationManager.getNotifications().remove(notification);
                continue;
            }

            float x, y;

            actualOffset = 3;
            notificationHeight = 16;
            String editTitle = notification.getNotificationType().name() + ": " + notification.getTitle() + notification.getDescription();

            notificationWidth = (int) FontManager.verdana18.getStringWidth(editTitle) + 5;

            x = sr.getScaledWidth() - (notificationWidth + 5);
            y = sr.getScaledHeight() - (yOffset + 18 + notificationHeight);

            notification.blurSuicideX(x - 5, y, notificationWidth, notificationHeight, animation.getOutput().floatValue());

            yOffset += (notificationHeight + actualOffset) * animation.getOutput().floatValue();
        }
    }
}
