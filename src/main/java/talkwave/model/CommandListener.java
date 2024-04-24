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
                    String messageContent = commandLine.replace(command.getCommandWithPrefix() + " ", "");
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
        System.out.println("Comando inválido. Tente novamente.");
        System.out.println("Comandos disponíveis: ");
        for (Command command : Command.values()) {
            System.out.println(command.getCommandWithPrefix());
        }
    }
}
