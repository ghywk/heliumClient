package cc.helium.event.impl.update;

import cc.helium.event.api.Event;
import cc.helium.event.api.type.Timing;
import net.minecraft.client.Minecraft;

/**
 * @author Kev1nLeft
 */

public class MotionEvent implements Event {
    private double x, y, z;
    private float yaw, pitch;
    private boolean ground;
    private final Timing eventState;

    public MotionEvent(double x, double y, double z, float yaw, float pitch, boolean ground) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.ground = ground;
        this.eventState = Timing.PRE;
    }

    public MotionEvent(float yaw, float pitch) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.eventState = Timing.POST;
    }

    public boolean isPre() {
        return this.eventState == Timing.PRE;
    }

    public boolean isPost() {
        return this.eventState == Timing.POST;
    }

    public boolean isGround() {
        return ground;
    }

    public void setGround(boolean ground) {
        this.ground = ground;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setYawHead(float yaw) {
        Minecraft.getMinecraft().thePlayer.prevRotationYawHead = Minecraft.getMinecraft().thePlayer.rotationYawHead;
        Minecraft.getMinecraft().thePlayer.rotationYawHead = yaw;
    }
}
