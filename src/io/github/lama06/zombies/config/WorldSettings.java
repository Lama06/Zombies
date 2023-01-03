package io.github.lama06.zombies.config;

import io.github.lama06.zombies.util.EntityPosition;

import java.util.List;
import java.util.Map;

public record WorldSettings(
        String startArea,
        EntityPosition spawnPosition,
        PowerSwitch powerSwitch,
        Map<String, Door> doors,
        Map<String, Window> windows,
        Map<String, WeaponShop> weaponShops,
        Map<String, MonsterSpawnLocation> spawnLocations,
        List<SpawnRate> spawnRates
) {
    @SuppressWarnings("unused")
    public static boolean isValid(
            final String startArea,
            final EntityPosition spawnPosition,
            final PowerSwitch powerSwitch,
            final Map<String, Door> doors,
            final Map<String, Window> windows,
            final Map<String, WeaponShop> weaponShops,
            final Map<String, MonsterSpawnLocation> spawnLocations,
            final List<SpawnRate> spawnRates
    ) {
        return startArea != null && !startArea.isEmpty() && spawnPosition != null && windows != null &&
                !windows.isEmpty() && spawnRates != null && !spawnRates.isEmpty();
    }

    public WorldSettings {
        if (!isValid(startArea, spawnPosition, powerSwitch, doors, windows, weaponShops, spawnLocations, spawnRates)) {
            throw new IllegalArgumentException();
        }
    }
}
