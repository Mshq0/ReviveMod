package ru.mshq.revive.events;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import ru.mshq.revive.effects.SpawnEffect;
import ru.mshq.revive.items.BloodPotion;

import java.util.Set;

public class UseBlock {

    public static ActionResult execute(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        ItemStack heldItem = player.getStackInHand(hand);
        BlockEntity targetBlockEntity = world.getBlockEntity(hitResult.getBlockPos());

        if (!BloodPotion.isBlood(heldItem) || !(targetBlockEntity instanceof SkullBlockEntity skullBlock)) {
            return ActionResult.PASS;
        }

        ProfileComponent profile = skullBlock.getOwner();
        if (profile == null) {
            return ActionResult.PASS;
        }

        String ownerName = profile.gameProfile().getName();
        MinecraftServer server = player.getServer();
        if (server == null) {
            return ActionResult.PASS;
        }

        ServerPlayerEntity targetPlayer = getOnlinePlayer(server, ownerName);
        if (targetPlayer == null || !targetPlayer.isSpectator()) {
            player.sendMessage(Text.literal(ownerName + " невозможно воскресить"), true);
            return ActionResult.PASS;
        }

        world.removeBlock(targetBlockEntity.getPos(), false);
        teleportPlayerToBlock(targetPlayer, targetBlockEntity.getPos());
        if (!revivePlayer(targetPlayer)) {
            return ActionResult.PASS;
        }

        playSound(targetPlayer);
        player.setStackInHand(hand, Items.GLASS_BOTTLE.getDefaultStack());
        SpawnEffect.spawn(targetPlayer);

        return ActionResult.SUCCESS;
    }

    private static ServerPlayerEntity getOnlinePlayer(MinecraftServer server, String playerName) {
        return server.getPlayerManager().getPlayer(playerName);
    }

    private static boolean revivePlayer(ServerPlayerEntity player) {
        return player.isSpectator() && player.changeGameMode(GameMode.SURVIVAL);
    }

    private static void teleportPlayerToBlock(ServerPlayerEntity player, BlockPos blockPos) {
        Vec3d teleportPos = new Vec3d(
                blockPos.getX() + 0.5,
                blockPos.getY(),
                blockPos.getZ() + 0.5
        );

        player.teleport(
                player.getServerWorld(),
                teleportPos.x,
                teleportPos.y,
                teleportPos.z,
                Set.of(),
                player.getYaw(),
                player.getPitch(),
                true
        );
    }

    private static void playSound(ServerPlayerEntity player) {
        ServerWorld serverWorld = player.getServerWorld();
        BlockPos pos = player.getBlockPos();

        Box area = new Box(
                pos.getX() - 20, pos.getY() - 20, pos.getZ() - 20,
                pos.getX() + 20, pos.getY() + 20, pos.getZ() + 20
        );

        SoundEvent goatHornSound = Registries.SOUND_EVENT.get(Identifier.of("minecraft:item.goat_horn.sound.5"));
        for (PlayerEntity nearbyPlayer : serverWorld.getEntitiesByClass(
                PlayerEntity.class, area, p -> true
        )) {
            nearbyPlayer.playSound(
                    goatHornSound
            );
        }
    }
}
