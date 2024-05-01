package talkwave.model;

import java.util.Arrays;
import java.util.List;

public enum CommandType {

    SEND_FILE("send file"),
    SEND_MESSAGE("send message"),
    USERS("users"),
    EXIT("sair"),
    BANNED("banned");

    private final String command;

    public static final String PREFIX = "/";

    CommandType(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public String getCommandWithPrefix() {
        return PREFIX + command;
    }

    public static List<CommandType> listInvokableCommands() {
        return Arrays.asList(SEND_FILE, SEND_MESSAGE, USERS, EXIT);
    }

    public static CommandType getCommand(String command) {
        return Arrays.stream(CommandType.values())
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
