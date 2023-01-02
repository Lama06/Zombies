package io.github.lama06.zombies.config;

import io.github.lama06.zombies.util.BlockArea;
import io.papermc.paper.math.BlockPosition;

public record Door(
        String area1, String area2, BlockPosition activationBlock, int gold, BlockArea blocks, BlockArea template
) {
    public Door {
        if (!isValid(area1, area2, activationBlock, gold, blocks, template)) {
            throw new IllegalArgumentException();
        }
    }

    public static boolean isValid(
            final String area1,
            final String area2,
            final BlockPosition activationBlock,
            final int gold,
            final BlockArea blocks,
            final BlockArea template
    ) {
        return area1 != null && !area1.isEmpty() && area2 != null && !area2.isEmpty() && !area1.equals(area2) &&
                activationBlock != null && gold >= 0 && blocks != null && template != null;
    }
}
