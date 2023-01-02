package io.github.lama06.zombies.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public final class StringConfig extends PrimitiveConfig<String> {
    @Override
    protected String deserializeImplPrimitive(final ConfigPath path, final JsonElement json) throws InvalidJsonException {
        if (!(json instanceof JsonPrimitive primitive) || primitive.isString()) {
            throw new InvalidJsonException(path, "expected string or null");
        }

        return primitive.getAsString();
    }

    @Override
    protected JsonElement serializeImplPrimitive() {
        return new JsonPrimitive(value);
    }

    @Override
    protected Component asComponentImplPrimitive() {
        return Component.text(value);
    }

    @Override
    protected String parseCommandInputImplPrimitive(final CommandSender sender, final String[] args) {
        return String.join(" ", args);
    }
}
