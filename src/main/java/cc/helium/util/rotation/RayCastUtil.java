package cc.helium.util.rotation;

import cc.helium.util.Util;
import cc.helium.util.vector.Vector2f;
import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import net.optifine.reflect.Reflector;

import java.util.List;

public class RayCastUtil implements Util {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static Entity raycastEntity(double range, IEntityFilter entityFilter) {
        return raycastEntity(range, RotationComponent.lastServerRotations.x, RotationComponent.lastServerRotations.y, entityFilter);
    }

    public static MovingObjectPosition rayCast(Vector2f rotation, double range, float expand, boolean throughWall) {
        return rayCast(rotation, range, expand, mc.thePlayer, throughWall);
    }

    public static Entity raycastEntity(double range, float yaw, float pitch, IEntityFilter entityFilter) {
        Entity renderViewEntity = mc.getRenderViewEntity();
        if (renderViewEntity != null && mc.theWorld != null) {
            double blockReachDistance = range;
            Vec3 eyePosition = renderViewEntity.getPositionEyes(1.0f);
            float yawCos = MathHelper.cos(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
            float yawSin = MathHelper.sin(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
            float pitchCos = -MathHelper.cos(-pitch * ((float)Math.PI / 180));
            float pitchSin = MathHelper.sin(-pitch * ((float)Math.PI / 180));
            Vec3 entityLook = new Vec3(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
            Vec3 vector = eyePosition.addVector(entityLook.xCoord * blockReachDistance, entityLook.yCoord * blockReachDistance, entityLook.zCoord * blockReachDistance);
            List<Entity> entityList = mc.theWorld.getEntitiesInAABBexcluding(renderViewEntity, renderViewEntity.getEntityBoundingBox().addCoord(entityLook.xCoord * blockReachDistance, entityLook.yCoord * blockReachDistance, entityLook.zCoord * blockReachDistance).expand(1.0, 1.0, 1.0), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            Entity pointedEntity = null;
            for (Entity entity : entityList) {
                double eyeDistance;
                if (!entityFilter.canRaycast(entity)) continue;
                float collisionBorderSize = entity.getCollisionBorderSize();
                AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().expand(collisionBorderSize, collisionBorderSize, collisionBorderSize);
                MovingObjectPosition movingObjectPosition = axisAlignedBB.calculateIntercept(eyePosition, vector);
                if (axisAlignedBB.isVecInside(eyePosition)) {
                    if (!(blockReachDistance >= 0.0)) continue;
                    pointedEntity = entity;
                    blockReachDistance = 0.0;
                    continue;
                }
                if (movingObjectPosition == null || !((eyeDistance = eyePosition.distanceTo(movingObjectPosition.hitVec)) < blockReachDistance) && blockReachDistance != 0.0) continue;
                if (entity == renderViewEntity.ridingEntity && !Reflector.callBoolean(entity, Reflector.ForgeEntity_canRiderInteract)) {
                    if (blockReachDistance != 0.0) continue;
                    pointedEntity = entity;
                    continue;
                }
                pointedEntity = entity;
                blockReachDistance = eyeDistance;
            }
            return pointedEntity;
        }
        return null;
    }

    public static MovingObjectPosition rayCast(Vector2f rotation, double range) {
        return rayCast(rotation, range, 0.0f);
    }

    public static MovingObjectPosition rayCast(Vector2f rotation, double range, float expand, Entity entity, boolean throughWall) {
        float partialTicks = mc.timer.renderPartialTicks;
        if (entity != null && mc.theWorld != null) {
            MovingObjectPosition objectMouseOver = entity.rayTrace(range, rotation.x, rotation.y);
            double d1 = range;
            Vec3 vec3 = entity.getPositionEyes(2.0f);
            if (objectMouseOver != null && objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && !throughWall) {
                d1 = objectMouseOver.hitVec.distanceTo(vec3);
            }
            Vec3 vec31 = mc.thePlayer.getVectorForRotation(rotation.y, rotation.x);
            Vec3 vec32 = vec3.addVector(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range);
            Entity pointedEntity = null;
            Vec3 vec33 = null;
            float f = 1.0f;
            List<Entity> list = mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range).expand(1.0, 1.0, 1.0), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            double d2 = d1;
            for (Entity entity1 : list) {
                double d3;
                float f1 = entity1.getCollisionBorderSize() + expand;
                AxisAlignedBB original = entity1.getEntityBoundingBox();
                original = original.offset(entity.posX - entity.prevPosX, entity.posY - entity.prevPosY, entity.posZ - entity.prevPosZ);
                AxisAlignedBB axisalignedbb = f1 >= 0.0f ? original.expand(f1, f1, f1).expand(-f1, -f1, -f1) : original.contract(f1, f1, f1).contract(-f1, -f1, -f1);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
                if (axisalignedbb.isVecInside(vec3)) {
                    if (!(d2 >= 0.0)) continue;
                    pointedEntity = entity1;
                    vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                    d2 = 0.0;
                    continue;
                }
                if (movingobjectposition == null || !((d3 = vec3.distanceTo(movingobjectposition.hitVec)) < d2) && d2 != 0.0) continue;
                pointedEntity = entity1;
                vec33 = movingobjectposition.hitVec;
                d2 = d3;
            }
            if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
                objectMouseOver = new MovingObjectPosition(pointedEntity, vec33);
            }
            return objectMouseOver;
        }
        return null;
    }

    public static boolean overBlock(Vector2f rotation, EnumFacing enumFacing, BlockPos pos, boolean strict) {
        MovingObjectPosition movingObjectPosition = mc.thePlayer.rayTrace(4.5, rotation.x, rotation.y);
        if (movingObjectPosition == null) {
            return false;
        }
        Vec3 hitVec = movingObjectPosition.hitVec;
        if (hitVec == null) {
            return false;
        }
        return movingObjectPosition.getBlockPos().equals(pos) && (!strict || movingObjectPosition.sideHit == enumFacing);
    }

    public static boolean overBlock(EnumFacing enumFacing, BlockPos pos, boolean strict) {
        MovingObjectPosition movingObjectPosition = mc.objectMouseOver;
        if (movingObjectPosition == null) {
            return false;
        }
        Vec3 hitVec = movingObjectPosition.hitVec;
        if (hitVec == null) {
            return false;
        }
        return movingObjectPosition.getBlockPos().equals(pos) && (!strict || movingObjectPosition.sideHit == enumFacing);
    }

    public static Boolean overBlock(Vector2f rotation, BlockPos pos) {
        return overBlock(rotation, EnumFacing.UP, pos, false);
    }

    public static Boolean overBlock(Vector2f rotation, BlockPos pos, EnumFacing enumFacing) {
        return overBlock(rotation, enumFacing, pos, true);
    }

    public static MovingObjectPosition rayCast(Vector2f rotation, double range, float expand) {
        float partialTicks = mc.timer.renderPartialTicks;
        Entity entity = mc.getRenderViewEntity();
        if (entity != null && mc.theWorld != null) {
            MovingObjectPosition objectMouseOver = entity.rayTrace(range, partialTicks, rotation.getX(), rotation.getY());
            double d1 = range;
            Vec3 vec3 = entity.getPositionEyes(partialTicks);
            if (objectMouseOver != null) {
                d1 = objectMouseOver.hitVec.distanceTo(vec3);
            }
            Vec3 vec31 = mc.thePlayer.getVectorForRotation(rotation.getY(), rotation.getX());
            Vec3 vec32 = vec3.addVector(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range);
            Entity pointedEntity = null;
            Vec3 vec33 = null;
            float f = 1.0f;
            List<Entity> list = mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range).expand(1.0, 1.0, 1.0), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            double d2 = d1;
            for (Entity entity1 : list) {
                double d3;
                float f1 = entity1.getCollisionBorderSize() + expand;
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
                if (axisalignedbb.isVecInside(vec3)) {
                    if (!(d2 >= 0.0)) continue;
                    pointedEntity = entity1;
                    vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                    d2 = 0.0;
                    continue;
                }
                if (movingobjectposition == null || !((d3 = vec3.distanceTo(movingobjectposition.hitVec)) < d2) && d2 != 0.0) continue;
                pointedEntity = entity1;
                vec33 = movingobjectposition.hitVec;
                d2 = d3;
            }
            if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
                objectMouseOver = new MovingObjectPosition(pointedEntity, vec33);
            }
            return objectMouseOver;
        }
        return null;
    }

    public interface IEntityFilter {
        boolean canRaycast(Entity var1);
    }
}

