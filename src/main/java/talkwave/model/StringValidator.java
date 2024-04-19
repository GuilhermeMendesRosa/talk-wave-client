package talkwave.model;

import java.util.List;

public class StringValidator {

    public static final List<String> COMMAND_PALETTE = List.of(
            "sair",
            "users",
            "send message",
            "send file"
    );

    public static boolean isNotBlank(String string) {
        if (string == null) return false;
        if (string.isBlank()) return false;

        return true;
    }

    public static boolean validateCommand(String command) {
        if (!isNotBlank(command)) return false;

        return COMMAND_PALETTE.stream().anyMatch(command::startsWith);
    }
}