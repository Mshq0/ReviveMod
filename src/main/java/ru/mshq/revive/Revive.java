package ru.mshq.revive;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mshq.revive.events.AttackEntity;
import ru.mshq.revive.events.UseBlock;
import ru.mshq.revive.events.UseItem;

public class Revive implements ModInitializer {
    public static final String MOD_ID = "revive";
    public static final Logger LOGGER = LoggerFactory.getLogger(Revive.class);

    @Override
    public void onInitialize() {
        LOGGER.info("Revive mod initialized");

        AttackEntityCallback.EVENT.register(
                AttackEntity::execute
        );

        UseBlockCallback.EVENT.register(
                UseBlock::execute
        );

        UseItemCallback.EVENT.register(
                UseItem::execute
        );
    }
}
