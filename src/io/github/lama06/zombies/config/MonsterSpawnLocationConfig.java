package io.github.lama06.zombies.config;

public final class MonsterSpawnLocationConfig extends CompoundConfig<MonsterSpawnLocation> {
    private final StringConfig area = registerConfig("area", new StringConfig());
    private final EntityPositionConfig position = registerConfig("position", new EntityPositionConfig());

    @Override
    protected void setValueImplCompound(final MonsterSpawnLocation newValue) {
        area.setValue(newValue.area());
        position.setValue(newValue.position());
    }

    @Override
    protected MonsterSpawnLocation getValueImplCompound(final ConfigPath path) throws InvalidConfigException {
        final var area = this.area.getValue(path.append("area"));
        final var position = this.position.getValue(path.append("position"));

        if (!MonsterSpawnLocation.isValid(area, position)) {
            throw new InvalidConfigException(path);
        }

        return new MonsterSpawnLocation(area, position);
    }
}
