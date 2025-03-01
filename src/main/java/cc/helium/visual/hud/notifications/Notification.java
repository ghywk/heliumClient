package cc.helium.visual.hud.notifications;

import cc.helium.util.Util;
import cc.helium.util.animation.Animation;
import cc.helium.util.animation.impl.DecelerateAnimation;
import cc.helium.util.time.StopWatch;
import cc.helium.visual.font.FontManager;

import java.awt.*;

public class Notification implements Util {
    private final NotificationType notificationType;
    private final String title, description;
    private final float time;
    private final StopWatch timerUtil;
    private final Animation animation;

    public Notification(NotificationType type, String title, String description) {
        this(type, title, description, NotificationManager.getToggleTime());
    }

    public Notification(NotificationType type, String title, String description, float time) {
        this.title = title;
        this.description = description;
        this.time = (long) (time * 1000);
        timerUtil = new StopWatch();
        this.notificationType = type;
        animation = new DecelerateAnimation(250, 1);
    }

    public void drawSuicideX(float x, float y, float width, float height, float animation) {
        float heightVal = height * animation <= 6 ? 0 : height * animation;
        float yVal = (y + height) - heightVal;

        String editTitle = notificationType.name() + ": " + getTitle() + getDescription();

        FontManager.verdana18.drawString(editTitle, x, (int) (yVal + FontManager.verdana18.getMiddleOfBox(heightVal)), applyOpacity(Color.WHITE, animation - .5f).getRGB());
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public float getTime() {
        return time;
    }

    public StopWatch getTimerUtil() {
        return timerUtil;
    }

    public Animation getAnimation() {
        return animation;
    }

    public static int applyOpacity(int color, float opacity) {
        Color old = new Color(color);
        return applyOpacity(old, opacity).getRGB();
    }

    //Opacity value ranges from 0-1
    public static Color applyOpacity(Color color, float opacity) {
        opacity = Math.min(1, Math.max(0, opacity));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (color.getAlpha() * opacity));
    }

}
