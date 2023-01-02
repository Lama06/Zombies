package io.github.lama06.zombies;

import com.google.gson.*;
import io.github.lama06.zombies.command.CommandNode;
import io.github.lama06.zombies.config.InvalidJsonException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ZombiesPlugin extends JavaPlugin {
    public static ZombiesPlugin getInstance() {
        return JavaPlugin.getPlugin(ZombiesPlugin.class);
    }

    private Map<World, ZombiesWorld> worlds;

    @Override
    public void onEnable() {
        createDataFolder();
        worlds = loadWorlds();
        CommandNode.registerCommand("zombies", new ZombiesCommand());
    }

    @Override
    public void onDisable() {
        try {
            saveWorlds();
        } catch (final IOException e) {
            throw new RuntimeException("failed to save worlds", e);
        }
    }

    private void createDataFolder() {
        final var dataFolder = getDataFolder().toPath();
        try {
            Files.createDirectories(dataFolder);
        } catch (final IOException e) {
            throw new RuntimeException("failed to create data folder", e);
        }
    }

    private Path getWorldsConfigPath() {
        return Path.of(getDataFolder().toPath().toString(), "worlds.json");
    }

    private Map<World, ZombiesWorld> loadWorlds() {
        final var worlds = new HashMap<World, ZombiesWorld>();

        final var worldsConfigPath = getWorldsConfigPath();

        if (!Files.exists(worldsConfigPath)) {
            return worlds;
        }

        final String worldsConfigText;
        try {
            worldsConfigText = Files.readString(worldsConfigPath);
        } catch (final IOException e) {
            throw new RuntimeException("failed to read worlds.json file", e);
        }
        final JsonElement worldsConfigJson;
        try {
            worldsConfigJson = JsonParser.parseString(worldsConfigText);
        } catch (final JsonParseException e) {
            throw new RuntimeException("invalid json in worlds.json configuration file", e);
        }
        if (!worldsConfigJson.isJsonObject()) {
            throw new IllegalArgumentException("worlds.json file must contain root object");
        }

        for (final var worldName : worldsConfigJson.getAsJsonObject().keySet()) {
            final var world = Bukkit.getWorld(worldName);
            if (world == null) {
                Bukkit.getLogger().warning("worlds.json file contains entry for non-existent world");
                continue;
            }

            try {
                final var zombiesWorld = new ZombiesWorld(world, worldsConfigJson.getAsJsonObject().get(worldName));
                worlds.put(world, zombiesWorld);
            } catch (final InvalidJsonException e) {
                Bukkit.getLogger()
                        .warning("Failed to load world %s. Reason: %s. Path: %s".formatted(worldName,
                                e.getPath(),
                                e.getMessage()
                        ));
            }
        }

        return worlds;
    }

    public void saveWorlds() throws IOException {
        final var worldsConfigPath = getWorldsConfigPath();

        if (!Files.exists(worldsConfigPath)) {
            try {
                Files.createFile(worldsConfigPath);
            } catch (final IOException e) {
                throw new IOException("failed to create the config file", e);
            }
        }

        final var worldsConfigJson = new JsonObject();
        for (final var worldEntry : worlds.entrySet()) {
            final var worldConfig = worldEntry.getValue().serialize();
            worldsConfigJson.add(worldEntry.getKey().getName(), worldConfig);
        }

        final var gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        final var worldsConfigText = gson.toJson(worldsConfigJson);

        try {
            Files.writeString(worldsConfigPath, worldsConfigText);
        } catch (final IOException e) {
            throw new IOException("failed to write to config file", e);
        }
    }

    public Map<World, ZombiesWorld> getWorlds() {
        return worlds;
    }
}
