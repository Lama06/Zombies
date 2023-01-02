package io.github.lama06.zombies.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Collections;
import java.util.List;

@FunctionalInterface
public interface CommandNode extends TabExecutor {
    static void registerCommand(final String name, final CommandNode cmd) {
        final var pluginCommand = Bukkit.getPluginCommand(name);
        if (pluginCommand == null) {
            throw new IllegalArgumentException("invalid command name: %s".formatted(name));
        }
        pluginCommand.setExecutor(cmd);
        pluginCommand.setTabCompleter(cmd);
    }

    void execute(CommandSender sender, String[] args) throws CommandException;

    default List<String> tabComplete(final CommandSender sender, final String[] args) {
        return Collections.emptyList();
    }

    @Override
    default boolean onCommand(
            final CommandSender sender, final Command command, final String label, final String[] args
    ) {
        try {
            execute(sender, args);
        } catch (final CommandException e) {
            sender.sendMessage(e.getErrorMessage());
        }
        return true;
    }

    @Override
    default List<String> onTabComplete(
            final CommandSender sender, final Command command, final String label, final String[] args
    ) {
        return tabComplete(sender, args);
    }
}
