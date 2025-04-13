package cc.helium.event.impl.world;

import cc.helium.event.api.cancellable.CancellableEvent;

/**
 * @author Kev1nLeft
 */

public class PlaceEvent extends CancellableEvent {
    private boolean shouldRightClick;
    private int slot;

    public PlaceEvent(final int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return this.slot;
    }

    public void setSlot(final int slot) {
        this.slot = slot;
    }

    public boolean isShouldRightClick() {
        return this.shouldRightClick;
    }

    public void setShouldRightClick(final boolean shouldRightClick) {
        this.shouldRightClick = shouldRightClick;
    }
}
