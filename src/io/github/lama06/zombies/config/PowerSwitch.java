package io.github.lama06.zombies.config;

import io.papermc.paper.math.BlockPosition;

public record PowerSwitch(int gold, BlockPosition activationBlock) {
    public static boolean isValid(final int gold, final BlockPosition activationBlock) {
        return gold >= 0 && activationBlock != null;
    }

    public PowerSwitch {
        if (!isValid(gold, activationBlock)) {
            throw new IllegalArgumentException();
        }
    }
}
