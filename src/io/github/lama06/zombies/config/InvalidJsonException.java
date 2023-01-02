package io.github.lama06.zombies.config;

public final class InvalidJsonException extends Exception {
    private final ConfigPath path;

    public InvalidJsonException(final ConfigPath path, final String message) {
        super(message);
        this.path = path;
    }

    public ConfigPath getPath() {
        return path;
    }
}
