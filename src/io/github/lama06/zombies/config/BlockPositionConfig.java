package io.github.lama06.zombies.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.lama06.zombies.command.CommandException;
import io.github.lama06.zombies.command.Require;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.Position;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public final class BlockPositionConfig extends PrimitiveConfig<BlockPosition> {
    @Override
    protected BlockPosition deserializeImplPrimitive(final ConfigPath path, final JsonElement json)
            throws InvalidJsonException {
        if (!(json instanceof JsonObject jsonObject))
            throw new InvalidJsonException(path, "expected object or null");
        if (!(jsonObject.get("x") instanceof JsonPrimitive xJson) || !xJson.isNumber())
            throw new InvalidJsonException(path.append("x"), "expected number");
        if (!(jsonObject.get("y") instanceof JsonPrimitive yJson) || !yJson.isNumber())
            throw new InvalidJsonException(path.append("y"), "expected number");
        if (!(jsonObject.get("z") instanceof JsonPrimitive zJson) || !zJson.isNumber())
            throw new InvalidJsonException(path.append("z"), "expected number");

        final var x = xJson.getAsInt();
        final var y = yJson.getAsInt();
        final var z = zJson.getAsInt();

        return Position.block(x, y, z);
    }

    @Override
    protected JsonElement serializeImplPrimitive() {
        final var json = new JsonObject();
        json.addProperty("x", value.blockX());
        json.addProperty("y", value.blockY());
        json.addProperty("z", value.blockZ());
        return json;
    }

    @Override
    protected Component asComponentImplPrimitive() {
        return Component.text("%d %d %d".formatted(value.blockX(), value.blockY(), value.blockZ()));
    }

    @Override
    protected BlockPosition parseCommandInputImplPrimitive(final CommandSender sender, final String[] args)
            throws CommandException {
        Require.argsExact(args, 3);
        return Require.blockPosition(args[0], args[1], args[2]);
    }
}
