package io.github.lama06.zombies.config;

public final class PowerSwitchConfig extends CoumpoundConfig<PowerSwitch> {
    private final IntegerConfig gold = registerConfig("gold", new IntegerConfig());
    private final BlockPositionConfig activationBlock = registerConfig("activationBlock", new BlockPositionConfig());

    @Override
    protected PowerSwitch getValueImplCompound(final ConfigPath path) throws InvalidConfigException {
        final var gold = this.gold.getValue(path.append("gold"));
        if (gold == null) {
            throw new InvalidConfigException(path);
        }
        final var activationBlock = this.activationBlock.getValue(path.append("activationBlock"));

        if (!PowerSwitch.isValid(gold, activationBlock)) {
            throw new InvalidConfigException(path);
        }

        return new PowerSwitch(gold, activationBlock);
    }

    @Override
    protected void setValueImplCompound(final PowerSwitch newValue) {
        gold.setValueImplConfig(newValue.gold());
        activationBlock.setValueImplConfig(newValue.activationBlock());
    }
}
