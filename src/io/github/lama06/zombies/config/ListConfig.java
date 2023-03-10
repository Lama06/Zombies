package io.github.lama06.zombies.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import io.github.lama06.zombies.command.CommandException;
import io.github.lama06.zombies.command.CommandNode;
import io.github.lama06.zombies.command.Require;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public final class ListConfig<T> extends Config<List<T>> {
    private final Supplier<? extends Config<T>> configConstructor;
    private List<Config<T>> configs = null;

    public ListConfig(final Supplier<? extends Config<T>> configConstructor) {
        this.configConstructor = configConstructor;
    }

    @Override
    protected JsonElement serializeImplConfig() {
        final var json = new JsonArray();
        for (final var config : configs) {
            json.add(config.serialize());
        }
        return json;
    }

    @Override
    protected void deserializeImplConfig(final ConfigPath path, final JsonElement json) throws InvalidJsonException {
        if (!(json instanceof JsonArray array)) {
            throw new InvalidJsonException(path, "expected array or null");
        }

        final var configs = new ArrayList<Config<T>>();
        var index = 0;
        for (final var configJson : array) {
            final var config = configConstructor.get();
            config.deserialize(path.append(String.valueOf(index)), configJson);
            configs.add(config);
            index++;
        }
        this.configs = configs;
    }

    @Override
    protected List<Component> toComponentsImplConfig() {
        if (configs.size() == 0) {
            return List.of(Component.text("[]"));
        }

        final var result = new ArrayList<Component>();
        var index = 1;
        for (final var config : configs) {
            final var components = config.toComponents();
            if (components.size() == 1) {
                result.add(Component.text(index + ": ").append(components.get(0)));
            } else {
                result.add(Component.text(index + ":"));
                for (final var component : components) {
                    result.add(Component.text("    ").append(component));
                }
            }
            index++;
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    protected List<T> getValueImplConfig(final ConfigPath path) throws InvalidConfigException {
        if (configs == null) {
            return null;
        }

        final var result = new ArrayList<T>();
        var index = 0;
        for (final var config : configs) {
            result.add(config.getValue(path.append(String.valueOf(index))));
            index++;
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public void setValueImplConfig(final List<T> data) {
        if (data == null) {
            configs = null;
            return;
        }

        configs = new ArrayList<>();

        for (final var element : data) {
            final var config = configConstructor.get();
            config.setValue(element);
            configs.add(config);
        }
    }

    @Override
    protected CommandNode createCommandImplConfig() {
        return new Command();
    }

    private final class Command implements CommandNode {
        private int requireIndex(final String text) throws CommandException {
            if (text.equals("end") || text.equals("last") || text.equals("-1")) {
                return configs.size();
            } else {
                final var index = Require.integer(text);
                if (index < 0 || index > configs.size()) {
                    throw new CommandException("Index out of range");
                }
                return index;
            }
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) throws CommandException {
            Require.argsAtLeast(args, 1);

            if (args[0].equals("init")) {
                if (configs != null) {
                    throw new CommandException("Already initialized");
                }

                configs = new ArrayList<>();
                sender.sendMessage(Component.text("Map has been initialized").color(NamedTextColor.GREEN));
                return;
            }

            if (configs == null) {
                throw new CommandException("Cannot perform operations on null list");
            }

            if (args[0].equals("add")) {
                Require.argsExact(args, 2);
                final var index = requireIndex(args[1]);
                final var newConfig = configConstructor.get();
                configs.add(index, newConfig);
                sender.sendMessage(Component.text("Successfully added new element to array")
                        .color(NamedTextColor.GREEN));
                return;
            }

            if (args[0].equals("remove")) {
                Require.argsExact(args, 2);
                final var index = requireIndex(args[1]);
                configs.remove(index);
                sender.sendMessage(Component.text("Successfully removed element from array")
                        .color(NamedTextColor.GREEN));
                return;
            }

            final var index = requireIndex(args[0]);
            final var config = configs.get(index);
            config.getCommand().execute(sender, Arrays.copyOfRange(args, 1, args.length));
        }

        @Override
        public List<String> tabComplete(final CommandSender sender, final String[] args) {
            return Collections.emptyList(); // TODO
        }
    }
}
