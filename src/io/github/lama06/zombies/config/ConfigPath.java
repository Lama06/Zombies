package io.github.lama06.zombies.config;

import java.util.Objects;

public record ConfigPath(String path) {
    public static final ConfigPath ROOT = new ConfigPath("");

    public ConfigPath {
        Objects.requireNonNull(path);
    }

    public ConfigPath append(final String text) {
        return new ConfigPath(path.isEmpty() ? text : "%s.%s".formatted(path, text));
    }
}
