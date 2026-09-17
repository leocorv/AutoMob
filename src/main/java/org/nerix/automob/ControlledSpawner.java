package org.nerix.automob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public final class ControlledSpawner {
    private static final ControlledSpawner I = new ControlledSpawner();
    public static ControlledSpawner get() { return I; }

    private static final int TICK_PERIOD = 40;   // toutes 2s
    private static final int RING_MIN    = 24;
    private static final int RING_MAX    = 40;

    private long lastTick = 0;

    public void tick(MinecraftServer server) {
        var world = server.getOverworld();
        if (world == null) return;

        long now = world.getTime();
        if ((now - lastTick) < TICK_PERIOD) return;
        lastTick = now;

        // pression adaptative basée sur dégâts/min
        double dpm = LiveStats.get().dpm();
        int base = 4;
        int extra = (dpm < 8) ? 6 : (dpm < 16 ? 3 : 0);

        for (ServerPlayerEntity p : server.getPlayerManager().getPlayerList()) {
            int want = base + extra;
            int have = countNearControlledMobs(world, p, 48);
            int toSpawn = Math.max(0, want - have);

            for (int i = 0; i < toSpawn; i++) {
                BlockPos pos = pickSpawnPosRing(world, p.getBlockPos());
                if (pos == null) continue;

                MobEntity mob = (MobEntity) EntityType.SKELETON.spawn(world, pos, SpawnReason.EVENT);
                if (mob == null) continue;

                mob.setYaw(world.getRandom().nextFloat() * 360f);
                mob.addCommandTag("automob_controlled");
                ControlledMobRegistry.get().register(mob);
            }
        }
    }

    private int countNearControlledMobs(World world, ServerPlayerEntity p, int radius) {
        Box box = Box.from(p.getPos()).expand(radius);
        return world.getEntitiesByClass(
                MobEntity.class,
                box,
                ControlledMobRegistry.get()::isControlled
        ).size();
    }

    private BlockPos pickSpawnPosRing(World world, BlockPos center) {
        var rand = world.getRandom();
        for (int tries = 0; tries < 24; tries++) {
            double ang = rand.nextDouble() * Math.PI * 2.0;
            int r = rand.nextBetween(RING_MIN, RING_MAX);
            int x = center.getX() + (int) Math.round(Math.cos(ang) * r);
            int z = center.getZ() + (int) Math.round(Math.sin(ang) * r);

            int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
            BlockPos pos = new BlockPos(x, y, z);

            boolean hasSolidBelow = world.getBlockState(pos.down()).isSolid();
            int sky = world.getLightLevel(LightType.SKY, pos);

            if (hasSolidBelow && sky < 12) {
                return pos;
            }
        }
        return null;
    }
}
