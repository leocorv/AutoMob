package org.nerix.automob;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
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

    private static void onDamage(LivingEntity target, DamageSource src, float amount) {
        if (target.getWorld().isClient()) return;

        if (target instanceof ServerPlayerEntity) {
            // amount ~ demi-coeurs (1 coeur = 2.0)
            LiveStats.get().addPlayerDamage(amount);
            LiveStats.get().addReward(+1.0 * amount); // shaping local
            // TODO gRPC: attribuer la reward exacte au bon agent (mob) côté backend
        } else {
            // TODO si mob "contrôlé", pénalité locale légère si tu veux
            // LiveStats.get().addReward(-0.5 * amount);
        }
    }

    private static void onDeath(LivingEntity entity, DamageSource src) {
        if (entity.getWorld().isClient()) return;

        if (entity instanceof ServerPlayerEntity) {
            LiveStats.get().addReward(+50.0);
            LiveStats.get().incAgentKill();
            // TODO gRPC: reward kill au mob agent impliqué
        } else {
            LiveStats.get().incAgentDeath();
        }
    }
}
