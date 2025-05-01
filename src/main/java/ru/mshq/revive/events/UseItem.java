package ru.mshq.revive.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import ru.mshq.revive.items.BloodPotion;

public class UseItem {
    public static ActionResult execute(PlayerEntity player, World world, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (BloodPotion.isBlood(stack)) {
            return ActionResult.FAIL;
        }

        return ActionResult.PASS;
    }
}
