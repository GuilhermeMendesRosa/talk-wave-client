package talkwave.model;

public class CommandListener {

    private final Client client;

    public CommandListener(Client client) {
        this.client = client;
    }

    public void start() {
        String commandLine;
        do {
            commandLine = SystemScanner.get();
            Command command = Command.getCommand(commandLine);
            if (command == null) {
                onInvalidCommand();
                continue;
            }

            if (command.isSendFile()) {
                client.sendFile(commandLine);
                continue;
            }

            client.sendMessage(commandLine);
        } while (!commandLine.equals("/sair"));
    }

    private void onInvalidCommand() {
        System.out.println("Comando inválido. Tente novamente.");
        System.out.println("Comandos disponíveis: ");
        for (Command command : Command.values()) {
            System.out.println(command.getCommand());
        }
    }
}
