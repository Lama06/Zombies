package io.github.lama06.zombies.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.lama06.zombies.command.BranchCommandNode;
import io.github.lama06.zombies.command.CommandException;
import io.github.lama06.zombies.command.CommandNode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.*;

public abstract class CompoundConfig<T> extends Config<T> {
    private final Map<String, Config<?>> configs = new HashMap<>();
    private boolean isNull = true;

    protected abstract void setValueImplCompound(T newValue);

    protected abstract T getValueImplCompound(ConfigPath path) throws InvalidConfigException;

    protected final <U extends Config<?>> U registerConfig(final String name, final U config) {
        configs.put(name, config);
        return config;
    }

    @Override
    protected final T getValueImplConfig(final ConfigPath path) throws InvalidConfigException {
        if (isNull) {
            return null;
        }

        return getValueImplCompound(path);
    }

    @Override
    public final void setValueImplConfig(final T data) {
        if (data == null) {
            isNull = true;
            return;
        }

        setValueImplCompound(data);
        isNull = false;
    }

    @Override
    protected final JsonElement serializeImplConfig() {
        final var json = new JsonObject();
        for (final var name : configs.keySet()) {
            final var config = configs.get(name);
            json.add(name, config.serialize());
        }
        return json;
    }

    @Override
    protected final void deserializeImplConfig(final ConfigPath path, final JsonElement json)
            throws InvalidJsonException {
        if (!(json instanceof JsonObject object)) {
            throw new InvalidJsonException(path, "expected object or null");
        }

        for (final var name : object.keySet()) {
            final var configJson = object.get(name);
            final var config = configs.get(name);
            if (config == null) {
                throw new InvalidJsonException(path, "unexpected key: %s".formatted(name));
            }
            config.deserialize(path.append(name), configJson);
        }
    }

    @Override
    protected final List<Component> toComponentsImplConfig() {
        final var result = new ArrayList<Component>();
        for (final var name : configs.keySet()) {
            final var config = configs.get(name);
            final var components = config.toComponents();
            if (components.size() == 1) {
                result.add(Component.text(name + ": ").append(components.get(0)));
            } else {
                result.add(Component.text(name + ":"));
                for (final var component : components) {
                    result.add(Component.text("    ").append(component));
                }
            }
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    protected final CommandNode createCommandImplConfig() {
        return new Command();
    }

    private final class Command extends BranchCommandNode {
        @Override
        protected void registerSubcommands() {
            registerSubCommand("init", (sender, args) -> {
                if (!isNull) {
                    throw new CommandException("Already initialized");
                }

                for (final var name : configs.keySet()) {
                    final var config = configs.get(name);
                    config.setValue(null);
                }
                isNull = false;
                sender.sendMessage(Component.text("Configuration has been initialized").color(NamedTextColor.GREEN));
            });

            for (final var name : configs.keySet()) {
                final var config = configs.get(name);
                registerSubCommand(name, new CommandNode() {
                    @Override
                    public void execute(final CommandSender sender, final String[] args) throws CommandException {
                        if (isNull) {
                            throw new CommandException("Cannot access properties on an null object");
                        }

                        config.getCommand().execute(sender, args);
                    }

                    @Override
                    public List<String> tabComplete(final CommandSender sender, final String[] args) {
                        // TODO
                        return Collections.emptyList();
                    }
                });
            }
        }
    }
}
