package io.github.lama06.zombies.config;

import com.google.gson.JsonElement;
import io.github.lama06.zombies.command.CommandException;
import io.github.lama06.zombies.command.CommandNode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public abstract class PrimitiveConfig<T> extends Config<T> {
    protected T value = null;

    protected abstract T deserializeImplPrimitive(ConfigPath path, JsonElement json) throws InvalidJsonException;

    protected abstract JsonElement serializeImplPrimitive();

    protected abstract Component asComponentImplPrimitive();

    protected abstract T parseCommandInputImplPrimitive(final CommandSender sender, final String[] args)
            throws CommandException;

    @Override
    protected final JsonElement serializeImplConfig() {
        return serializeImplPrimitive();
    }

    @Override
    protected final void deserializeImplConfig(final ConfigPath path, final JsonElement json) throws
            InvalidJsonException {
        value = deserializeImplPrimitive(path, json);
    }

    @Override
    protected final T getValueImplConfig(final ConfigPath path) {
        return value;
    }

    @Override
    protected final void setValueImplConfig(final T data) {
        value = data;
    }

    @Override
    protected final List<Component> toComponentsImplConfig() {
        return List.of(asComponentImplPrimitive());
    }

    @Override
    protected final CommandNode createCommandImplConfig() {
        return new CommandNode() {
            @Override
            public void execute(final CommandSender sender, final String[] args) throws CommandException {
                value = parseCommandInputImplPrimitive(sender, args);
                sender.sendMessage(Component.text("The value of this setting has been successfully changed")
                        .color(NamedTextColor.GREEN));
            }

            @Override
            public List<String> tabComplete(final CommandSender sender, final String[] args) {
                return Collections.emptyList(); // TODO
            }
        };
    }
}
