package io.github.lama06.zombies.config;

import io.github.lama06.zombies.monster.MonsterType;

public final class SpawnRateConfig extends CompoundConfig<SpawnRate> {
    private final IntegerConfig delay = registerConfig("delay", new IntegerConfig());
    private final IntegerConfig monsters = registerConfig("monsters", new IntegerConfig());
    private final ListConfig<MonsterType<?, ?>> distribution =
            registerConfig("distribution", new ListConfig<>(() -> new NamedConfig<>(MonsterType::getByName)));
    private final NamedConfig<MonsterType<?, ?>> boss =
            registerConfig("boss", new NamedConfig<>(MonsterType::getByName));

    @Override
    protected void setValueImplCompound(final SpawnRate newValue) {
        delay.setValue(newValue.delay());
        monsters.setValue(newValue.monsters());
        distribution.setValue(newValue.distribution());
        boss.setValue(newValue.boss());
    }

    @Override
    protected SpawnRate getValueImplCompound(final ConfigPath path) throws InvalidConfigException {
        final var delay = this.delay.getValue(path.append("delay"));
        if (delay == null) {
            throw new InvalidConfigException(path);
        }
        final var monsters = this.monsters.getValue(path.append("monsters"));
        if (monsters == null) {
            throw new InvalidConfigException(path);
        }
        final var distribution = this.distribution.getValue(path.append("distribution"));
        final var boss = this.boss.getValue(path.append("boss"));

        if (!SpawnRate.isValid(delay, monsters, distribution, boss)) {
            throw new InvalidConfigException(path);
        }

        return new SpawnRate(delay, monsters, distribution, boss);
    }
}
