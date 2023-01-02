package io.github.lama06.zombies.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.lama06.zombies.command.CommandException;
import io.github.lama06.zombies.command.Require;
import io.papermc.paper.math.FinePosition;
import io.papermc.paper.math.Position;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public final class FinePositionConfig extends PrimitiveConfig<FinePosition> {
    @Override
    protected FinePosition deserializeImplPrimitive(final ConfigPath path, final JsonElement json) throws InvalidJsonException {
        if (!(json instanceof JsonObject object)) throw new InvalidJsonException(path, "expected json object");

        if (!(object.get("x") instanceof JsonPrimitive xJson) || !xJson.isNumber())
            throw new InvalidJsonException(path.append("x"), "expected number");
        if (!(object.get("y") instanceof JsonPrimitive yJson) || !yJson.isNumber())
            throw new InvalidJsonException(path.append("y"), "expected number");
        if (!(object.get("z") instanceof JsonPrimitive zJson) || !zJson.isNumber())
            throw new InvalidJsonException(path.append("z"), "expected number");

        final var x = xJson.getAsDouble();
        final var y = yJson.getAsDouble();
        final var z = zJson.getAsDouble();

        return Position.fine(x, y, z);
    }

    @Override
    protected JsonElement serializeImplPrimitive() {
        final var json = new JsonObject();
        json.addProperty("x", value.x());
        json.addProperty("y", value.y());
        json.addProperty("z", value.z());
        return json;
    }

    @Override
    protected Component asComponentImplPrimitive() {
        return Component.text("%f %f %f".formatted(value.x(), value.y(), value.z()));
    }

    @Override
    protected FinePosition parseCommandInputImplPrimitive(final CommandSender sender, final String[] args) throws CommandException {
        Require.argsExact(args, 3);
        return Require.finePosition(args[0], args[1], args[2]);
    }
}
