package ru.mshq.revive;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mshq.revive.config.Config;
import ru.mshq.revive.events.AttackEntity;
import ru.mshq.revive.events.UseBlock;
import ru.mshq.revive.events.UseItem;

import java.nio.file.Path;

public class Revive implements ModInitializer {
    public static Config config;
    public static final Logger LOGGER = LoggerFactory.getLogger(Revive.class);

    @Override
    public void onInitialize() {
        loadConfig();
        loadEvents();

        LOGGER.info("Revive mod initialized");
    }

    private void loadConfig() {
        if (config != null) return;

        Path configFile = FabricLoader.getInstance().getConfigDir().resolve("revive.toml");
        config = Config.load(configFile);
    }

    private void loadEvents() {
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
