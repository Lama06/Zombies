package io.github.lama06.zombies.config;

import io.github.lama06.zombies.util.EntityPosition;

public record MonsterSpawnLocation(String area, EntityPosition position) {
    public static boolean isValid(final String area, final EntityPosition position) {
        return area != null && position != null;
    }

    public MonsterSpawnLocation {
        if (!isValid(area, position)) {
            throw new IllegalArgumentException();
        }
    }
}
