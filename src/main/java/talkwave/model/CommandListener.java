package talkwave.model;

public class CommandListener {

    private final Client client;

    public CommandListener(Client client) {
        this.client = client;
    }

    public void start() {
        while (true) {
            String commandLine = SystemScanner.get();
            Command command = Command.getCommand(commandLine);

            if (command == null) {
                onInvalidCommand();
                continue;
            }

            switch (command) {
                case SEND_MESSAGE -> {
                    String message = commandLine.replace(command.getCommandWithPrefix() + " ", "");
                    client.sendMessage(message);
                }
                case USERS -> {
                    client.sendListUsersMessage();
                }
                case SEND_FILE -> {
                    client.sendFile(commandLine);
                }
                case EXIT -> {
                    return;
                }
            }
        }
    }

    private void onInvalidCommand() {
        MessagePrinter.println(ConsoleColors.RED,"Comando inválido. Tente novamente.");
        MessagePrinter.println(ConsoleColors.BLUE,"Comandos disponíveis: ");
        for (Command command : Command.values()) {
            MessagePrinter.println(ConsoleColors.BLUE,command.getCommandWithPrefix());
        }
    }
}
