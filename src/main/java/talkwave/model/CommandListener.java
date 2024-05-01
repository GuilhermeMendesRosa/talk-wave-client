package talkwave.model;

import talkwave.integration.Client;
import talkwave.integration.dto.MessageDTO;
import talkwave.integration.MessageBuilder;
import talkwave.ui.ConsoleColors;
import talkwave.ui.MessagePrinter;
import talkwave.ui.SystemScanner;

public class CommandListener {

    private final Client client;

    public CommandListener(Client client) {
        this.client = client;
    }

    public void start() {
        while (true) {
            try {
                String commandLine = SystemScanner.get();
                MessageDTO message = new MessageBuilder(client.getUserId(), commandLine).build();
                client.send(message);
            } catch (InvalidCommandException ignored) {
                onInvalidCommand();
            } catch (Exception e) {
                MessagePrinter.println(ConsoleColors.RED,"Erro ao processar comando. Tente novamente.");
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
