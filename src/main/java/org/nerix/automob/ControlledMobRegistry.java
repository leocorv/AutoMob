package org.nerix.automob;

import net.minecraft.entity.mob.MobEntity;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class ControlledMobRegistry {
    private static final ControlledMobRegistry I = new ControlledMobRegistry();
    public static ControlledMobRegistry get() { return I; }

    private final Set<UUID> controlled = ConcurrentHashMap.newKeySet();

    public void register(MobEntity mob) {
        if (mob == null || mob.isRemoved()) return;
        controlled.add(mob.getUuid());
    }

    public void unregister(MobEntity mob) {
        if (mob == null) return;
        controlled.remove(mob.getUuid());
    }

    public boolean isControlled(MobEntity mob) {
        return mob != null && controlled.contains(mob.getUuid());
    }

    public Set<UUID> ids() { return Collections.unmodifiableSet(controlled); }
}
