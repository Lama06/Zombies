package io.github.lama06.zombies.config;

import io.github.lama06.zombies.util.BlockArea;

public final class BlockAreaConfig extends CoumpoundConfig<BlockArea> {
    private final BlockPositionConfig position1 = registerConfig("position1", new BlockPositionConfig());
    private final BlockPositionConfig position2 = registerConfig("position2", new BlockPositionConfig());

    @Override
    public BlockArea getValueImplCompound(final ConfigPath path) throws InvalidConfigException {
        final var position1 = this.position1.getValue(path.append("position1"));
        final var position2 = this.position2.getValue(path.append("position2"));

        if (position1 == null || position2 == null) {
            throw new InvalidConfigException(path);
        }

        return new BlockArea(position1, position2);
    }

    @Override
    public void setValueImplCompound(final BlockArea newValue) {
        position1.setValueImplConfig(newValue.position1());
        position2.setValueImplConfig(newValue.position2());
    }
}
