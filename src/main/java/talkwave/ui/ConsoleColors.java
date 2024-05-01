package talkwave.ui;

public enum ConsoleColors {

    RESET("\033[0m"),
    RED("\033[0;31m"),
    BLUE("\033[0;34m");

    private final String code;

    ConsoleColors(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
