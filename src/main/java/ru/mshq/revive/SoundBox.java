package ru.mshq.revive;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class SoundBox {
    public static void playAround(ServerWorld serverWorld, PlayerEntity player, SoundEvent soundEvent) {
        BlockPos pos = player.getBlockPos();

        Box area = new Box(
                pos.getX() - 20, pos.getY() - 20, pos.getZ() - 20,
                pos.getX() + 20, pos.getY() + 20, pos.getZ() + 20
        );

        for (PlayerEntity nearbyPlayer : serverWorld.getEntitiesByClass(
                PlayerEntity.class, area, p -> true
        )) {
            nearbyPlayer.playSound(
                    soundEvent
            );
        }
    }
}
