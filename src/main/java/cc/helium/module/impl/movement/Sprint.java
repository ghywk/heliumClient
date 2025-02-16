package cc.helium.module.impl.movement;

import cc.helium.event.api.annotations.TargetEvent;
import cc.helium.event.impl.update.UpdateEvent;
import cc.helium.module.Category;
import cc.helium.module.Module;
import org.lwjgl.input.Keyboard;

/**
 * @author Kev1nLeft
 */

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", Keyboard.KEY_LCONTROL, Category.Movement);
    }

    @TargetEvent
    public void onUpdate(UpdateEvent ignored) {
        if (mc.thePlayer == null) return;

        mc.gameSettings.keyBindSprint.setPressed(true);
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer == null) return;

        mc.thePlayer.setSprinting(false);
        mc.gameSettings.keyBindSprint.setPressed(false);
    }
}
