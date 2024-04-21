package talkwave.model;

import java.util.Arrays;

public enum Command {

    SEND_FILE("send file"),
    SEND_MESSAGE("send message"),
    USERS("users"),
    EXIT("sair");

    private final String command;

    public static final String PREFIX = "/";

    Command(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public String getCommandWithPrefix() {
        return PREFIX + command;
    }

    public static Command getCommand(String command) {
        return Arrays.stream(Command.values())
                .filter(c -> command.startsWith(c.getCommandWithPrefix()))
                .findFirst()
                .orElse(null);
    }

    public boolean isSendFile() {
        return SEND_FILE.equals(this);
    }

    public boolean isSendMessage() {
        return SEND_MESSAGE.equals(this);
    }
}
