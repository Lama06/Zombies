package io.github.lama06.zombies.config;

import io.github.lama06.zombies.weapon.WeaponType;

public final class WeaponShopConfig extends CompoundConfig<WeaponShop> {
    private final NamedConfig<WeaponType<?>> weapon =
            registerConfig("weapon", new NamedConfig<>(WeaponType::getByName));
    private final BlockPositionConfig activationBlock = registerConfig("activationBlock", new BlockPositionConfig());
    private final IntegerConfig gold = registerConfig("gold", new IntegerConfig());
    private final IntegerConfig refillPrice = registerConfig("refillPrice", new IntegerConfig());

    @Override
    protected WeaponShop getValueImplCompound(final ConfigPath path) throws InvalidConfigException {
        final var weapon = this.weapon.getValue(path.append("weapon"));
        final var activationBlock = this.activationBlock.getValue(path.append("activationBlock"));
        final var gold = this.gold.getValue(path.append("gold"));
        if (gold == null) {
            throw new InvalidConfigException(path);
        }
        final var refillPrice = this.refillPrice.getValue(path.append("refillPrice"));
        if (refillPrice == null) {
            throw new InvalidConfigException(path);
        }

        if (!WeaponShop.isValid(weapon, activationBlock, gold, refillPrice)) {
            throw new InvalidConfigException(path);
        }

        return new WeaponShop(weapon, activationBlock, gold, refillPrice);
    }

    @Override
    protected void setValueImplCompound(final WeaponShop newValue) {
        weapon.setValue(newValue.weapon());
        activationBlock.setValue(newValue.activationBLock());
        gold.setValue(newValue.gold());
        refillPrice.setValue(newValue.refillPrice());
    }
}
