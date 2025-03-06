package cc.helium.event.impl.movement;

import cc.helium.event.api.cancellable.CancellableEvent;

public final class StrafeEvent extends CancellableEvent {
    private float forward;
    private float strafe;
    private float friction;
    private float yaw;

    public StrafeEvent(float forward, float friction, float yaw, float strafe) {
        this.forward = forward;
        this.friction = friction;
        this.yaw = yaw;
        this.strafe = strafe;
    }

    public float getForward() {
        return forward;
    }

    public void setForward(float forward) {
        this.forward = forward;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getStrafe() {
        return strafe;
    }

    public void setStrafe(float strafe) {
        this.strafe = strafe;
    }
}
