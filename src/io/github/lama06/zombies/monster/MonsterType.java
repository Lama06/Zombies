package io.github.lama06.zombies.monster;

import io.github.lama06.zombies.util.EntityPosition;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public record MonsterType<T extends Monster<T, E>, E extends Entity>(
        String name, MonsterSpawnLocationType spawnLocation, Class<E> entityType,
        MonsterCreator<T, E> creator
) {
    private static final Set<MonsterType<?, ?>> TYPES = new HashSet<>();

    public static Set<MonsterType<?, ?>> getTypes() {
        return TYPES;
    }

    public static Optional<MonsterType<?, ?>> getByName(final String name) {
        return TYPES.stream().filter(type -> type.name.equals(name)).findFirst();
    }

    public MonsterType {
        TYPES.add(this);
    }

    @FunctionalInterface
    public interface MonsterCreator<T extends Monster<T, E>, E extends Entity> {
        T createMonster(MonsterType<T, E> type, World world, EntityPosition position);
    }
}