package ru.mshq.revive.events;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Set;

import static ru.mshq.revive.Revive.config;

public class EndServerTick {
    public static void execute(MinecraftServer server) {
        if (config.getSpectatorMovement()) {
            return;
        }

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            if (player.isSpectator() && !player.hasPermissionLevel(2)) {
                player.setVelocity(0, 0, 0);
                player.teleport(
                        player.getWorld(),
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        Set.of(),
                        player.getYaw(),
                        player.getPitch(),
                        true
                );
                player.sendMessage(
                        Text.of(config.getSpectatorTitle()),
                        true
                );
            }
        }
    }
}
