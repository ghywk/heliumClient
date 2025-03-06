package cc.helium.util.viafix;

import cc.helium.Client;

/**
 * @author Kev1nLeft
 */

public class Fixer {
    public Fixer() {
        Client.getInstance().eventManager.register(new FixMoveFlying());
        Client.getInstance().eventManager.register(new FixMovement());
        Client.getInstance().eventManager.register(new FixPlacement());
        Client.getInstance().eventManager.register(new FixInteract());
    }
}
