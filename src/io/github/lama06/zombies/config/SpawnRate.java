package io.github.lama06.zombies.config;

import io.github.lama06.zombies.monster.MonsterType;

import java.util.List;

public record SpawnRate(int delay, int monsters, List<MonsterType<?, ?>> distribution, MonsterType<?, ?> boss) {
    public static boolean isValid(
            final int delay,
            final int monsters,
            final List<MonsterType<?, ?>> distribution,
            final MonsterType<?, ?> boss
    ) {
        return delay >= 1 && monsters >= 1 && distribution != null && !distribution.isEmpty();
    }

    public SpawnRate {
        if (!isValid(delay, monsters, distribution, boss)) {
            throw new IllegalArgumentException();
        }
    }
}
