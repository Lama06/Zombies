package io.github.lama06.zombies.config;

import io.github.lama06.zombies.util.EntityPosition;

import java.util.Map;

public record WorldSettings(EntityPosition spawnPosition, PowerSwitch powerSwitch, Map<String, Door> doors) {
    public static boolean isValid(
            final EntityPosition spawnPosition,
            final PowerSwitch powerSwitch,
            final Map<String, Door> doors
    ) {
        return spawnPosition != null && doors != null;
    }

    public WorldSettings {
        if (!isValid(spawnPosition, powerSwitch, doors)) {
            throw new IllegalArgumentException();
        }
    }
}
