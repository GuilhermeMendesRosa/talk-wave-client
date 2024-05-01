package talkwave.model;

public class StringValidator {

    public static boolean isNotBlank(String string) {
        if (string == null) return false;
        if (string.isBlank()) return false;

        return true;
    }
}