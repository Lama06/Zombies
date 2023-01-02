package io.github.lama06.zombies.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.github.lama06.zombies.command.CommandException;
import io.github.lama06.zombies.util.Named;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import java.util.function.Function;

public final class NamedConfig<T extends Named> extends PrimitiveConfig<T> {
    private final Function<String, T> getByName;

    public NamedConfig(final Function<String, T> getByName) {
        this.getByName = getByName;
    }

    @Override
    protected T deserializeImplPrimitive(final ConfigPath path, final JsonElement json) throws InvalidJsonException {
        if (!(json instanceof JsonPrimitive primitive) || !primitive.isString()) {
            throw new InvalidJsonException(path, "expected string or null");
        }

        final var value = getByName.apply(primitive.getAsString());
        if (value == null) {
            throw new InvalidJsonException(path, "unexpected value");
        }

        return value;
    }

    @Override
    protected JsonElement serializeImplPrimitive() {
        return new JsonPrimitive(value.name());
    }

    @Override
    protected Component asComponentImplPrimitive() {
        return Component.text(value.name());
    }

    @Override
    protected T parseCommandInputImplPrimitive(final CommandSender sender, final String[] args) throws CommandException {
        final var name = String.join(" ", args);
        final var value = getByName.apply(name);
        if (value == null) {
            throw new CommandException("Invalid value: " + name);
        }
        return value;
    }
}
