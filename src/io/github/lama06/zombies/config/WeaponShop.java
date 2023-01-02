package io.github.lama06.zombies.config;

import io.github.lama06.zombies.weapon.WeaponType;
import io.papermc.paper.math.BlockPosition;

public record WeaponShop(WeaponType<?> weapon, BlockPosition activationBLock, int gold, int refillPrice) {
    public WeaponShop {
        if (!isValid(weapon, activationBLock, gold, refillPrice)) {
            throw new IllegalArgumentException();
        }
    }

    public static boolean isValid(
            final WeaponType<?> weapon,
            final BlockPosition activationBLock,
            final int gold,
            final int refillPrice
    ) {
        return weapon != null && activationBLock != null && gold >= 0 && refillPrice >= 0;
    }
}
