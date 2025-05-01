package ru.mshq.revive.effects;

import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class BloodingEffect {
    public static final int BLOOD_COLOR = 0x8B0000;

    public static void spawn(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();

        DustParticleEffect effect = new DustParticleEffect(BLOOD_COLOR, 1.0f);

        world.spawnParticles(
                effect,
                player.getX(),
                player.getY() + 0.5f,
                player.getZ(),
                30,
                0.5, 0.5, 0.5,
                0.1
        );
    }
}
