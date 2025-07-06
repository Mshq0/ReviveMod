package ru.mshq.revive.custom.items;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;

public class BloodPotion {
    public static final String BLOOD_POTION_TAG = "revive:blood_potion";

    public static ItemStack createBloodPotion() {
        ItemStack potion = new ItemStack(Items.POTION);

        MutableText name = Text.literal("Бутылочка крови").styled(style -> style.withItalic(false));
        potion.set(DataComponentTypes.CUSTOM_NAME, name);

        NbtCompound nbt = new NbtCompound();
        nbt.putString("ReviveType", BLOOD_POTION_TAG);

        PotionContentsComponent potionContents = new PotionContentsComponent(
                Optional.empty(),
                Optional.of(0x8B0000),
                List.of(),
                Optional.of(" ")
        );

        potion.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
        potion.set(DataComponentTypes.POTION_CONTENTS, potionContents);

        return potion;
    }

    public static boolean isBlood(ItemStack stack) {
        NbtComponent data = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (data == null) {
            return false;
        }

        Optional<String> reviveType = data.copyNbt().getString("ReviveType");
        return reviveType.map(s -> s.equals(BLOOD_POTION_TAG)).orElse(false);
    }
}
