package cc.helium.event.impl.update;

import cc.helium.event.api.Event;
import cc.helium.event.api.type.Timing;

/**
 * @author Kev1nLeft
 */

public record TickEvent(Timing timing) implements Event {
}
