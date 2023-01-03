package io.github.lama06.zombies.config;

public final class WindowConfig extends CompoundConfig<Window> {
    private final StringConfig area = registerConfig("area", new StringConfig());
    private final EntityPositionConfig spawnLocation = registerConfig("spawnLocation", new EntityPositionConfig());
    private final BlockAreaConfig blocks = registerConfig("blocks", new BlockAreaConfig());

    @Override
    protected void setValueImplCompound(final Window newValue) {
        area.setValue(newValue.area());
        spawnLocation.setValue(newValue.spawnLocation());
        blocks.setValue(newValue.blocks());
    }

    @Override
    protected Window getValueImplCompound(final ConfigPath path) throws InvalidConfigException {
        final var area = this.area.getValue(path.append("area"));
        final var spawnLocation = this.spawnLocation.getValue(path.append("spawnLocation"));
        final var blocks = this.blocks.getValue(path.append("blocks"));

        if (!Window.isValid(area, spawnLocation, blocks)) {
            throw new InvalidConfigException(path);
        }

        return new Window(area, spawnLocation, blocks);
    }
}
