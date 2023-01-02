package io.github.lama06.zombies.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.github.lama06.zombies.command.CommandException;
import io.github.lama06.zombies.command.Require;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public final class FloatConfig extends PrimitiveConfig<Float> {
    @Override
    protected Float deserializeImplPrimitive(final ConfigPath path, final JsonElement json) throws InvalidJsonException {
        if (!(json instanceof JsonPrimitive primitive) || !primitive.isNumber()) {
            throw new InvalidJsonException(path, "expected float or null");
        }

        return primitive.getAsFloat();
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
    protected Float parseCommandInputImplPrimitive(final CommandSender sender, final String[] args) throws CommandException {
        Require.argsExact(args, 1);
        return Require.float32(args[0]);
    }
}

