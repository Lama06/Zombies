package io.github.lama06.zombies.config;

public final class DoorConfig extends CoumpoundConfig<Door> {
    private final StringConfig area1 = registerConfig("area1", new StringConfig());
    private final StringConfig area2 = registerConfig("area2", new StringConfig());
    private final BlockPositionConfig activationBlock = registerConfig("activationBlock", new BlockPositionConfig());
    private final IntegerConfig gold = registerConfig("gold", new IntegerConfig());
    private final BlockAreaConfig blocks = registerConfig("blocks", new BlockAreaConfig());
    private final BlockAreaConfig template = registerConfig("template", new BlockAreaConfig());

    @Override
    protected Door getValueImplCompound(final ConfigPath path) throws InvalidConfigException {
        final var area1 = this.area1.getValue(path.append("area1"));
        final var area2 = this.area2.getValue(path.append("area2"));
        final var activationBlock = this.activationBlock.getValue(path.append("activationBlock"));
        final var gold = this.gold.getValue(path.append("gold"));
        if (gold == null) {
            throw new InvalidConfigException(path);
        }
        final var blocks = this.blocks.getValue(path.append("blocks"));
        final var template = this.template.getValue(path.append("template"));

        if (!Door.isValid(area1, area2, activationBlock, gold, blocks, template)) {
            throw new InvalidConfigException(path);
        }

        return new Door(area1, area2, activationBlock, gold, blocks, template);
    }

    @Override
    protected void setValueImplCompound(final Door newValue) {
        area1.setValueImplConfig(newValue.area1());
        area2.setValueImplConfig(newValue.area2());
        activationBlock.setValueImplConfig(newValue.activationBlock());
        gold.setValueImplConfig(newValue.gold());
        blocks.setValueImplConfig(newValue.blocks());
        template.setValueImplConfig(newValue.template());
    }
}
