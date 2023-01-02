package io.github.lama06.zombies.command;

import io.github.lama06.zombies.ZombiesPlugin;
import io.github.lama06.zombies.ZombiesWorld;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.FinePosition;
import io.papermc.paper.math.Position;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public final class Require {
    private static final ZombiesPlugin PLUGIN = ZombiesPlugin.getInstance();

    private Require() {
    }

    public static void argsExact(final String[] args, final int number) throws CommandException {
        if (args.length != number) {
            throw new CommandException("This number of arguments that were given to this command is not correct");
        }
    }

    public static void argsAtLeast(final String[] args, final int number) throws CommandException {
        if (args.length < number) {
            throw new CommandException("This command needs more arguments");
        }
    }

    public static boolean bool(final String text) throws CommandException {
        class Constants {
            private static final Set<String> TRUE_STRINGS = Set.of("yes", "true", "on", "1");
            private static final Set<String> FALSE_STRINGS = Set.of("no", "false", "off", "0");
        }

        if (!Constants.TRUE_STRINGS.contains(text) && !Constants.FALSE_STRINGS.contains(text)) {
            throw new CommandException("This is not a boolean value: %s".formatted(text));
        }

        return Constants.TRUE_STRINGS.contains(text);
    }

    public static int integer(final String text) throws CommandException {
        try {
            return Integer.parseInt(text);
        } catch (final NumberFormatException e) {
            throw new CommandException("This is not a whole number: %s".formatted(text));
        }
    }

    public static float float32(final String text) throws CommandException {
        try {
            return Float.parseFloat(text);
        } catch (final NumberFormatException e) {
            throw new CommandException("This is not a floating point number: %s".formatted(text));
        }
    }

    public static double float64(final String text) throws CommandException {
        try {
            return Double.parseDouble(text);
        } catch (final NumberFormatException e) {
            throw new CommandException("This is not a floating point number: %s".formatted(text));
        }
    }


    public static NamespacedKey namespacedKey(final String name) throws CommandException {
        final var key = NamespacedKey.fromString(name);
        if (key == null) {
            throw new CommandException("%s is not a valid key".formatted(name));
        }
        return key;
    }

    public static BlockPosition blockPosition(final String x, final String y, final String z) throws CommandException {
        final var xPos = integer(x);
        final var yPos = integer(y);
        final var zPos = integer(z);
        return Position.block(xPos, yPos, zPos);
    }

    public static FinePosition finePosition(final String x, final String y, final String z) throws CommandException {
        final var xPos = float64(x);
        final var yPos = float64(y);
        final var zPos = float64(z);
        return Position.fine(xPos, yPos, zPos);
    }

    public static Material material(final String name) throws CommandException {
        final var key = namespacedKey(name);

        final var material = Registry.MATERIAL.get(key);
        if (material == null) {
            throw new CommandException("There is no block or item named %s".formatted(key));
        }

        return material;
    }

    public static BlockData blockData(final String text) throws CommandException {
        try {
            return Bukkit.createBlockData(text);
        } catch (final IllegalArgumentException e) {
            throw new CommandException("This is not a valid block data: %s".formatted(text));
        }
    }

    public static void op(final CommandSender sender) throws CommandException {
        final var NO_PERMISSION_MSG = "Only server operators can execute this command";

        if (sender instanceof ConsoleCommandSender || (sender instanceof Player player && player.isOp())) {
            return;
        }

        throw new CommandException(NO_PERMISSION_MSG);
    }

    public static Player player(final CommandSender sender) throws CommandException {
        if (!(sender instanceof Player player)) {
            throw new CommandException("This command can only be used by players");
        }

        return player;
    }

    public static Player onlinePlayer(final String name) throws CommandException {
        final var player = Bukkit.getPlayer(name);
        if (player == null) {
            throw new CommandException("No player with this name was found on the server: %s".formatted(name));
        }
        return player;
    }

    public static World world(final String name) throws CommandException {
        final var world = Bukkit.getWorld(name);
        if (world == null) {
            throw new CommandException("No world with this name was found: %s".formatted(name));
        }
        return world;
    }

    public static ZombiesWorld zombiesWorld(final String name) throws CommandException {
        final var world = world(name);
        if (!PLUGIN.getWorlds().containsKey(world)) {
            throw new CommandException("This world does not contains a zombies game: %s".formatted(name));
        }
        return PLUGIN.getWorlds().get(world);
    }
}