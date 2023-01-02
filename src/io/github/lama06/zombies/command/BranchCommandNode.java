package io.github.lama06.zombies.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;

import java.util.*;

public abstract class BranchCommandNode implements CommandNode {
    private final Map<String, CommandNode> subCommands = new HashMap<>();

    {
        subCommands.put("help", new HelpCommandNode());
        registerSubcommands();
    }

    protected void registerSubCommand(final String name, final CommandNode commandNode) {
        subCommands.put(name, commandNode);
    }

    protected abstract void registerSubcommands();

    protected void checkHasPermission(final CommandSender sender) throws CommandException {
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) throws CommandException {
        checkHasPermission(sender);

        if (args.length == 0 && subCommands.containsKey("")) {
            subCommands.get("").execute(sender, args);
            return;
        }

        Require.argsAtLeast(args, 1);

        if (!subCommands.containsKey(args[0])) {
            throw new CommandException("No sub command named: %s".formatted(args[0]));
        }

        subCommands.get(args[0]).execute(sender, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String[] args) {
        return Collections.emptyList(); // TODO
    }

    private final class HelpCommandNode implements CommandNode {
        @Override
        public void execute(final CommandSender sender, final String[] args) {
            final var builder = Component.text()
                    .append(Component.text("Available sub commands:").decorate(TextDecoration.UNDERLINED));
            for (final var subCommand : subCommands.entrySet()) {
                if (subCommand.getValue() instanceof HelpCommandNode) {
                    continue;
                }

                builder.appendNewline().append(Component.text("- %s".formatted(subCommand.getKey())));
            }
            sender.sendMessage(builder);
        }
    }
}
