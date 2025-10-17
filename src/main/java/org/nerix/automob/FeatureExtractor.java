package org.nerix.automob;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public final class FeatureExtractor {

    public static float[] featuresFor(MobEntity mob, ServerPlayerEntity target) {
        Vec3d mp = mob.getPos();
        Vec3d tp = target.getPos();
        double dx = tp.x - mp.x;
        double dy = (tp.y + target.getStandingEyeHeight()) - (mp.y + mob.getStandingEyeHeight());
        double dz = tp.z - mp.z;

        double dist = Math.sqrt(dx*dx + dy*dy + dz*dz);
        double nx = safeNorm(dx, dist);
        double ny = safeNorm(dy, dist);
        double nz = safeNorm(dz, dist);

        // vitesses
        Vec3d mv = mob.getVelocity();
        Vec3d tv = target.getVelocity();

        // pv normalisés
        float mobHp = (float) (mob.getHealth() / mob.getMaxHealth());
        float tgtHp = (float) (target.getHealth() / target.getMaxHealth());

        // yaw relatif approximé
        double yawToTarget = Math.atan2(-dx, dz);
        double yawMob = Math.toRadians(mob.getYaw());
        double yawDelta = wrapAngle(yawToTarget - yawMob);

        return new float[] {
                (float) nx, (float) ny, (float) nz,
                (float) clamp01(dist / 32.0),
                (float) mv.x, (float) mv.y, (float) mv.z,
                (float) tv.x, (float) tv.y, (float) tv.z,
                mobHp, tgtHp,
                (float) yawDelta,
                (float) (tp.x - mp.x),
                (float) (tp.z - mp.z),
                1.0f
        };
    }

    public static ServerPlayerEntity nearestPlayer(MobEntity mob) {
        var world = mob.getWorld();
        ServerPlayerEntity best = null;
        double bestD2 = Double.MAX_VALUE;
        for (var p : world.getPlayers()) {
            if (p.isSpectator() || p.isCreative()) continue;
            double d2 = p.squaredDistanceTo(mob);
            if (d2 < bestD2) { bestD2 = d2; best = p; }
        }
        return best;
    }

    private static double safeNorm(double v, double d) { return d > 1e-6 ? v / d : 0.0; }
    private static double clamp01(double v) { return v < 0 ? 0 : (v > 1 ? 1 : v); }
    private static double wrapAngle(double a) {
        while (a <= -Math.PI) a += 2*Math.PI;
        while (a > Math.PI) a -= 2*Math.PI;
        return a;
    }
}
