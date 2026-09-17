package org.nerix.automob;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class Automob implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            ControlledSpawner.get().tick(server);
            ControllerTick.get().tick(server);
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, env) ->
                NeuroCommands.register(dispatcher));

        DamageHooks.register();

        System.out.println("[AutoMob] initialized (server-only experimental local-policy prototype).");
    }
}
