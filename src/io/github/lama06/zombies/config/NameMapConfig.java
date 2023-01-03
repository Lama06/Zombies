package io.github.lama06.zombies.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.lama06.zombies.command.CommandException;
import io.github.lama06.zombies.command.CommandNode;
import io.github.lama06.zombies.command.Require;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.function.Supplier;

public final class NameMapConfig<T> extends Config<Map<String, T>> {
    private final Supplier<? extends Config<T>> configConstructor;
    private Map<String, Config<T>> value = null;

    public NameMapConfig(final Supplier<? extends Config<T>> configConstructor) {
        this.configConstructor = configConstructor;
    }

    @Override
    protected boolean isNullImplConfig() {
        return value == null;
    }

    @Override
    protected JsonElement serializeImplConfig() {
        final var json = new JsonObject();
        for (final var name : value.keySet()) {
            final var element = value.get(name);
            json.add(name, element.serialize());
        }
        return json;
    }

    @Override
    protected void deserializeImplConfig(final ConfigPath path, final JsonElement json) throws InvalidJsonException {
        if (!(json instanceof JsonObject object)) {
            throw new InvalidJsonException(path, "expected object or null");
        }

        value = new HashMap<>();

        for (final var name : object.keySet()) {
            final var elementJson = object.get(name);
            final var element = configConstructor.get();
            element.deserialize(path.append(name), elementJson);
            value.put(name, element);
        }
    }

    @Override
    protected List<Component> toComponentsImplConfig() {
        if (value.size() == 0) {
            return List.of(Component.text("{}"));
        }

        final var result = new ArrayList<Component>();
        for (final var name : value.keySet()) {
            final var element = value.get(name);
            final var components = element.toComponents();
            if (components.size() == 1) {
                result.add(Component.text(name + ": ").append(components.get(0)));
            } else {
                result.add(Component.text(name + ": "));
                for (final var component : components) {
                    result.add(Component.text("    ").append(component));
                }
            }
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    protected Map<String, T> getValueImplConfig(final ConfigPath path) throws InvalidConfigException {
        final var result = new HashMap<String, T>();
        for (final var name : value.keySet()) {
            final var config = value.get(name);
            result.put(name, config.getValue(path.append(name)));
        }
        return Collections.unmodifiableMap(result);
    }

    @Override
    protected void setValueImplConfig(final Map<String, T> newValue) {
        if (newValue == null) {
            value = null;
            return;
        }

        value = new HashMap<>();
        for (final var name : newValue.keySet()) {
            final var element = newValue.get(name);
            final var config = configConstructor.get();
            config.setValue(element);
            value.put(name, config);
        }
    }

    @Override
    protected CommandNode createCommandImplConfig() {
        return new Command();
    }

    private final class Command implements CommandNode {
        @Override
        public void execute(final CommandSender sender, final String[] args) throws CommandException {
            Require.argsAtLeast(args, 1);

            if (args[0].equals("init")) {
                value = new HashMap<>();
                sender.sendMessage(Component.text("Map has been initialized").color(NamedTextColor.GREEN));
                return;
            }

            if (value == null) {
                throw new CommandException("Not initialized");
            }

            if (args[0].equals("add")) {
                Require.argsExact(args, 2);
                final var name = args[1];
                final var newConfig = configConstructor.get();
                value.put(name, newConfig);
                sender.sendMessage(Component.text("Successfully added new element").color(NamedTextColor.GREEN));
                return;
            }

            if (args[0].equals("remove")) {
                Require.argsExact(args, 2);
                final var name = args[1];
                final var previous = value.remove(name);
                if (previous == null) {
                    throw new CommandException("Name not found");
                } else {
                    sender.sendMessage(Component.text("Successfully removed").color(NamedTextColor.GREEN));
                }
            }

            final var name = args[0];
            final var element = value.get(name);
            if (element == null) {
                throw new CommandException("Name not found");
            }
            element.getCommand().execute(sender, Arrays.copyOfRange(args, 1, args.length));
        }

        @Override
        public List<String> tabComplete(final CommandSender sender, final String[] args) {
            // TODO
            return Collections.emptyList();
        }
    }
}
