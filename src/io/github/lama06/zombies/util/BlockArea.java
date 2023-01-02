package io.github.lama06.zombies.util;

import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.Position;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

import java.util.HashSet;
import java.util.Set;

public record BlockArea(BlockPosition position1, BlockPosition position2) {
    public BlockArea {
        if (position1 == null || position2 == null) {
            throw new NullPointerException();
        }
    }

    public int getUpperX() {
        return Math.max(position1.blockX(), position2.blockX());
    }

    public int getUpperY() {
        return Math.max(position1.blockY(), position2.blockY());
    }

    public int getUpperZ() {
        return Math.max(position1.blockZ(), position2.blockZ());
    }

    public int getLowerX() {
        return Math.min(position1.blockX(), position2.blockX());
    }

    public int getLowerY() {
        return Math.min(position1.blockY(), position2.blockY());
    }

    public int getLowerZ() {
        return Math.min(position1.blockZ(), position2.blockZ());
    }

    public BlockPosition getLowerCorner() {
        return Position.block(getLowerX(), getLowerY(), getLowerZ());
    }

    public BlockPosition getUpperCorner() {
        return Position.block(getUpperX(), getUpperY(), getUpperZ());
    }

    public Set<BlockPosition> getBlocks() {
        final Set<BlockPosition> blocks = new HashSet<>();

        final var lowerCorner = getLowerCorner();
        final var upperCorner = getUpperCorner();

        for (var x = lowerCorner.blockX(); x <= upperCorner.blockX(); x++) {
            for (var y = lowerCorner.blockY(); y <= upperCorner.blockX(); y++) {
                for (var z = lowerCorner.blockZ(); z <= upperCorner.blockZ(); z++) {
                    blocks.add(Position.block(x, y, z));
                }
            }
        }

        return blocks;
    }

    public boolean containsBlock(final BlockPosition position) {
        final var lowerCorner = getLowerCorner();
        final var upperCorner = getUpperCorner();

        final var x = position.blockX() >= lowerCorner.blockX() && position.blockX() <= upperCorner.blockX();
        final var y = position.blockY() >= lowerCorner.blockY() && position.blockY() <= upperCorner.blockY();
        final var z = position.blockZ() >= lowerCorner.blockZ() && position.blockZ() <= upperCorner.blockZ();

        return x && y && z;
    }

    public void clone(final World world, final BlockArea destination) {
        if (!hasSameDimensions(destination)) {
            return;
        }

        final var sourceLowerCorner = getLowerCorner();
        final var sourceUpperCorner = getUpperCorner();
        final var destinationLowerCorner = destination.getLowerCorner();

        for (var x = sourceLowerCorner.blockX(); x <= sourceUpperCorner.blockX(); x++) {
            for (var y = sourceLowerCorner.blockY(); y <= sourceUpperCorner.blockY(); y++) {
                for (var z = sourceLowerCorner.blockZ(); z <= sourceUpperCorner.blockZ(); z++) {
                    final var xOffset = x - sourceLowerCorner.blockX();
                    final var yOffset = y - sourceLowerCorner.blockY();
                    final var zOffset = z - sourceLowerCorner.blockZ();

                    world.getBlockAt(destinationLowerCorner.blockX() + xOffset,
                                 destinationLowerCorner.blockY() + yOffset, destinationLowerCorner.blockZ() + zOffset)
                         .setBlockData(world.getBlockData(x, y, z));
                }
            }
        }
    }

    public void fill(final World world, final BlockData data) {
        for (final var block : getBlocks()) {
            block.toLocation(world).getBlock().setBlockData(data);
        }
    }

    public int getHeight() {
        return getUpperX() - getLowerX() + 1;
    }

    public int getWidthX() {
        return getUpperY() - getLowerY() + 1;
    }

    public int getWidthZ() {
        return getUpperZ() - getLowerZ() + 1;
    }

    public boolean hasSameDimensions(final BlockArea other) {
        return getHeight() == other.getHeight() && getWidthX() == other.getWidthX() && getWidthZ() == other.getWidthZ();
    }

    public boolean is2d() {
        return getHeight() == 1 || getWidthX() == 1 || getLowerZ() == 1;
    }
}