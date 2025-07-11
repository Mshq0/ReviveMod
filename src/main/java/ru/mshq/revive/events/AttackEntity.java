package ru.mshq.revive.events;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import ru.mshq.revive.SoundBox;
import ru.mshq.revive.custom.effects.Bleeding;
import ru.mshq.revive.custom.items.BloodPotion;

import java.util.Objects;

import static ru.mshq.revive.Revive.config;

public class AttackEntity {
    public static ActionResult execute(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult ignoredResult) {
        if (!(entity instanceof PlayerEntity targetPlayer) || !hand.equals(Hand.MAIN_HAND)
            || targetPlayer.isSpectator() || targetPlayer.isCreative()) {
            return ActionResult.PASS;
        }

        ItemStack heldItem = player.getStackInHand(Hand.MAIN_HAND);
        ItemStack offHandStack = player.getStackInHand(Hand.OFF_HAND);

        boolean isIronSword = heldItem.getItem() == Items.IRON_SWORD;
        boolean hasGlassBottle = offHandStack.getItem() == Items.GLASS_BOTTLE;

        boolean ateEnchantedGoldenApple = targetPlayer.hasStatusEffect(StatusEffects.REGENERATION) &&
                targetPlayer.hasStatusEffect(StatusEffects.ABSORPTION) &&
                targetPlayer.hasStatusEffect(StatusEffects.RESISTANCE) &&
                targetPlayer.hasStatusEffect(StatusEffects.FIRE_RESISTANCE);

        boolean hasCurseOfVanishing = getLevel(heldItem, Enchantments.VANISHING_CURSE) > 0;
        boolean hasSmiteV = getLevel(heldItem, Enchantments.SMITE) >= 5;

        if (isIronSword && ateEnchantedGoldenApple && hasCurseOfVanishing && hasSmiteV && hasGlassBottle) {
            if (!world.isClient) {
                SoundBox.playAround((ServerWorld) world, player, SoundEvents.ITEM_GLOW_INK_SAC_USE);
            }

            targetPlayer.clearStatusEffects();
            targetPlayer.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.BLINDNESS,
                    100,
                    0,
                    false,
                    true
            ));

            double currentMaxHealth = targetPlayer.getAttributeValue(EntityAttributes.MAX_HEALTH);

            if (currentMaxHealth <= config.getMinHealth()) {
                return ActionResult.SUCCESS;
            } else {
                Objects.requireNonNull(targetPlayer.getAttributeInstance(EntityAttributes.MAX_HEALTH))
                        .setBaseValue(currentMaxHealth - config.getRemoveHealth());

                Bleeding.spawn((ServerPlayerEntity) targetPlayer);
                ItemStack bloodPotion = BloodPotion.createBloodPotion();

                player.setStackInHand(Hand.MAIN_HAND, Items.AIR.getDefaultStack());

                if (offHandStack.getCount() == 1) {
                    player.setStackInHand(Hand.OFF_HAND, bloodPotion);
                } else {
                    offHandStack.decrement(1);
                    player.giveOrDropStack(bloodPotion);
                }
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    public static int getLevel(ItemStack stack, RegistryKey<Enchantment> enchantment) {
        for (RegistryEntry<Enchantment> enchantments : stack.getEnchantments().getEnchantments()){
            if (enchantments.toString().contains(enchantment.getValue().toString())){
                return stack.getEnchantments().getLevel(enchantments);
            }
        }
        return 0;
    }
}
