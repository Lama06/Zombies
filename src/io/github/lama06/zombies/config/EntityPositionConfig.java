package io.github.lama06.zombies.config;

import io.github.lama06.zombies.util.EntityPosition;

public final class EntityPositionConfig extends CompoundConfig<EntityPosition> {
    private final FinePositionConfig position = registerConfig("position", new FinePositionConfig());
    private final FloatConfig yaw = registerConfig("yaw", new FloatConfig());
    private final FloatConfig pitch = registerConfig("pitch", new FloatConfig());

    @Override
    protected EntityPosition getValueImplCompound(final ConfigPath path) throws InvalidConfigException {
        final var position = this.position.getValue(path.append("position"));
        if (position == null) {
            throw new InvalidConfigException(path);
        }
        final var yaw = this.yaw.getValue(path.append("yaw"));
        if (yaw == null) {
            throw new InvalidConfigException(path);
        }
        final var pitch = this.pitch.getValue(path.append("pitch"));
        if (pitch == null) {
            throw new InvalidConfigException(path);
        }

        return new EntityPosition(position, yaw, pitch);
    }

    @Override
    protected void setValueImplCompound(final EntityPosition newValue) {
        position.setValue(newValue.position());
        yaw.setValue(newValue.yaw());
        pitch.setValue(newValue.pitch());
    }
}
