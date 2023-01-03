package io.github.lama06.zombies;

import io.github.lama06.zombies.command.BranchCommandNode;
import io.github.lama06.zombies.command.CommandException;
import io.github.lama06.zombies.command.CommandNode;
import io.github.lama06.zombies.command.Require;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public final class ZombiesCommand extends BranchCommandNode {
    private static final ZombiesPlugin PLUGIN = ZombiesPlugin.getInstance();

    @Override
    protected void registerSubcommands() {
        registerSubCommand("worlds", new WorldsCommand());
        registerSubCommand("config", new ConfigCommand());
    }

    private static final class WorldsCommand extends BranchCommandNode {
        @Override
        protected void checkHasPermission(final CommandSender sender) throws CommandException {
            Require.op(sender);
        }

        @Override
        protected void registerSubcommands() {
            registerSubCommand("list", this::list);
            registerSubCommand("add", this::add);
            registerSubCommand("remove", this::remove);
        }

        private void list(final CommandSender sender, final String[] args) {
            final var builder =
                    Component.text().append(Component.text("These are the worlds containing a zombies game:"));
            for (final var entry : PLUGIN.getWorlds().entrySet()) {
                builder.appendNewline().append(Component.text("%s".formatted(entry.getKey().getName())));
            }
            sender.sendMessage(builder);
        }

        private void add(final CommandSender sender, final String[] args) throws CommandException {
            Require.argsExact(args, 1);

            final var world = Require.world(args[0]);

            if (PLUGIN.getWorlds().containsKey(world)) {
                throw new CommandException("A zombies game does already exists in this world");
            }

            PLUGIN.getWorlds().put(world, new ZombiesWorld(world));
            sender.sendMessage(Component.text("A zombies game has successfully been created in this world")
                    .color(NamedTextColor.GREEN));
        }

        private void remove(final CommandSender sender, final String[] args) throws CommandException {
            Require.argsExact(args, 1);
            final var zombiesWorld = Require.zombiesWorld(args[0]);
            PLUGIN.getWorlds().remove(zombiesWorld.getWorld());
            sender.sendMessage(Component.text("THe zombies game in this world has successfully been deleted")
                    .color(NamedTextColor.GREEN));
        }
    }

    private static class ConfigCommand implements CommandNode {
        @Override
        public void execute(final CommandSender sender, final String[] args) throws CommandException {
            Require.op(sender);
            Require.argsAtLeast(args, 1);
            final var zombiesWorld = Require.zombiesWorld(args[0]);
            zombiesWorld.getConfigCommandNode().execute(sender, Arrays.copyOfRange(args, 1, args.length));
        }
    }
}
