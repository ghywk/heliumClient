package cc.helium.visual.hud.notifications;

import cc.helium.module.impl.render.HUDModule;

import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager {
    private static float toggleTime = 2;

    private static final CopyOnWriteArrayList<Notification> notifications = new CopyOnWriteArrayList<>();

    public static void post(NotificationType type, String title, String description) {
        post(new Notification(type, title, description));
    }

    public static void post(NotificationType type, String title, String description, float time) {
        post(new Notification(type, title, description, time));
    }

    private static void post(Notification notification) {
        if (HUDModule.notificationsValue.getValue()) {
            notifications.add(notification);
        }
    }

    public static float getToggleTime() {
        return toggleTime;
    }

    public static CopyOnWriteArrayList<Notification> getNotifications() {
        return notifications;
    }

    public static void setToggleTime(float toggleTime) {
        NotificationManager.toggleTime = toggleTime;
    }
}
