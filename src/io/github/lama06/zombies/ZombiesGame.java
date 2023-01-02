package io.github.lama06.zombies;

import org.bukkit.World;

public final class ZombiesGame {
    private final ZombiesWorld zombiesWorld;

    public ZombiesGame(final ZombiesWorld zombiesWorld) {
        this.zombiesWorld = zombiesWorld;
    }

    public ZombiesWorld getZombiesWorld() {
        return zombiesWorld;
    }

    public World getWorld() {
        return zombiesWorld.getWorld();
    }
}
