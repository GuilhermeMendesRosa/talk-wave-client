package talkwave.model;

import talkwave.integration.Client;

public class CommandListener {

    private final Client client;

    public CommandListener(Client client) {
        this.client = client;
    }

    public void start() {
        while (true) {
            String commandLine = SystemScanner.get();
            CommandType commandType = CommandType.getCommand(commandLine);

            if (commandType == null) {
                onInvalidCommand();
                continue;
            }

            switch (commandType) {
                case SEND_MESSAGE -> {
                    String message = commandLine.replace(commandType.getCommandWithPrefix() + " ", "");
                    client.sendMessage(message);
                }
                case USERS -> client.sendListUsersMessage();
                case SEND_FILE -> client.sendFile(commandLine);
                case EXIT -> {
                    return;
                }
            }
        }
    }

    private void onInvalidCommand() {
        MessagePrinter.println(ConsoleColors.RED,"Comando inválido. Tente novamente.");
        MessagePrinter.println(ConsoleColors.BLUE,"Comandos disponíveis: ");
        for (CommandType command : CommandType.values()) {
            MessagePrinter.println(ConsoleColors.BLUE,command.getCommandWithPrefix());
        }
    }
}
