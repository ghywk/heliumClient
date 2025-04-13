package cc.helium.module.impl.misc;

import cc.helium.Client;
import cc.helium.event.api.annotations.SubscribeEvent;
import cc.helium.event.impl.packet.PacketReceiveEvent;
import cc.helium.event.impl.world.WorldEvent;
import cc.helium.module.Category;
import cc.helium.module.Module;
import cc.helium.util.logging.LogUtil;
import cc.helium.value.impl.BoolValue;
import cc.helium.value.impl.ModeValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S14PacketEntity;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AntiBot extends Module {
    private static final BoolValue entityID = new BoolValue("EntityID", false);
    private static final BoolValue sleep = new BoolValue("Sleep", false);
    private static final BoolValue noArmor = new BoolValue("No armor", false);
    private static final BoolValue height = new BoolValue("Height", false);
    private static final BoolValue ground = new BoolValue("Ground", false);
    private static final BoolValue dead = new BoolValue("Dead", false);
    private static final BoolValue health = new BoolValue("Health", false);
    private static final BoolValue hytGetNames = new BoolValue("Get name", false);
    private final BoolValue tips = new BoolValue("Get name tips", false);
    private static final ModeValue hytGetNameModes = new ModeValue("Get name mode", "Normal", "Normal", "LargeOrb", "SmallOrb");
    private static final List<Integer> groundBotList = new ArrayList<>();
    private static final List<String> playerName = new ArrayList<>();

    public AntiBot() {
        super("AntiBot", -1, Category.Misc);
    }

    @SubscribeEvent
    public void onWorld(WorldEvent event) {
        this.clearAll();
    }

    private void clearAll() {
        playerName.clear();
    }

    @SubscribeEvent
    public void onPacketReceive(PacketReceiveEvent event) {
        if (mc.thePlayer != null && mc.theWorld != null) {
            Packet<?> packet = event.getPacket();
            if (event.getPacket() instanceof S14PacketEntity && ground.getValue()) {
                Entity entity = ((S14PacketEntity)event.getPacket()).getEntity(mc.theWorld);
                if (entity instanceof EntityPlayer && ((S14PacketEntity)event.getPacket()).getOnGround() && !groundBotList.contains(entity.getEntityId())) {
                    groundBotList.add(entity.getEntityId());
                }
            }

            if (hytGetNames.getValue() && packet instanceof S02PacketChat s02PacketChat) {
                if (s02PacketChat.getChatComponent().getUnformattedText().contains("获得胜利!")
                        || s02PacketChat.getChatComponent().getUnformattedText().contains("游戏开始 ...")) {
                    this.clearAll();
                }
                Matcher matcher = Pattern.compile("杀死了 (.*?)\\(").matcher(s02PacketChat.getChatComponent().getUnformattedText());
                Matcher matcher2 = Pattern.compile("起床战争>> (.*?) (\\((((.*?) 死了!)))").matcher(s02PacketChat.getChatComponent().getUnformattedText());
                String var4 = hytGetNameModes.getValue();
                switch(var4) {
                    case "Normal":
                    case "LargeOrb":
                        if (matcher.find() && !s02PacketChat.getChatComponent().getUnformattedText().contains(": 起床战争>>")
                                || !s02PacketChat.getChatComponent().getUnformattedText().contains(": 杀死了")) {
                            String name = matcher.group(1).trim();
                            registerBot(name, 6000L);
                        }

                        if (matcher2.find() && !s02PacketChat.getChatComponent().getUnformattedText().contains(": 起床战争>>")
                                || !s02PacketChat.getChatComponent().getUnformattedText().contains(": 杀死了")) {
                            String name = matcher2.group(1).trim();
                            registerBot(name, 6000L);
                        }
                        break;
                    case "SmallOrb":
                        if (matcher.find() && !s02PacketChat.getChatComponent().getUnformattedText().contains(": 击败了")
                                || !s02PacketChat.getChatComponent().getUnformattedText().contains(": 玩家 ")) {
                            String name = matcher.group(1).trim();
                            registerBot(name, 10000L);
                        }

                        if (matcher2.find() && !s02PacketChat.getChatComponent().getUnformattedText().contains(": 击败了")
                                || !s02PacketChat.getChatComponent().getUnformattedText().contains(": 玩家 ")) {
                            String name = matcher2.group(1).trim();
                            if (!name.isEmpty()) {
                                playerName.add(name);
                                LogUtil.log_chat("Register bot: " + name);
                                new Thread(() -> {
                                    try {
                                        Thread.sleep(10000L);
                                        playerName.remove(name);
                                        LogUtil.log_chat("Unregister bot" + name);
                                    } catch (InterruptedException var2x) {
                                        var2x.printStackTrace();
                                    }
                                }).start();
                            }
                        }
                }
            }
        }
    }

    private void registerBot(String name, long millis) {
        if (!name.isEmpty()) {
            playerName.add(name);
            if (this.tips.getValue()) {
                LogUtil.log_chat("Register bot: " + name);
            }

            new Thread(() -> {
                try {
                    Thread.sleep(millis);
                    playerName.remove(name);
                    if (this.tips.getValue()) {
                        LogUtil.log_chat("Unregister bot" + name);
                    }
                } catch (InterruptedException var3x) {
                    var3x.printStackTrace();
                }
            }).start();
        }
    }

    public static boolean isServerBot(Entity entity) {
        if (Client.getInstance().moduleManager.getModule(AntiBot.class).isEnable() && entity instanceof EntityPlayer) {
            if (hytGetNames.getValue() && playerName.contains(entity.getName())) {
                return true;
            } else if (!height.getValue() || !((double)entity.height <= 0.5) && !((EntityPlayer)entity).isPlayerSleeping() && entity.ticksExisted >= 80) {
                if (dead.getValue() && entity.isDead) {
                    return true;
                } else if (health.getValue() && ((EntityPlayer)entity).getHealth() == 0.0F) {
                    return true;
                } else if (sleep.getValue() && ((EntityPlayer)entity).isPlayerSleeping()) {
                    return true;
                } else if (!entityID.getValue() || entity.getEntityId() < 1000000000 && entity.getEntityId() > -1) {
                    if (ground.getValue() && !groundBotList.contains(entity.getEntityId())) {
                        return true;
                    } else {
                        return noArmor.getValue()
                                && ((EntityPlayer)entity).inventory.armorInventory[0] == null
                                && ((EntityPlayer)entity).inventory.armorInventory[1] == null
                                && ((EntityPlayer)entity).inventory.armorInventory[2] == null
                                && ((EntityPlayer)entity).inventory.armorInventory[3] == null;
                    }
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
