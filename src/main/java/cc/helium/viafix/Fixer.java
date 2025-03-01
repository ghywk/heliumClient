package cc.helium.viafix;

import cc.helium.Client;

import java.util.List;

/**
 * @author Kev1nLeft
 */

public class Fixer {
    public Fixer() {
        Client.getInstance().eventManager.register(new FixMoveFlying());
        Client.getInstance().eventManager.register(new FixMovement());
        Client.getInstance().eventManager.register(new FixPlacement());
        Client.getInstance().eventManager.register(new FixInteract());
        Client.getInstance().eventManager.register(new FixLadder());
    }
}
