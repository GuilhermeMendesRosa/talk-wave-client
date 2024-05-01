package talkwave.model;

import talkwave.integration.Client;
import talkwave.integration.Message;
import talkwave.integration.MessageBuilder;

public class CommandListener {

    private final Client client;

    public CommandListener(Client client) {
        this.client = client;
    }

    public void start() {
        while (true) {
            try {
                String commandLine = SystemScanner.get();
                Message message = new MessageBuilder(client.getUserId(), commandLine).build();
                client.send(message);
            } catch (InvalidCommandException ignored) {
                onInvalidCommand();
            }
        }
    }

    private void onInvalidCommand() {
        MessagePrinter.println(ConsoleColors.RED,"Comando inválido. Tente novamente.");
        MessagePrinter.println(ConsoleColors.BLUE,"Comandos disponíveis: ");
        for (CommandType command : CommandType.listInvokableCommands()) {
            MessagePrinter.println(ConsoleColors.BLUE,command.getCommandWithPrefix());
        }
    }
}
