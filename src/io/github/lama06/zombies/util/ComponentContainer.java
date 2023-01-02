package io.github.lama06.zombies.util;

import java.util.HashMap;
import java.util.Map;

public final class ComponentContainer {
    private final Map<Class<?>, Object> components = new HashMap<>();

    public void addComponent(final Object component) {
        components.put(component.getClass(), component);
    }

    public boolean hasComponent(final Class<?> target) {
        for (final var type : components.keySet()) {
            if (type.equals(target)) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public <T> T getComponent(final Class<T> type) {
        for (final var component : components.entrySet()) {
            if (component.getKey() == type) {
                return (T) component.getValue();
            }
        }

        return null;
    }

    public Map<Class<?>, Object> getComponents() {
        return components;
    }
}