package io.github.lama06.zombies;

import com.google.gson.JsonElement;
import io.github.lama06.zombies.command.CommandNode;
import io.github.lama06.zombies.config.InvalidJsonException;
import io.github.lama06.zombies.config.InvalidConfigException;
import io.github.lama06.zombies.config.WorldSettings;
import io.github.lama06.zombies.config.WorldSettingsConfig;
import org.bukkit.World;

public final class ZombiesWorld {
    private final World world;
    private final WorldSettingsConfig config = new WorldSettingsConfig();
    private ZombiesGame currentGame;

    public ZombiesWorld(final World world) {
        this.world = world;
    }

    public ZombiesWorld(final World world, final JsonElement json) throws InvalidJsonException {
        this.world = world;
        config.deserialize(json);
    }

    public WorldSettings getSettings() throws InvalidConfigException {
        return config.getValue();
    }

    public JsonElement serialize() {
        return config.serialize();
    }

    public CommandNode getConfigCommandNode() {
        return config.getCommand();
    }

    public World getWorld() {
        return world;
    }
}
