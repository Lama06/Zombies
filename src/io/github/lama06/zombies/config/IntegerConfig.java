package io.github.lama06.zombies.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.github.lama06.zombies.command.CommandException;
import io.github.lama06.zombies.command.Require;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public final class IntegerConfig extends PrimitiveConfig<Integer> {
    @Override
    protected JsonElement serializeImplPrimitive() {
        return new JsonPrimitive(value);
    }

    @Override
    protected Integer deserializeImplPrimitive(final ConfigPath path, final JsonElement json) throws InvalidJsonException {
        if (!(json instanceof JsonPrimitive primitive) || !primitive.isNumber()) {
            throw new InvalidJsonException(path, "expected integer or null");
        }

        return primitive.getAsInt();
    }

    @Override
    protected Component asComponentImplPrimitive() {
        return Component.text(value);
    }

    @Override
    protected Integer parseCommandInputImplPrimitive(final CommandSender sender, final String[] args) throws CommandException {
        Require.argsExact(args, 1);
        return Require.integer(args[0]);
    }
}
