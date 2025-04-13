package cc.helium.module.impl.misc;

import cc.helium.Client;
import cc.helium.module.Category;
import cc.helium.module.Module;
import cc.helium.value.impl.BoolValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class Teams extends Module {
    private static final BoolValue armorValue = new BoolValue("Armor", true);
    private static final BoolValue colorValue = new BoolValue("Name", true);
    private static final BoolValue scoreboardValue = new BoolValue("Scoreboard", true);

    public Teams() {
        super("Teams", -1, Category.Misc);
    }

    public static boolean isSameTeam(Entity entity) {
        if (entity instanceof EntityPlayer entityPlayer) {
            if (Objects.requireNonNull(Client.getInstance().moduleManager.getModule(Teams.class)).isEnable()) {
                return (armorValue.getValue() && armorTeam(entityPlayer)) ||
                        (colorValue.getValue() && colorTeam(entityPlayer)) ||
                        (scoreboardValue.getValue() && scoreTeam(entityPlayer));
            }
            return false;
        }
        return false;
    }
    
    public static boolean armorTeam(EntityPlayer entityPlayer) {
        if (mc.thePlayer.inventory.armorInventory[3] != null && entityPlayer.inventory.armorInventory[3] != null) {
            ItemStack myHead = mc.thePlayer.inventory.armorInventory[3];
            ItemArmor myItemArmor = (ItemArmor) myHead.getItem();
            ItemStack entityHead = entityPlayer.inventory.armorInventory[3];
            ItemArmor entityItemArmor = (ItemArmor) entityHead.getItem();
            if (String.valueOf(entityItemArmor.getColor(entityHead)).equals("10511680")) {
                return true;
            }
            return myItemArmor.getColor(myHead) == entityItemArmor.getColor(entityHead);
        }
        return false;
    }

    public static boolean colorTeam(EntityPlayer sb) {
        String targetName = StringUtils.replace(sb.getDisplayName().getFormattedText(),"§r", "");
        String clientName = mc.thePlayer.getDisplayName().getFormattedText().replace("§r", "");
        return targetName.startsWith("§" + clientName.charAt(1));
    }

    public static boolean scoreTeam(EntityPlayer entityPlayer) {
        return mc.thePlayer.isOnSameTeam(entityPlayer);
    }
}
