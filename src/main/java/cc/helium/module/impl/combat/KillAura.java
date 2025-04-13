package cc.helium.module.impl.combat;

import cc.helium.event.api.annotations.Priority;
import cc.helium.event.api.annotations.SubscribeEvent;
import cc.helium.event.impl.update.MotionEvent;
import cc.helium.event.impl.update.UpdateEvent;
import cc.helium.module.Category;
import cc.helium.module.Module;
import cc.helium.module.impl.misc.AntiBot;
import cc.helium.module.impl.misc.Teams;
import cc.helium.util.packet.PacketUtil;
import cc.helium.util.rotation.MovementFix;
import cc.helium.util.rotation.RotationComponent;
import cc.helium.util.rotation.RotationUtil;
import cc.helium.util.time.StopWatch;
import cc.helium.util.vector.Vector2f;
import cc.helium.value.impl.BoolValue;
import cc.helium.value.impl.ModeValue;
import cc.helium.value.impl.NumberValue;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.protocols.v1_8to1_9.Protocol1_8To1_9;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import de.florianmichael.viamcp.fixes.AttackOrder;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author A3roCat
 */

public class KillAura extends Module {
    public ModeValue priority = new ModeValue("Priority", "Range", "Range", "Fov", "Angle", "Health");
    public ModeValue mode = new ModeValue("Mode", "Single", "Single", "Switch", "Multi");
    public ModeValue attackMode = new ModeValue("Attack mode", "Post", "Pre", "Post");
    public ModeValue rotMode = new ModeValue("Rotation mode", "Blatant", "Normal", "Blatant");
    public ModeValue abMode = new ModeValue("AutoBlock mode", "Grim", "Off", "Grim", "Key Bind", "Use Item", "Fake");
    public NumberValue cps = new NumberValue("CPS", 13.0, 1.0, 20.0, 1.0);
    public NumberValue range = new NumberValue("Range", 3.0, 2.0, 6.0, 0.1);
    public NumberValue blockRange = new NumberValue("Block range", 4.0, 2.0, 6.0, 0.1);
    public NumberValue scanRange = new NumberValue("Scan range", 5.0, 2.0, 6.0, 0.1);
    public NumberValue switchDelay = new NumberValue("Switch delay", 500.0, 0.0, 1000.0, 10.0);
    public BoolValue moveFixValue = new BoolValue("Movement fix", true);
    public BoolValue strictValue = new BoolValue("Strict", false, () -> this.moveFixValue.getValue());
    public BoolValue rayCastValue = new BoolValue("Ray cast", false);
    public BoolValue playerValue = new BoolValue("Player", true);
    public BoolValue animalValue = new BoolValue("Animal", false);
    public BoolValue mobValue = new BoolValue("Mob", false);
    public BoolValue invisibleValue = new BoolValue("Invisible", true);
    private final StopWatch switchTimer = new StopWatch();
    private final StopWatch attackTimer = new StopWatch();
    public List<Entity> targets = new ArrayList<>();
    public static EntityLivingBase target;
    public static boolean isBlocking;
    public static boolean renderBlocking;
    public int index = 0;

    public KillAura() {
        super("KillAura", Keyboard.KEY_R, Category.Combat);
    }

    @Override
    public void onEnable() {
        this.switchTimer.reset();
        isBlocking = false;
        target = null;
        this.targets.clear();
        if (isBlocking && !this.abMode.getValue().equals("Off")) {
            this.stopBlocking();
        }
        this.index = 0;
    }

    @Override
    public void onDisable() {
        if (KillAura.mc.thePlayer == null) {
            return;
        }
        target = null;
        this.targets.clear();
        if (isBlocking && !this.abMode.getValue().equals("Off")) {
            this.stopBlocking();
        }
        isBlocking = false;
        this.index = 0;
    }

    @SubscribeEvent
    public void onRotations(UpdateEvent ignored) {
        if (!this.targets.isEmpty()) {
            if (this.index >= this.targets.size()) {
                this.index = 0;
            }
            target = (double) KillAura.mc.thePlayer.getClosestDistanceToEntity(this.targets.get(this.index)) <= this.range.getValue() ? (EntityLivingBase)this.targets.get(this.index) : (EntityLivingBase)this.targets.getFirst();
        }
        if (target != null && (double)KillAura.mc.thePlayer.getClosestDistanceToEntity(target) <= this.range.getValue()) {
            float[] rotation = this.getRot();
            RotationComponent.setRotations(new Vector2f(rotation[0], rotation[1]), 10.0f, this.moveFixValue.getValue() ? MovementFix.NORMAL : this.strictValue.getValue() ? MovementFix.TRADITIONAL : MovementFix.OFF);
        }
    }

    private float[] getRot() {
        float[] rot = RotationUtil.getHVHRotation(target);
        switch (this.rotMode.getValue()) {
            case "Normal": {
                Vector2f vec = RotationUtil.calculate(target, true, this.range.getValue(), this.range.getValue(), true, true);
                if (vec != null) {
                    rot = new float[]{vec.x, vec.y};
                }
                break;
            }
            case "Blatant": {
                rot = RotationUtil.getHVHRotation(target);
                break;
            }
        }
        return rot;
    }

    @SubscribeEvent
    @Priority(9)
    public void onUpdate(MotionEvent event) {
        this.setSuffix(abMode.getValue());

        if (event.isPre() && target == null) {
            this.stopBlocking();
        }

        if (event.isPre()) {
            if (KillAura.mc.thePlayer.isDead || KillAura.mc.thePlayer.isSpectator()) {
                return;
            }
            this.targets = this.getTargets(this.scanRange.getValue());
            if (this.targets.isEmpty()) {
                target = null;
            }
            this.sortTargets();
            if ((this.targets.size() > 1 && this.mode.getValue().equals("Switch") || this.mode.getValue().equals("Multi")) && (this.switchTimer.finished(this.switchDelay.getValue().longValue()) || this.mode.getValue().equals("Multi"))) {
                ++this.index;
                this.switchTimer.reset();
            }
            if (this.targets.size() > 1 && this.mode.getValue().equals("Single")) {
                if (target == null) {
                    ++this.index;
                } else {
                    if ((double)KillAura.mc.thePlayer.getClosestDistanceToEntity(target) > this.scanRange.getValue()) {
                        ++this.index;
                    } else if (KillAura.target.isDead) {
                        ++this.index;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    @Priority(0)
    public void blockEvent(MotionEvent e) {
        if (e.isPost() && this.attackMode.is("Pre") && !this.abMode.getValue().equals("Off") && !this.abMode.getValue().contains("Watchdog") && this.shouldBlock()) {
            this.doBlock();
        }
    }

    public boolean shouldAttack() {
        MovingObjectPosition movingObjectPosition = KillAura.mc.objectMouseOver;
        return (double)(KillAura.mc.thePlayer.canEntityBeSeen(target) ? KillAura.mc.thePlayer.getClosestDistanceToEntity(target) : KillAura.mc.thePlayer.getDistanceToEntity(target)) <= this.range.getValue() && (!this.rayCastValue.getValue() || !KillAura.mc.thePlayer.canEntityBeSeen(target) || this.rayCastValue.getValue() && movingObjectPosition != null && movingObjectPosition.entityHit == target);
    }

    public boolean shouldBlock() {
        return target != null && !this.targets.isEmpty() && (double)KillAura.mc.thePlayer.getClosestDistanceToEntity(target) <= this.blockRange.getValue();
    }

    private void attack() {
        if (this.shouldAttack() && this.attackTimer.finished(700L / (long) this.cps.getValue().intValue())) {
            AttackOrder.sendFixedAttack(KillAura.mc.thePlayer, target);
            this.attackTimer.reset();
        }
    }

    @SubscribeEvent
    public void onMotion(MotionEvent event) {
        if (event.isPre()) {
            if (this.attackMode.is("Pre") && target != null) {
                this.attack();
            }
        }
        if (event.isPost() && this.attackMode.is("Post") && target != null) {
            if (!this.abMode.getValue().equals("Off") && this.shouldBlock()) {
                this.doBlock();
            }
            this.attack();
            if (!this.abMode.getValue().equals("Off") && this.shouldBlock()) {
                this.doBlock();
            }
        }
    }

    public boolean isSword() {
        return Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() != null && Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword;
    }

    private void stopBlocking() {
        if (this.isSword() && renderBlocking) {
            switch (this.abMode.getValue()) {
                case "Key Bind": {
                    KillAura.mc.gameSettings.keyBindUseItem.setPressed(false);
                    KillAura.mc.thePlayer.sendQueue.addToSendQueue(new C0EPacketClickWindow(0, 36, 0, 2, new ItemStack(Block.getBlockById(166)), (short) 0));
                    break;
                }
                case "Grim":
            }
            renderBlocking = false;
            isBlocking = false;
        }
    }

    private void doBlock() {
        if (this.isSword()) {
            switch (this.abMode.getValue()) {
                case "Grim": {
                    if (ViaLoadingBase.getInstance().getTargetVersion().newerThan(ProtocolVersion.v1_8)) {
                        KillAura.mc.playerController.sendUseItem(KillAura.mc.thePlayer, KillAura.mc.theWorld, KillAura.mc.thePlayer.getHeldItem());
                        PacketWrapper use_0 = PacketWrapper.create(29, null, Via.getManager().getConnectionManager().getConnections().iterator().next());
                        use_0.write(Types.VAR_INT, 0);
                        PacketUtil.sendToServer(use_0, Protocol1_8To1_9.class, true, true);
                        PacketWrapper use_1 = PacketWrapper.create(29, null, Via.getManager().getConnectionManager().getConnections().iterator().next());
                        use_1.write(Types.VAR_INT, 1);
                        PacketUtil.sendToServer(use_1, Protocol1_8To1_9.class, true, true);
                    } else {
                        KillAura.mc.playerController.sendUseItem(KillAura.mc.thePlayer, KillAura.mc.theWorld, KillAura.mc.thePlayer.getCurrentEquippedItem());
                    }
                    break;
                }
                case "Key Bind": {
                    KillAura.mc.gameSettings.keyBindUseItem.setPressed(true);
                    break;
                }
                case "Use Item": {
                    KillAura.mc.playerController.sendUseItem(KillAura.mc.thePlayer, KillAura.mc.theWorld, KillAura.mc.thePlayer.getCurrentEquippedItem());
                    break;
                }
            }
            if (!this.abMode.is("Fake")) {
                isBlocking = true;
            }
            renderBlocking = true;
        }
    }

    public List<Entity> getTargets(Double value) {
        return Minecraft.getMinecraft().theWorld.loadedEntityList.stream().filter(e -> (double)KillAura.mc.thePlayer.getClosestDistanceToEntity(e) <= value && this.isValid(e, value)).collect(Collectors.toList());
    }

    public boolean isValid(Entity entity, double range) {
        if ((double)KillAura.mc.thePlayer.getClosestDistanceToEntity(entity) > range) {
            return false;
        }
        if (entity.isInvisible() && !this.invisibleValue.getValue()) {
            return false;
        }
        if (!entity.isEntityAlive()) {
            return false;
        }
        if (entity == Minecraft.getMinecraft().thePlayer || entity.isDead || Minecraft.getMinecraft().thePlayer.getHealth() == 0.0f) {
            return false;
        }
        if ((entity instanceof EntityMob || entity instanceof EntityGhast || entity instanceof EntityGolem || entity instanceof EntityDragon || entity instanceof EntitySlime) && this.mobValue.getValue()) {
            return true;
        }
        if ((entity instanceof EntitySquid || entity instanceof EntityBat || entity instanceof EntityVillager) && this.animalValue.getValue()) {
            return true;
        }
        if (entity instanceof EntityAnimal && this.animalValue.getValue()) {
            return true;
        }
        if (AntiBot.isServerBot(entity)) {
            return false;
        }
        if (entity.getEntityId() == -8 || entity.getEntityId() == -1337) {
            return false;
        }
        if (Teams.isSameTeam(entity)) {
            return false;
        }
        return entity instanceof EntityPlayer && this.playerValue.getValue();
    }

    private void sortTargets() {
        if (!this.targets.isEmpty()) {
            EntityPlayerSP thePlayer = KillAura.mc.thePlayer;
            switch (this.priority.getValue()) {
                case "Range": {
                    this.targets.sort((o1, o2) -> (int)(o1.getClosestDistanceToEntity(thePlayer) - o2.getClosestDistanceToEntity(thePlayer)));
                    break;
                }
                case "Fov": {
                    this.targets.sort(Comparator.comparingDouble(o -> this.getDistanceBetweenAngles(thePlayer.rotationPitch, RotationUtil.getRotationsNeeded(o)[0])));
                    break;
                }
                case "Angle": {
                    this.targets.sort((o1, o2) -> {
                        float[] rot1 = RotationUtil.getRotationsNeeded(o1);
                        float[] rot2 = RotationUtil.getRotationsNeeded(o2);
                        return (int)(thePlayer.rotationYaw - rot1[0] - (thePlayer.rotationYaw - rot2[0]));
                    });
                    break;
                }
                case "Health": {
                    this.targets.sort((o1, o2) -> (int)(((EntityLivingBase)o1).getHealth() - ((EntityLivingBase)o2).getHealth()));
                }
            }
        }
    }

    private float getDistanceBetweenAngles(float angle1, float angle2) {
        float agl = Math.abs(angle1 - angle2) % 360.0f;
        if (agl > 180.0f) {
            agl = 0.0f;
        }
        return agl - 1.0f;
    }

    static {
        isBlocking = false;
        renderBlocking = false;
    }
}

