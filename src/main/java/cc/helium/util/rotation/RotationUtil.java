package cc.helium.util.rotation;

import cc.helium.util.Util;
import cc.helium.util.cal.Location;
import cc.helium.util.cal.Mth;
import cc.helium.util.cal.Rotation;
import cc.helium.util.vector.Vector2f;
import cc.helium.util.vector.Vector3d;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Kev1nLeft
 */

public class RotationUtil implements Util {
    public static final SecureRandom secureRandom = new SecureRandom();
    private static final List<Double> xzPercents = Arrays.asList(0.5, 0.4, 0.3, 0.2, 0.1, 0.0, -0.1, -0.2, -0.3, -0.4, -0.5);

    public static float[] getHVHRotation(Entity entity) {
        if (entity == null) {
            return null;
        }
        double diffX = entity.posX - RotationUtil.mc.thePlayer.posX;
        double diffZ = entity.posZ - RotationUtil.mc.thePlayer.posZ;
        Vec3 BestPos = RotationUtil.getNearestPointBB(RotationUtil.mc.thePlayer.getPositionEyes(1.0f), entity.getEntityBoundingBox());
        Location myEyePos = new Location(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + (double)RotationUtil.mc.thePlayer.getEyeHeight(), Minecraft.getMinecraft().thePlayer.posZ);
        double diffY = BestPos.yCoord - myEyePos.getY();
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static Vec3 getNearestPointBB(Vec3 eye, AxisAlignedBB box) {
        double[] origin = new double[]{eye.xCoord, eye.yCoord, eye.zCoord};
        double[] destMins = new double[]{box.minX, box.minY, box.minZ};
        double[] destMaxs = new double[]{box.maxX, box.maxY, box.maxZ};
        for (int i = 0; i < 3; ++i) {
            if (origin[i] > destMaxs[i]) {
                origin[i] = destMaxs[i];
                continue;
            }
            if (!(origin[i] < destMins[i])) continue;
            origin[i] = destMins[i];
        }
        return new Vec3(origin[0], origin[1], origin[2]);
    }

    public static Vector2f toRotationByVector(Vec3 vec, boolean predict) {
        Vec3 eyesPos = new Vec3(RotationUtil.mc.thePlayer.posX, RotationUtil.mc.thePlayer.getEntityBoundingBox().minY + (double)RotationUtil.mc.thePlayer.getEyeHeight(), RotationUtil.mc.thePlayer.posZ);
        if (predict) {
            eyesPos.addVector(RotationUtil.mc.thePlayer.motionX, RotationUtil.mc.thePlayer.motionY, RotationUtil.mc.thePlayer.motionZ);
        }
        double diffX = vec.xCoord - eyesPos.xCoord;
        double diffY = vec.yCoord - eyesPos.yCoord;
        double diffZ = vec.zCoord - eyesPos.zCoord;
        return new Vector2f(MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f), MathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))))));
    }

    public static Vector2f calculateSimple(Entity entity, double range, double wallRange) {
        AxisAlignedBB aabb = entity.getEntityBoundingBox().contract(-0.05, -0.05, -0.05).contract(0.05, 0.05, 0.05);
        range += 0.05;
        wallRange += 0.05;
        Vec3 eyePos = RotationUtil.mc.thePlayer.getPositionEyes(1.0f);
        Vec3 nearest = new Vec3(Mth.clamp(eyePos.xCoord, aabb.minX, aabb.maxX), Mth.clamp(eyePos.yCoord, aabb.minY, aabb.maxY), Mth.clamp(eyePos.zCoord, aabb.minZ, aabb.maxZ));
        Vector2f rotation = RotationUtil.toRotationByVector(nearest, false);
        if (nearest.subtract(eyePos).lengthSquared() <= wallRange * wallRange) {
            return rotation;
        }
        MovingObjectPosition result = RayCastUtil.rayCast(rotation, range, 0.0f, false);
        double maxRange = Math.max(wallRange, range);
        if (result != null && result.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && result.entityHit == entity && result.hitVec.subtract(eyePos).lengthSquared() <= maxRange * maxRange) {
            return rotation;
        }
        return null;
    }

    public static Vector2f calculate(Vector3d from, Vector3d to) {
        Vector3d diff = to.subtract(from);
        double distance = Math.hypot(diff.getX(), diff.getZ());
        float yaw = (float)(MathHelper.atan2(diff.getZ(), diff.getX()) * (double)MathHelper.TO_DEGREES) - 90.0f;
        float pitch = (float)(-(MathHelper.atan2(diff.getY(), distance) * (double)MathHelper.TO_DEGREES));
        return new Vector2f(yaw, pitch);
    }

    public static Vector2f calculate(Vec3 to) {
        return RotationUtil.calculate(RotationUtil.mc.thePlayer.getPositionVector3d().add(0.0, RotationUtil.mc.thePlayer.getEyeHeight(), 0.0), new Vector3d(to.xCoord, to.yCoord, to.zCoord));
    }

    public static Vector2f calculate(Entity entity, boolean adaptive, double range, double wallRange, boolean predict, boolean randomCenter) {
        MovingObjectPosition normalResult;
        if (RotationUtil.mc.thePlayer == null) {
            return null;
        }
        double rangeSq = range * range;
        double wallRangeSq = wallRange * wallRange;
        Vector2f simpleRotation = RotationUtil.calculateSimple(entity, range, wallRange);
        if (simpleRotation != null) {
            return simpleRotation;
        }
        Vector2f normalRotations = RotationUtil.toRotationByVector(RotationUtil.getVec(entity), predict);
        if (!randomCenter && (normalResult = RayCastUtil.rayCast(normalRotations, range, 0.0f, false)) != null && normalResult.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            return normalRotations;
        }
        double yStart = 1.0;
        double yEnd = 0.0;
        double yStep = -0.5;
        if (randomCenter && secureRandom.nextBoolean()) {
            yStart = 0.0;
            yEnd = 1.0;
            yStep = 0.5;
        }
        double yPercent = yStart;
        while (Math.abs(yEnd - yPercent) > 0.001) {
            if (randomCenter) {
                Collections.shuffle(xzPercents);
            }
            for (double xzPercent : xzPercents) {
                for (int side = 0; side <= 3; ++side) {
                    MovingObjectPosition result;
                    double xPercent = 0.0;
                    double zPercent = 0.0;
                    switch (side) {
                        case 0: {
                            xPercent = xzPercent;
                            zPercent = 0.5;
                            break;
                        }
                        case 1: {
                            xPercent = xzPercent;
                            zPercent = -0.5;
                            break;
                        }
                        case 2: {
                            xPercent = 0.5;
                            zPercent = xzPercent;
                            break;
                        }
                        case 3: {
                            xPercent = -0.5;
                            zPercent = xzPercent;
                        }
                    }
                    Vec3 Vec32 = RotationUtil.getVec(entity).add(new Vec3((entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX) * xPercent, (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * yPercent, (entity.getEntityBoundingBox().maxZ - entity.getEntityBoundingBox().minZ) * zPercent));
                    double distanceSq = Vec32.squareDistanceTo(RotationUtil.mc.thePlayer.getPositionEyes(1.0f));
                    Rotation rotation = RotationUtil.toRotationByRot(Vec32, predict);
                    rotation.fixedSensitivity(RotationUtil.mc.gameSettings.mouseSensitivity);
                    rotation.distanceSq = distanceSq;
                    if (distanceSq <= wallRangeSq && (result = RayCastUtil.rayCast(rotation.toVec2f(), wallRange, 0.0f, true)) != null && result.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                        return rotation.toVec2f();
                    }
                    if (!(distanceSq <= rangeSq) || (result = RayCastUtil.rayCast(rotation.toVec2f(), range, 0.0f, false)) == null || result.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) continue;
                    return rotation.toVec2f();
                }
            }
            yPercent += yStep;
        }
        return null;
    }

    public static Rotation toRotationByRot(Vec3 vec, boolean predict) {
        Vec3 eyesPos = new Vec3(RotationUtil.mc.thePlayer.posX, RotationUtil.mc.thePlayer.getEntityBoundingBox().minY + (double)RotationUtil.mc.thePlayer.getEyeHeight(), RotationUtil.mc.thePlayer.posZ);
        if (predict) {
            eyesPos.addVector(RotationUtil.mc.thePlayer.motionX, RotationUtil.mc.thePlayer.motionY, RotationUtil.mc.thePlayer.motionZ);
        }
        double diffX = vec.xCoord - eyesPos.xCoord;
        double diffY = vec.yCoord - eyesPos.yCoord;
        double diffZ = vec.zCoord - eyesPos.zCoord;
        return new Rotation(MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f), MathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))))));
    }

    public static Vec3 getVec(Entity entity) {
        return new Vec3(entity.posX, entity.posY, entity.posZ);
    }

    public static float[] getRotationsNeeded(Entity target) {
        double yDist = target.posY - RotationUtil.mc.thePlayer.posY;
        Vec3 pos = yDist >= 1.7 ? new Vec3(target.posX, target.posY, target.posZ) : (yDist <= -1.7 ? new Vec3(target.posX, target.posY + (double)target.getEyeHeight(), target.posZ) : new Vec3(target.posX, target.posY + (double)(target.getEyeHeight() / 2.0f), target.posZ));
        Vec3 vec = new Vec3(RotationUtil.mc.thePlayer.posX, RotationUtil.mc.thePlayer.getEntityBoundingBox().minY + (double)RotationUtil.mc.thePlayer.getEyeHeight(), RotationUtil.mc.thePlayer.posZ);
        double x = pos.xCoord - vec.xCoord;
        double y = pos.yCoord - vec.yCoord;
        double z = pos.zCoord - vec.zCoord;
        double sqrt = Math.sqrt(x * x + z * z);
        float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(y, sqrt)));
        return new float[]{yaw, Math.min(Math.max(pitch, -90.0f), 90.0f)};
    }

    private static float[] getRotationsByVec(Vec3 origin, Vec3 position) {
        Vec3 difference = position.subtract(origin);
        double distance = difference.flat().lengthVector();
        float yaw = (float)Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(difference.yCoord, distance)));
        return new float[]{yaw, pitch};
    }

    public static float[] getRotationBlock(BlockPos pos) {
        return RotationUtil.getRotationsByVec(RotationUtil.mc.thePlayer.getPositionVector().addVector(0.0, RotationUtil.mc.thePlayer.getEyeHeight(), 0.0), new Vec3((double)pos.getX() + 0.51, (double)pos.getY() + 0.51, (double)pos.getZ() + 0.51));
    }

    public static Vec3 getVectorForRotation(Rotation rotation) {
        float yawCos = MathHelper.cos(-rotation.getYaw() * ((float)Math.PI / 180) - (float)Math.PI);
        float yawSin = MathHelper.sin(-rotation.getYaw() * ((float)Math.PI / 180) - (float)Math.PI);
        float pitchCos = -MathHelper.cos(-rotation.getPitch() * ((float)Math.PI / 180));
        float pitchSin = MathHelper.sin(-rotation.getPitch() * ((float)Math.PI / 180));
        return new Vec3(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
    }

    public static double getRotationDifference(final Rotation rotation) {
        return RotationComponent.lastServerRotations == null ? 0D : getRotationDifference(rotation, RotationComponent.lastServerRotations);
    }

    public static float getAngleDifference(final float a, final float b) {
        return ((((a - b) % 360F) + 540F) % 360F) - 180F;
    }

    public static double getRotationDifference(final Rotation a, final Vector2f b) {
        return Math.hypot(getAngleDifference(a.getYaw(), b.getX()), a.getPitch() - b.getY());
    }
}
