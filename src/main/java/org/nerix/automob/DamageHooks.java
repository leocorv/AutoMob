package org.nerix.automob;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public final class DamageHooks {
    public static void register() {
        // avant application des dégâts
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            onDamage(entity, source, amount);
            return true; // ne bloque pas le dégât
        });

        // après la mort
        ServerLivingEntityEvents.AFTER_DEATH.register(DamageHooks::onDeath);
    }

    private static MobEntity controlledAttacker(DamageSource source) {
        if (source.getAttacker() instanceof MobEntity mob
                && ControlledMobRegistry.get().isControlled(mob)) {
            return mob;
        }
        return null;
    }

    private static void onDamage(LivingEntity target, DamageSource src, float amount) {
        if (target.getWorld().isClient()) return;

        // Les statistiques AutoMob ne doivent prendre en compte que les dégâts
        // infligés par un mob réellement enregistré comme contrôlé.
        if (target instanceof ServerPlayerEntity && controlledAttacker(src) != null) {
            LiveStats.get().addPlayerDamage(amount);
            LiveStats.get().addReward(amount);
        }
    }

    private static void onDeath(LivingEntity entity, DamageSource src) {
        if (entity.getWorld().isClient()) return;

        if (entity instanceof ServerPlayerEntity) {
            if (controlledAttacker(src) != null) {
                LiveStats.get().addReward(50.0);
                LiveStats.get().incAgentKill();
            }
            return;
        }

        if (entity instanceof MobEntity mob && ControlledMobRegistry.get().isControlled(mob)) {
            LiveStats.get().incAgentDeath();
            ControlledMobRegistry.get().unregister(mob);
        }
    }
}
