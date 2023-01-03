package io.github.lama06.zombies.config;

public final class WorldSettingsConfig extends CompoundConfig<WorldSettings> {
    private final StringConfig startArea = registerConfig("startArea", new StringConfig());
    private final EntityPositionConfig spawnPoint = registerConfig("spawnPoint", new EntityPositionConfig());
    private final PowerSwitchConfig powerSwitch = registerConfig("powerSwitch", new PowerSwitchConfig());
    private final NameMapConfig<Door> doors = registerConfig("doors", new NameMapConfig<>(DoorConfig::new));
    private final NameMapConfig<Window> windows = registerConfig("windows", new NameMapConfig<>(WindowConfig::new));
    private final NameMapConfig<WeaponShop> weaponShops =
            registerConfig("weaponShops", new NameMapConfig<>(WeaponShopConfig::new));
    private final ListConfig<SpawnRate> spawnRates =
            registerConfig("spawnRates", new ListConfig<>(SpawnRateConfig::new));

    @Override
    protected WorldSettings getValueImplCompound(final ConfigPath path) throws InvalidConfigException {
        final var startArea = this.startArea.getValue(path.append("startArea"));
        final var spawnPoint = this.spawnPoint.getValue(path.append("spawnPoint"));
        final var powerSwitch = this.powerSwitch.getValue(path.append("powerSwitch"));
        final var doors = this.doors.getValue(path.append("doors"));
        final var windows = this.windows.getValue(path.append("windows"));
        final var weaponShops = this.weaponShops.getValue(path.append("weaponShops"));
        final var spawnRates = this.spawnRates.getValue(path.append("spawnRates"));

        if (!WorldSettings.isValid(startArea, spawnPoint, powerSwitch, doors, windows, weaponShops, spawnRates)) {
            throw new InvalidConfigException(path);
        }

        return new WorldSettings(startArea, spawnPoint, powerSwitch, doors, windows, weaponShops, spawnRates);
    }

    @Override
    protected void setValueImplCompound(final WorldSettings newValue) {
        startArea.setValue(newValue.startArea());
        spawnPoint.setValue(newValue.spawnPosition());
        powerSwitch.setValue(newValue.powerSwitch());
        doors.setValue(newValue.doors());
        windows.setValue(newValue.windows());
        spawnRates.setValue(newValue.spawnRates());
    }
}
