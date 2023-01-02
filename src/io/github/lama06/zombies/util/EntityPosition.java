package io.github.lama06.zombies.util;

import io.papermc.paper.math.FinePosition;
import org.bukkit.Location;
import org.bukkit.World;

public record EntityPosition(FinePosition position, float yaw, float pitch) {
    public EntityPosition {
        if (position == null) {
            throw new NullPointerException();
        }
    }

    public Location toLocation(final World world) {
        return new Location(world, position.x(), position.y(), position.z(), yaw, pitch);
    }
}
