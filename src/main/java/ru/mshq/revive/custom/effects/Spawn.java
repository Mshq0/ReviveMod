package ru.mshq.revive.custom.effects;

import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Colors;

import java.util.Objects;

public class Spawn {
    private static final double RADIUS = 1;

    public static void spawn(ServerPlayerEntity player) {
        ServerWorld world = Objects.requireNonNull(player.getServer()).getOverworld();

        DustParticleEffect effect = new DustParticleEffect(Colors.CYAN, 1);

        for (double y = 0; y <= 23; y += 0.1) {
            double x = RADIUS * Math.cos(y);
            double z = RADIUS * Math.sin(y);

            world.spawnParticles(
                    effect,
                    player.getX() + x, player.getY() + (y / 10), player.getZ() + z,
                    1,
                    0, 0, 0,
                    0.01
            );
            
        }
    }
}
