package io.github.lama06.zombies.config;

import io.github.lama06.zombies.util.BlockArea;
import io.github.lama06.zombies.util.EntityPosition;

public record Window(String area, EntityPosition spawnLocation, BlockArea blocks) {
    public static boolean isValid(final String area, final EntityPosition spawnLocation, final BlockArea blocks) {
        return area != null && !area.isEmpty() && spawnLocation != null && blocks != null && blocks.is2d();
    }

    public Window {
        if (!isValid(area, spawnLocation, blocks)) {
            throw new IllegalArgumentException();
        }
    }
}
