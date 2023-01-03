package io.github.lama06.zombies.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import io.github.lama06.zombies.command.CommandException;
import io.github.lama06.zombies.command.CommandNode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public abstract class Config<T> implements ComponentLike {
    private CommandNode command;

    protected abstract boolean isNullImplConfig();

    protected abstract T getValueImplConfig(ConfigPath path) throws InvalidConfigException;

    protected abstract void setValueImplConfig(T data);

    protected abstract JsonElement serializeImplConfig();

    protected abstract void deserializeImplConfig(ConfigPath path, JsonElement json) throws InvalidJsonException;

    protected abstract List<Component> toComponentsImplConfig();

    protected abstract CommandNode createCommandImplConfig();

    public final boolean isNull() {
        return isNullImplConfig();
    }

    public final T getValue(final ConfigPath path) throws InvalidConfigException {
        if (isNull()) {
            return null;
        }

        return getValueImplConfig(path);
    }

    public final T getValue() throws InvalidConfigException {
        return getValue(ConfigPath.ROOT);
    }

    public final void setValue(final T newValue) {
        setValueImplConfig(newValue);
    }

    public final JsonElement serialize() {
        if (isNull()) {
            return JsonNull.INSTANCE;
        }

        return serializeImplConfig();
    }

    public final void deserialize(final ConfigPath path, final JsonElement json) throws InvalidJsonException {
        if (json instanceof JsonNull) {
            setValue(null);
            return;
        }

        deserializeImplConfig(path, json);
    }

    public final void deserialize(final JsonElement json) throws InvalidJsonException {
        deserialize(ConfigPath.ROOT, json);
    }

    public final List<Component> toComponents() {
        if (isNull()) {
            return List.of(Component.text("null").color(NamedTextColor.RED));
        }

        return Collections.unmodifiableList(toComponentsImplConfig());
    }

    public final CommandNode getCommand() {
        return command == null ? command = new Command() : command;
    }

    @Override
    public final Component asComponent() {
        final var builder = Component.text();
        var first = true;
        for (final var component : toComponents()) {
            if (!first) {
                builder.appendNewline();
            } else {
                first = false;
            }
            builder.append(component);
        }
        return builder.asComponent();
    }

    private final class Command implements CommandNode {
        private final CommandNode command = createCommandImplConfig();

        @Override
        public void execute(final CommandSender sender, final String[] args) throws CommandException {
            if (args.length == 0) {
                final var builder =
                        Component.text().append(Component.text("The current value of this configuration is: "));
                final var components = toComponents();
                if (components.size() == 1) {
                    builder.append(components.get(0));
                } else {
                    for (final var component : components) {
                        builder.appendNewline().append(component);
                    }
                }
                sender.sendMessage(builder);
                return;
            }

            if (args.length == 1 && args[0].equals("null")) {
                setValue(null);
                sender.sendMessage(Component.text("Config value has been changed to null").color(NamedTextColor.GREEN));
                return;
            }

            command.execute(sender, args);
        }

        @Override
        public List<String> tabComplete(final CommandSender sender, final String[] args) {
            return Collections.emptyList(); // TODO
        }
    }
}
