package cc.helium.event.impl.render;

import cc.helium.event.api.Event;
import cc.helium.util.vector.Vector2f;

public final class LookEvent implements Event {
    private Vector2f rotation;

    public LookEvent(Vector2f rotation) {
        this.rotation = rotation;
    }

    public Vector2f getRotation() {
        return rotation;
    }

    public void setRotation(Vector2f rotation) {
        this.rotation = rotation;
    }
}
