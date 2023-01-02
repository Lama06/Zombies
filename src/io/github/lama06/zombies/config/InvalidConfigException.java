package io.github.lama06.zombies.config;

public final class InvalidConfigException extends Exception {
    private final ConfigPath path;

    public InvalidConfigException(final ConfigPath path, final String message) {
        super(message);
        this.path = path;
    }

    public InvalidConfigException(final ConfigPath path) {
        this(path, null);
    }

    public ConfigPath getPath() {
        return path;
    }
}
