package org.nerix.automob;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public final class NeuroCommands {
    public static void register(CommandDispatcher<ServerCommandSource> d) {
        d.register(CommandManager.literal("neuro")
                .requires(src -> src.hasPermissionLevel(2))
                .then(CommandManager.literal("stats")
                        .executes(ctx -> {
                            var s = LiveStats.get();
                            ctx.getSource().sendFeedback(
                                    () -> Text.literal(String.format(
                                            "[AutoMob] RPM=%.2f | DPM=%.2f | K/D=%d/%d",
                                            s.rpm(), s.dpm(), s.k(), s.d())), false);
                            return 1;
                        }))
                .then(CommandManager.literal("reset")
                        .executes(ctx -> {
                            LiveStats.get().reset();
                            ctx.getSource().sendFeedback(
                                    () -> Text.literal("[AutoMob] local stats reset."), false);
                            return 1;
                        }))
        );
    }
}
