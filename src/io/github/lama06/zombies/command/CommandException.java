package io.github.lama06.zombies.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public final class CommandException extends Exception {
    private final Component errorMessage;

    public CommandException(final Component errorMessage) {
        super(PlainTextComponentSerializer.plainText().serialize(errorMessage));
        this.errorMessage = errorMessage;
    }

    public CommandException(final String message) {
        super(message);
        this.errorMessage = Component.text(message).color(NamedTextColor.RED);
    }

    public Component getErrorMessage() {
        return errorMessage;
    }
}
