package io.github.lama06.zombies.config;

public final class WorldSettingsConfig extends CoumpoundConfig<WorldSettings> {
    private final EntityPositionConfig spawnPoint = registerConfig("spawnPoint", new EntityPositionConfig());
    private final PowerSwitchConfig powerSwitch = registerConfig("powerSwitch", new PowerSwitchConfig());
    private final NameMapConfig<DoorConfig, Door> doors = registerConfig("doors", new NameMapConfig<>(DoorConfig::new));

    @Override
    protected WorldSettings getValueImplCompound(final ConfigPath path) throws InvalidConfigException {
        final var spawnPoint = this.spawnPoint.getValue(path.append("spawnPoint"));
        final var powerSwitch = this.powerSwitch.getValueImplConfig(path.append("powerSwitch"));
        final var doors = this.doors.getValue(path.append("doors"));

        if (!WorldSettings.isValid(spawnPoint, powerSwitch, doors)) {
            throw new InvalidConfigException(path);
        }

        return new WorldSettings(spawnPoint, powerSwitch, doors);
    }

    @Override
    protected void setValueImplCompound(final WorldSettings newValue) {
        spawnPoint.setValueImplConfig(newValue.spawnPosition());
        powerSwitch.setValue(newValue.powerSwitch());
        doors.setValueImplConfig(newValue.doors());
    }
}
