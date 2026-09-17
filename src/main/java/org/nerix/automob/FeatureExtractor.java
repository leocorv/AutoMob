package org.nerix.automob;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public final class FeatureExtractor {

    public static float[] featuresFor(MobEntity mob, ServerPlayerEntity target) {
        Vec3d mobPos = mob.getPos();
        Vec3d targetPos = target.getPos();
        double dx = targetPos.x - mobPos.x;
        double dy = (targetPos.y + target.getStandingEyeHeight())
                - (mobPos.y + mob.getStandingEyeHeight());
        double dz = targetPos.z - mobPos.z;

        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        double nx = safeNorm(dx, distance);
        double ny = safeNorm(dy, distance);
        double nz = safeNorm(dz, distance);

        Vec3d mobVelocity = mob.getVelocity();
        Vec3d targetVelocity = target.getVelocity();

        float mobHp = mob.getMaxHealth() > 0
                ? mob.getHealth() / mob.getMaxHealth()
                : 0.0f;
        float targetHp = target.getMaxHealth() > 0
                ? target.getHealth() / target.getMaxHealth()
                : 0.0f;

        double yawToTarget = Math.atan2(-dx, dz);
        double mobYaw = Math.toRadians(mob.getYaw());
        double yawDelta = wrapAngle(yawToTarget - mobYaw);

        return new float[] {
                (float) nx, (float) ny, (float) nz,
                (float) clamp01(distance / 32.0),
                (float) mobVelocity.x, (float) mobVelocity.y, (float) mobVelocity.z,
                (float) targetVelocity.x, (float) targetVelocity.y, (float) targetVelocity.z,
                mobHp, targetHp,
                (float) yawDelta,
                (float) dx,
                (float) dz,
                1.0f
        };
    }

    public static ServerPlayerEntity nearestPlayer(MobEntity mob) {
        if (!(mob.getWorld() instanceof ServerWorld world)) {
            return null;
        }

        ServerPlayerEntity best = null;
        double bestDistanceSquared = Double.MAX_VALUE;

        for (ServerPlayerEntity player : world.getPlayers()) {
            if (player.isSpectator() || player.isCreative() || !player.isAlive()) {
                continue;
            }

            double distanceSquared = player.squaredDistanceTo(mob);
            if (distanceSquared < bestDistanceSquared) {
                bestDistanceSquared = distanceSquared;
                best = player;
            }
        }

        return best;
    }

    private static double safeNorm(double value, double distance) {
        return distance > 1e-6 ? value / distance : 0.0;
    }

    private static double clamp01(double value) {
        return value < 0 ? 0 : Math.min(value, 1);
    }

    private static double wrapAngle(double angle) {
        while (angle <= -Math.PI) angle += 2 * Math.PI;
        while (angle > Math.PI) angle -= 2 * Math.PI;
        return angle;
    }
}
