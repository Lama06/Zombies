package io.github.lama06.zombies.weapon;

import io.github.lama06.zombies.ZombiesGame;
import io.github.lama06.zombies.ZombiesPlayer;
import io.github.lama06.zombies.util.Named;

import java.util.HashSet;
import java.util.Set;

public record WeaponType<T extends Weapon<T>>(String name, String displayName, WeaponCreator<T> creator)
        implements Named {
    private static final Set<WeaponType<?>> TYPES = new HashSet<>();

    public Set<WeaponType<?>> getTypes() {
        return TYPES;
    }

    public static WeaponType<?> getByName(final String name) {
        return TYPES.stream().filter(type -> type.name.equals(name)).findFirst().orElse(null);
    }

    public WeaponType {
        TYPES.add(this);
    }

    public interface WeaponCreator<T extends Weapon<T>> {
        T createWeapon(ZombiesGame game, ZombiesPlayer player, WeaponType<T> type);
    }
}