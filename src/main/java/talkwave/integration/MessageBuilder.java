package talkwave.integration;

import talkwave.integration.dto.FileDTO;
import talkwave.integration.dto.MessageDTO;
import talkwave.model.CommandType;
import talkwave.model.InvalidCommandException;

import java.io.File;
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

    public MessageDTO build() throws InvalidCommandException {
        CommandType commandType = CommandType.getCommand(commandLine);
        if (commandType == null) throw new InvalidCommandException("Comando inválido");

        switch (commandType) {
            case USERS, EXIT -> {
                return new MessageDTO(userId, commandType);
            }
            case SEND_MESSAGE -> {
                return new MessageDTO(userId, getRecipientList(), getArgument(1), CommandType.SEND_MESSAGE);
            }
            case SEND_FILE -> {
                String filePath = getArgument(1);

                File file = new File(filePath);
                String base64File = FileHelper.encodeFileToBase64(file);
                if (base64File == null) return null;

                FileDTO fileDTO = new FileDTO(file.getName(), base64File);
                return new MessageDTO(userId, getRecipientList(), fileDTO, CommandType.SEND_FILE);
            }
            default -> throw new InvalidCommandException("Comando inválido");
        }
    }

    private String getArgument(int index) {
        final int maxArgs = 2;
        String messageArgs = commandLine
                .replace(commandType.getCommandWithPrefix() + " ", "");
        return messageArgs.split(" ", maxArgs)[index];
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
