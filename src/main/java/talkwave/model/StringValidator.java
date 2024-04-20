package talkwave.model;

import java.util.List;

public class StringValidator {

    public static boolean isNotBlank(String string) {
        if (string == null) return false;
        if (string.isBlank()) return false;

        return true;
    }

    public static boolean validateCommand(String command) {
        try {
            if (!isNotBlank(command)) return false;

            Command.valueOf(command);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}