package org.nerix.automob;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public final class ControllerTick {
    private static final ControllerTick I = new ControllerTick();
    public static ControllerTick get() { return I; }

    // Pour l’instant, policy locale (dummy). Ensuite: gRPC.
    private final Policy policy = new DummyPolicy();

    public void tick(MinecraftServer server) {
        var world = server.getOverworld();
        if (world == null) return;

        var toControl = new ArrayList<MobEntity>();
        // Parcours par registre (plus perf)
        for (var id : ControlledMobRegistry.get().ids()) {
            var e = world.getEntity(id);
            if (e instanceof MobEntity m && !m.isRemoved()) toControl.add(m);
        }

        for (MobEntity mob : toControl) {
            var target = FeatureExtractor.nearestPlayer(mob);
            if (target == null) continue;

            float[] feat = FeatureExtractor.featuresFor(mob, target);
            Action act = policy.act(feat);

            applyMovement(mob, act);
            // TODO: attaques / tir à brancher plus tard
        }
    }

    private void applyMovement(MobEntity mob, Action a) {
        double speed = 1.10;
        Vec3d pos = mob.getPos();
        Vec3d dir = new Vec3d(a.moveDx(), 0, a.moveDz());
        if (dir.lengthSquared() < 1e-4) return;

        Vec3d target = pos.add(dir.normalize().multiply(2.5));
        mob.getNavigation().startMovingTo(target.x, target.y, target.z, speed);
    }
}
