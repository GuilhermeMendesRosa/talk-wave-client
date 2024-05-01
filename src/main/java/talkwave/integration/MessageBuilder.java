package talkwave.integration;

import talkwave.model.CommandType;
import talkwave.model.InvalidCommandException;

import java.util.Arrays;
import java.util.List;

public class MessageBuilder {

    final String userId;
    final String commandLine;
    final CommandType commandType;

    public MessageBuilder(String userId, String commandLine) {
        this.userId = userId;
        this.commandLine = commandLine;
        this.commandType = CommandType.getCommand(commandLine);
    }

    public Message build() throws InvalidCommandException {
        CommandType commandType = CommandType.getCommand(commandLine);
        if (commandType == null) throw new InvalidCommandException("Comando inválido");

        switch (commandType) {
            case USERS, EXIT -> {
                return new Message(userId, commandType);
            }
            case SEND_MESSAGE -> {
                return new Message(userId, getRecipientList(), getArgument(1), CommandType.SEND_MESSAGE);
            }
            case SEND_FILE -> {
                String filePath = getArgument(1);
                String base64File = FileHelper.encodeFileToBase64(filePath);
                if (base64File == null) return null;
                return new Message(userId, getRecipientList(), base64File, CommandType.SEND_FILE);
            }
            default -> throw new InvalidCommandException("Comando inválido");
        }
    }

    private String getArgument(int index) {
        String messageArgs = commandLine
                .replace(commandType.getCommandWithPrefix() + " ", "");
        return messageArgs.split(" ")[index];
    }

    private List<String> getRecipientList() {
        String messageArgs = commandLine
                .replace(commandType.getCommandWithPrefix() + " ", "");

        if (messageArgs.contains("to:")) {
            String[] parts = messageArgs.split("\\s*to:\\s*|\\s+", 3);

            return Arrays.asList(parts[1].trim().split(","));
        } else {
            return List.of(getArgument(0));
        }
    }
}
